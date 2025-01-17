package cofh.thermal.core.util.recipes.machine;

import cofh.lib.common.fluid.FluidIngredient;
import cofh.thermal.core.ThermalCore;
import cofh.thermal.core.util.managers.machine.InsolatorRecipeManager;
import cofh.thermal.lib.util.recipes.MachineRecipeSerializer;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.recipes.RecipeJsonUtils.*;
import static cofh.thermal.core.init.registries.TCoreRecipeSerializers.INSOLATOR_RECIPE_SERIALIZER;
import static cofh.thermal.core.init.registries.TCoreRecipeTypes.INSOLATOR_RECIPE;

public class InsolatorRecipe extends ThermalRecipe {

    public InsolatorRecipe(ResourceLocation recipeId, int energy, float experience, List<Ingredient> inputItems, List<FluidIngredient> inputFluids, List<ItemStack> outputItems, List<Float> outputItemChances, List<FluidStack> outputFluids) {

        super(recipeId, energy, experience, inputItems, inputFluids, outputItems, outputItemChances, outputFluids);

        if (this.energy <= 0) {
            int defaultEnergy = InsolatorRecipeManager.instance().getDefaultEnergy();
            ThermalCore.LOG.warn("Energy value for " + recipeId + " was out of allowable range and has been set to a default value of " + defaultEnergy + ".");
            this.energy = defaultEnergy;
        }
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {

        return INSOLATOR_RECIPE_SERIALIZER.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {

        return INSOLATOR_RECIPE.get();
    }

    // region SERIALIZER
    public static class Serializer<T extends ThermalRecipe> extends MachineRecipeSerializer<T> {

        protected final int defaultWater;

        public Serializer(IFactory<T> recipeFactory, int defaultEnergy, int defaultWater) {

            super(recipeFactory, defaultEnergy);
            this.defaultWater = defaultWater;
        }

        @Override
        public T fromJson(ResourceLocation recipeId, JsonObject json) {

            int energy = defaultEnergy;
            int water = defaultWater;
            float experience = 0.0F;

            ArrayList<Ingredient> inputItems = new ArrayList<>();
            ArrayList<FluidIngredient> inputFluids = new ArrayList<>();
            ArrayList<ItemStack> outputItems = new ArrayList<>();
            ArrayList<Float> outputItemChances = new ArrayList<>();
            ArrayList<FluidStack> outputFluids = new ArrayList<>();

            /* INPUT */
            if (json.has(INGREDIENT)) {
                parseInputs(inputItems, inputFluids, json.get(INGREDIENT));
            } else if (json.has(INGREDIENTS)) {
                parseInputs(inputItems, inputFluids, json.get(INGREDIENTS));
            } else if (json.has(INPUT)) {
                parseInputs(inputItems, inputFluids, json.get(INPUT));
            } else if (json.has(INPUTS)) {
                parseInputs(inputItems, inputFluids, json.get(INPUTS));
            }

            /* OUTPUT */
            if (json.has(RESULT)) {
                parseOutputs(outputItems, outputItemChances, outputFluids, json.get(RESULT));
            } else if (json.has(RESULTS)) {
                parseOutputs(outputItems, outputItemChances, outputFluids, json.get(RESULTS));
            } else if (json.has(OUTPUT)) {
                parseOutputs(outputItems, outputItemChances, outputFluids, json.get(OUTPUT));
            } else if (json.has(OUTPUTS)) {
                parseOutputs(outputItems, outputItemChances, outputFluids, json.get(OUTPUTS));
            }

            /* ENERGY */
            if (json.has(ENERGY)) {
                energy = json.get(ENERGY).getAsInt();
            }
            if (json.has(ENERGY_MOD)) {
                energy *= json.get(ENERGY_MOD).getAsFloat();
            }
            /* EXPERIENCE */
            if (json.has(EXPERIENCE)) {
                experience = json.get(EXPERIENCE).getAsFloat();
            }
            /* WATER */
            if (json.has(WATER)) {
                water = json.get(WATER).getAsInt();
            }
            if (json.has(WATER_MOD)) {
                water *= json.get(WATER_MOD).getAsFloat();
            }
            if (inputFluids.isEmpty()) {
                inputFluids.add(FluidIngredient.of(new FluidStack(Fluids.WATER, water)));
            }
            if (inputItems.isEmpty() || outputItems.isEmpty() && outputFluids.isEmpty()) {
                throw new JsonSyntaxException("Invalid Thermal Series recipe: " + recipeId + "\nRefer to the recipe's ResourceLocation to find the mod responsible and let them know!");
            }
            return factory.create(recipeId, energy, experience, inputItems, inputFluids, outputItems, outputItemChances, outputFluids);
        }

    }
    // endregion
}
