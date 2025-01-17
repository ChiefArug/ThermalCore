package cofh.thermal.core.init.data.providers;

import cofh.lib.init.data.BlockStateProviderCoFH;
import cofh.lib.util.DeferredRegisterCoFH;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static cofh.thermal.core.ThermalCore.BLOCKS;
import static cofh.thermal.lib.util.ThermalIDs.*;

public class TCoreBlockStateProvider extends BlockStateProviderCoFH {

    public TCoreBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {

        super(output, ID_THERMAL, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        var reg = BLOCKS;

        registerVanilla(reg);
        registerStorage(reg);
        registerBuildingBlocks(reg);
    }

    // region HELPERS
    private void registerVanilla(DeferredRegisterCoFH<Block> reg) {

        simpleBlock(reg.getSup(ID_CHARCOAL_BLOCK));
        simpleBlock(reg.getSup(ID_GUNPOWDER_BLOCK));
        // axisBlock(reg.getSup(ID_BAMBOO_BLOCK), "bamboo_block");
        axisBlock(reg.getSup(ID_SUGAR_CANE_BLOCK), "sugar_cane_block");

        // TODO: Missing - Food Crates
    }

    private void registerStorage(DeferredRegisterCoFH<Block> reg) {

        simpleBlock(reg.get(ID_APATITE_BLOCK));
        simpleBlock(reg.get(ID_CINNABAR_BLOCK));
        simpleBlock(reg.get(ID_NITER_BLOCK));
        simpleBlock(reg.get(ID_SULFUR_BLOCK));

        simpleBlock(reg.get(ID_SAWDUST_BLOCK));
        simpleBlock(reg.get(ID_COAL_COKE_BLOCK));
        simpleBlock(reg.get(ID_BITUMEN_BLOCK));
        // TODO: Add a top/bottom method to handle this easily.
        // storageBlock(reg.getSup(ID_TAR_BLOCK));
        // storageBlock(reg.getSup(ID_ROSIN_BLOCK));

        simpleBlock(reg.getSup(ID_RUBBER_BLOCK));
        simpleBlock(reg.getSup(ID_CURED_RUBBER_BLOCK));
        simpleBlock(reg.getSup(ID_SLAG_BLOCK));
        simpleBlock(reg.getSup(ID_RICH_SLAG_BLOCK));

        simpleBlock(reg.getSup(ID_ENDERIUM_BLOCK));
        simpleBlock(reg.getSup(ID_LUMIUM_BLOCK));
        simpleBlock(reg.getSup(ID_SIGNALUM_BLOCK));
    }

    private void registerBuildingBlocks(DeferredRegisterCoFH<Block> reg) {

        simpleBlock(reg.getSup(ID_POLISHED_SLAG));
        simpleBlock(reg.getSup(ID_CHISELED_SLAG));
        simpleBlock(reg.getSup(ID_SLAG_BRICKS));
        simpleBlock(reg.getSup(ID_CRACKED_SLAG_BRICKS));

        simpleBlock(reg.getSup(ID_POLISHED_RICH_SLAG));
        simpleBlock(reg.getSup(ID_CHISELED_RICH_SLAG));
        simpleBlock(reg.getSup(ID_RICH_SLAG_BRICKS));
        simpleBlock(reg.getSup(ID_CRACKED_RICH_SLAG_BRICKS));
    }
    // endregion
}
