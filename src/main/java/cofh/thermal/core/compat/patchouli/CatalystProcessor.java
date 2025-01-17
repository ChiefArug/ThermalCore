package cofh.thermal.core.compat.patchouli;

import cofh.lib.util.Utils;
import cofh.thermal.core.util.managers.machine.InsolatorRecipeManager;
import cofh.thermal.core.util.managers.machine.PulverizerRecipeManager;
import cofh.thermal.core.util.managers.machine.SmelterRecipeManager;
import cofh.thermal.lib.util.managers.CatalyzedRecipeManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.helpers.StringHelper.localize;

public class CatalystProcessor implements IComponentProcessor {

    private final ArrayList<CatalyzedRecipeManager> managers = new ArrayList<>();

    @Override
    public void setup(Level level, IVariableProvider variables) {

        managers.add(SmelterRecipeManager.instance());
        managers.add(PulverizerRecipeManager.instance());
        managers.add(InsolatorRecipeManager.instance());
    }

    @Override
    public IVariable process(Level level, String key) {

        if (managers.size() == 0)
            return null;
        String[] keys = key.split("_");
        int machineIndex = ((int) keys[0].charAt(0)) - 65;

        if (machineIndex < managers.size()) {
            if (keys.length == 2) {
                switch (keys[1]) {
                    case "machine":
                        if (managers.get(machineIndex) instanceof SmelterRecipeManager)
                            return IVariable.wrap(localize("block.thermal.machine_smelter"));
                        if (managers.get(machineIndex) instanceof PulverizerRecipeManager)
                            return IVariable.wrap(localize("block.thermal.machine_pulverizer"));
                        if (managers.get(machineIndex) instanceof InsolatorRecipeManager)
                            return IVariable.wrap(localize("block.thermal.machine_insolator"));
                    case "enablejei":
                        return IVariable.wrap(managers.get(machineIndex).getCatalysts().size() > 6);
                    default:
                        return null;
                }
            } else {
                CatalyzedRecipeManager manager = managers.get(machineIndex);
                List<ItemStack> catalysts = manager.getCatalysts();
                int catalystIndex = Integer.parseInt(keys[1]);

                if (catalystIndex < catalysts.size() && (catalystIndex < 5 || (catalystIndex == 5 && catalysts.size() < 7))) {
                    ItemStack catalyst = catalysts.get(catalystIndex);
                    switch (keys[2]) {
                        case "enable":
                            return IVariable.wrap(true);
                        case "item":
                            return IVariable.wrap(Utils.getRegistryName(catalyst.getItem()).toString());
                        case "p":
                            return IVariable.wrap(manager.getCatalyst(catalyst).getPrimaryMod());
                        case "s":
                            return IVariable.wrap(manager.getCatalyst(catalyst).getSecondaryMod());
                        case "e":
                            return IVariable.wrap(manager.getCatalyst(catalyst).getEnergyMod());
                        case "u":
                            return IVariable.wrap((int) (manager.getCatalyst(catalyst).getUseChance() * 100));
                        default:
                            return null;
                    }
                }
            }
        }
        return null;
    }

}
