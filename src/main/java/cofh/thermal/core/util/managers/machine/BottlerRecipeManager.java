package cofh.thermal.core.util.managers.machine;

import cofh.core.util.helpers.FluidHelper;
import cofh.lib.api.fluid.IFluidStackHolder;
import cofh.lib.api.inventory.IItemStackHolder;
import cofh.lib.common.fluid.FluidIngredient;
import cofh.lib.util.crafting.ComparableItemStack;
import cofh.thermal.core.ThermalCore;
import cofh.thermal.core.common.item.FlorbItem;
import cofh.thermal.core.util.recipes.machine.BottlerRecipe;
import cofh.thermal.core.util.recipes.machine.BottlerRecipeNBT;
import cofh.thermal.lib.util.managers.AbstractManager;
import cofh.thermal.lib.util.managers.IRecipeManager;
import cofh.thermal.lib.util.recipes.IThermalInventory;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import cofh.thermal.lib.util.recipes.internal.IMachineRecipe;
import cofh.thermal.lib.util.recipes.internal.SimpleMachineRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.*;

import static cofh.core.init.CoreFluids.POTION_FLUID;
import static cofh.lib.util.Constants.BOTTLE_VOLUME;
import static cofh.lib.util.Constants.BUCKET_VOLUME;
import static cofh.lib.util.Utils.getName;
import static cofh.lib.util.Utils.getRegistryName;
import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static cofh.thermal.core.ThermalCore.ITEMS;
import static cofh.thermal.core.init.registries.TCoreRecipeTypes.BOTTLER_RECIPE;
import static cofh.thermal.lib.util.ThermalIDs.ID_FLORB;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class BottlerRecipeManager extends AbstractManager implements IRecipeManager {

    private static final BottlerRecipeManager INSTANCE = new BottlerRecipeManager();
    protected static final int DEFAULT_ENERGY = 400;

    protected boolean defaultBucketRecipes = true;
    protected boolean defaultFlorbRecipes = true;
    protected boolean defaultPotionRecipes = true;

    protected Map<List<Integer>, IMachineRecipe> recipeMap = new Object2ObjectOpenHashMap<>();
    protected Set<Fluid> validFluids = new ObjectOpenHashSet<>();
    protected Set<ComparableItemStack> validItems = new ObjectOpenHashSet<>();

    protected int maxOutputItems;
    protected int maxOutputFluids;

    public static BottlerRecipeManager instance() {

        return INSTANCE;
    }

    protected BottlerRecipeManager() {

        super(DEFAULT_ENERGY);
        this.maxOutputItems = 1;
        this.maxOutputFluids = 0;
    }

    public void setDefaultBucketRecipes(boolean defaultBucketRecipes) {

        this.defaultBucketRecipes = defaultBucketRecipes;
    }

    public void setDefaultFlorbRecipes(boolean defaultFlorbRecipes) {

        this.defaultFlorbRecipes = defaultFlorbRecipes;
    }

    public void setDefaultPotionRecipes(boolean defaultPotionRecipes) {

        this.defaultPotionRecipes = defaultPotionRecipes;
    }

    public void addRecipe(ThermalRecipe recipe) {

        if (!recipe.getInputItems().isEmpty()) {
            for (ItemStack recipeInput : recipe.getInputItems().get(0).getItems()) {
                for (FluidStack fluidInput : recipe.getInputFluids().get(0).getFluids()) {
                    addRecipe(recipe.getEnergy(), recipe.getXp(), singletonList(recipeInput), singletonList(fluidInput), recipe.getOutputItems(), recipe.getOutputItemChances(), recipe.getOutputFluids());
                }
            }
        } else {
            for (FluidStack fluidInput : recipe.getInputFluids().get(0).getFluids()) {
                addRecipe(recipe.getEnergy(), recipe.getXp(), Collections.emptyList(), singletonList(fluidInput), recipe.getOutputItems(), recipe.getOutputItemChances(), recipe.getOutputFluids());
            }
        }
    }

    public boolean validItem(ItemStack item) {

        return validItems.contains(makeComparable(item));
    }

    public boolean validFluid(FluidStack fluid) {

        return validFluids.contains(fluid.getFluid());
    }

    protected void clear() {

        recipeMap.clear();
        validFluids.clear();
        validItems.clear();
        convertedRecipes.clear();
    }

    // region RECIPES
    protected IMachineRecipe getRecipe(List<? extends IItemStackHolder> inputSlots, List<? extends IFluidStackHolder> inputTanks) {

        if (inputSlots.isEmpty() && inputTanks.isEmpty() || inputSlots.get(0).isEmpty() && inputTanks.get(0).isEmpty()) {
            return null;
        }
        if (inputTanks.isEmpty() || inputTanks.get(0).isEmpty()) {
            ItemStack inputItem = inputSlots.get(0).getItemStack();
            return recipeMap.get(singletonList(makeComparable(inputItem).hashCode()));
        }
        if (inputSlots.isEmpty() || inputSlots.get(0).isEmpty()) {
            FluidStack inputFluid = inputTanks.get(0).getFluidStack();
            return recipeMap.get(singletonList(FluidHelper.fluidHashcodeNoTag(inputFluid)));
        }
        ItemStack inputItem = inputSlots.get(0).getItemStack();
        FluidStack inputFluid = inputTanks.get(0).getFluidStack();
        return recipeMap.get(asList(makeComparable(inputItem).hashCode(), FluidHelper.fluidHashcodeNoTag(inputFluid)));
    }

    protected IMachineRecipe addRecipe(int energy, float experience, List<ItemStack> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems, List<Float> chance, List<FluidStack> outputFluids) {

        if (inputItems.isEmpty() || inputFluids.isEmpty() || outputItems.size() > maxOutputItems || outputFluids.size() > maxOutputFluids || energy <= 0) {
            return null;
        }
        ItemStack inputItem = inputItems.get(0);
        if (inputItem.isEmpty()) {
            return null;
        }
        FluidStack inputFluid = inputFluids.get(0);
        if (inputFluid.isEmpty()) {
            return null;
        }
        for (ItemStack stack : outputItems) {
            if (stack.isEmpty()) {
                return null;
            }
        }
        validItems.add(makeComparable(inputItem));
        validFluids.add(inputFluid.getFluid());
        energy = (int) (energy * getDefaultScale());

        SimpleMachineRecipe recipe = new SimpleMachineRecipe(energy, experience, inputItems, inputFluids, outputItems, chance, outputFluids);
        recipeMap.put(asList(makeComparable(inputItem).hashCode(), FluidHelper.fluidHashcodeNoTag(inputFluid)), recipe);
        return recipe;
    }

    protected IMachineRecipe addRecipe(IMachineRecipe recipe) {

        ItemStack inputItem = recipe.getInputItems().get(0);
        if (inputItem.isEmpty()) {
            return null;
        }
        FluidStack inputFluid = recipe.getInputFluids().get(0);
        if (inputFluid.isEmpty()) {
            return null;
        }
        validItems.add(makeComparable(inputItem));
        validFluids.add(inputFluid.getFluid());
        recipeMap.put(asList(makeComparable(inputItem).hashCode(), FluidHelper.fluidHashcodeNoTag(inputFluid)), recipe);
        return recipe;
    }
    // endregion

    // region IRecipeManager
    @Override
    public IMachineRecipe getRecipe(IThermalInventory inventory) {

        return getRecipe(inventory.inputSlots(), inventory.inputTanks());
    }

    @Override
    public List<IMachineRecipe> getRecipeList() {

        return new ArrayList<>(recipeMap.values());
    }
    // endregion

    // region IManager
    @Override
    public void refresh(RecipeManager recipeManager) {

        clear();
        var recipes = recipeManager.byType(BOTTLER_RECIPE.get());
        for (var entry : recipes.entrySet()) {
            addRecipe(entry.getValue());
        }

        int energy = (int) (getDefaultEnergy() * getDefaultScale());
        if (defaultBucketRecipes) {
            ThermalCore.LOG.debug("Adding default Bucket recipes to the Fluid Encapsulator...");
            Set<Fluid> bucketFluids = new ObjectOpenHashSet<>(32);
            for (Fluid fluid : ForgeRegistries.FLUIDS) {
                if (fluid instanceof FlowingFluid flowing) {
                    Fluid still = null;
                    try {
                        still = flowing.getSource();
                    } catch (Exception e) {
                        ThermalCore.LOG.error("Fluid " + getRegistryName(fluid) + " had a critical error when attempting to query its still form!");
                    }
                    if (still == null) {
                        ThermalCore.LOG.error("Fluid " + getRegistryName(fluid) + " returned a null value for its Still Fluid! This is an error. Report this to the mod author. Probable mod: " + getRegistryName(fluid).getNamespace());
                        continue;
                    }
                    ItemStack bucket = new ItemStack(still.getBucket());
                    if (!bucket.isEmpty() && !bucketFluids.contains(still)) {
                        addRecipe(convert(energy, 0.0F, new ItemStack(Items.BUCKET), new FluidStack(still, BUCKET_VOLUME), bucket));
                        bucketFluids.add(still);
                    }
                }
            }
        }

        if (defaultFlorbRecipes) {
            ThermalCore.LOG.debug("Adding default Florb recipes to the Fluid Encapsulator...");
            Set<Fluid> florbFluids = new ObjectOpenHashSet<>(32);
            for (Fluid fluid : ForgeRegistries.FLUIDS) {
                if (fluid instanceof FlowingFluid flowing) {
                    Fluid still = null;
                    try {
                        still = flowing.getSource();
                    } catch (Exception e) {
                        ThermalCore.LOG.error("Fluid " + getRegistryName(fluid) + " had a critical error when attempting to query its still form!");
                    }
                    if (still == null) {
                        ThermalCore.LOG.error("Fluid " + getRegistryName(fluid) + " returned a null value for its Still Fluid! This is an error. Report this to the mod author. Probable mod: " + getRegistryName(fluid).getNamespace());
                        continue;
                    }
                    if (still.defaultFluidState().createLegacyBlock().isAir()) {
                        continue;
                    }
                    ItemStack florb = new ItemStack(ITEMS.get(ID_FLORB));
                    ((FlorbItem) florb.getItem()).fill(florb, new FluidStack(still, BUCKET_VOLUME), EXECUTE);
                    if (!((FlorbItem) florb.getItem()).getFluid(florb).isEmpty() && !florbFluids.contains(still)) {
                        addRecipe(convert(energy, 0.0F, new ItemStack(ITEMS.get(ID_FLORB)), new FluidStack(still, BUCKET_VOLUME), florb));
                        florbFluids.add(still);
                    }
                }
            }
        }

        if (defaultPotionRecipes) {
            ThermalCore.LOG.debug("Adding default Potion recipes to the Fluid Encapsulator...");
            addRecipe(convert(energy, 0.0F, new ItemStack(Items.GLASS_BOTTLE), new FluidStack(POTION_FLUID.get(), BOTTLE_VOLUME), new ItemStack(Items.POTION)));
        }
    }
    // endregion

    // region CONVERSION
    protected List<BottlerRecipe> convertedRecipes = new ArrayList<>();

    public List<BottlerRecipe> getConvertedRecipes() {

        return convertedRecipes;
    }

    protected BottlerRecipeNBT convert(int energy, float experience, @Nonnull ItemStack inputItem, @Nonnull FluidStack inputFluid, @Nonnull ItemStack outputItem) {

        convertedRecipes.add(new BottlerRecipe(new ResourceLocation(ID_THERMAL, "bottler_" + getName(outputItem)), energy, experience, singletonList(Ingredient.of(inputItem)), singletonList(FluidIngredient.of(inputFluid).setAmount(inputFluid.getAmount())), singletonList(outputItem), emptyList(), emptyList()));
        return new BottlerRecipeNBT(energy, experience, inputItem, inputFluid, outputItem);
    }
    // endregion
}
