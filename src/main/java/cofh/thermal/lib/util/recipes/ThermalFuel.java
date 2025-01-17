package cofh.thermal.lib.util.recipes;

import cofh.lib.common.fluid.FluidIngredient;
import cofh.lib.util.recipes.SerializableRecipe;
import cofh.thermal.core.ThermalCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public abstract class ThermalFuel extends SerializableRecipe {

    protected final List<Ingredient> inputItems = new ArrayList<>();
    protected final List<FluidIngredient> inputFluids = new ArrayList<>();

    protected int energy;

    protected ThermalFuel(ResourceLocation recipeId, int energy, List<Ingredient> inputItems, List<FluidIngredient> inputFluids) {

        super(recipeId);

        if ((inputItems == null || inputItems.isEmpty()) && (inputFluids == null || inputFluids.isEmpty())) {
            ThermalCore.LOG.warn("Invalid Thermal Series fuel: " + recipeId + "\nRefer to the fuel's ResourceLocation to find the mod responsible and let them know!");
        }
        this.energy = energy;

        if (inputItems != null) {
            this.inputItems.addAll(inputItems);
        }
        if (inputFluids != null) {
            this.inputFluids.addAll(inputFluids);
        }
        trim();
    }

    private void trim() {

        ((ArrayList<Ingredient>) this.inputItems).trimToSize();
        ((ArrayList<FluidIngredient>) this.inputFluids).trimToSize();
    }

    // region GETTERS
    public List<Ingredient> getInputItems() {

        return inputItems;
    }

    public List<FluidIngredient> getInputFluids() {

        return inputFluids;
    }

    public int getEnergy() {

        return energy;
    }
    // endregion
}
