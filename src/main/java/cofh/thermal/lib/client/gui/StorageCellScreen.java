package cofh.thermal.lib.client.gui;

import cofh.core.client.gui.element.panel.ConfigPanel;
import cofh.core.common.inventory.ContainerMenuCoFH;
import cofh.thermal.core.client.gui.ThermalGuiHelper;
import cofh.thermal.lib.common.block.entity.StorageCellBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class StorageCellScreen<T extends ContainerMenuCoFH> extends AugmentableTileScreen<T> {

    protected StorageCellBlockEntity tile;

    public StorageCellScreen(T container, Inventory inv, StorageCellBlockEntity tile, Component titleIn) {

        super(container, inv, tile, titleIn);
        this.tile = tile;
    }

    @Override
    public void init() {

        super.init();

        addPanel(new ConfigPanel(this, tile, tile, () -> tile.getFacing())
                .allowFacingConfig(true)
                .addConditionals(ThermalGuiHelper.createDefaultCellConfigs(this, name, tile)));
    }

}
