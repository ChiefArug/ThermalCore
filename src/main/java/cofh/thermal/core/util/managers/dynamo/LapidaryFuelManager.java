package cofh.thermal.core.util.managers.dynamo;

import cofh.thermal.lib.util.managers.SingleItemFuelManager;
import cofh.thermal.lib.util.recipes.internal.IDynamoFuel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import static cofh.thermal.core.init.registries.TCoreRecipeTypes.LAPIDARY_FUEL;

public class LapidaryFuelManager extends SingleItemFuelManager {

    private static final LapidaryFuelManager INSTANCE = new LapidaryFuelManager();
    protected static final int DEFAULT_ENERGY = 16000;

    public static LapidaryFuelManager instance() {

        return INSTANCE;
    }

    protected LapidaryFuelManager() {

        super(DEFAULT_ENERGY);
    }

    public int getEnergy(ItemStack stack) {

        IDynamoFuel fuel = getFuel(stack);
        return fuel != null ? fuel.getEnergy() : 0;
    }

    // region IManager
    @Override
    public void refresh(RecipeManager recipeManager) {

        clear();
        var recipes = recipeManager.byType(LAPIDARY_FUEL.get());
        for (var entry : recipes.entrySet()) {
            addFuel(entry.getValue());
        }
    }
    // endregion
}
