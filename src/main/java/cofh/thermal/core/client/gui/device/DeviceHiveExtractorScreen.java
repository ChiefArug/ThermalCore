package cofh.thermal.core.client.gui.device;

import cofh.thermal.core.common.inventory.device.DeviceHiveExtractorMenu;
import cofh.thermal.lib.client.gui.AugmentableTileScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static cofh.core.util.helpers.GuiHelper.*;
import static cofh.lib.util.constants.ModIds.ID_THERMAL;

public class DeviceHiveExtractorScreen extends AugmentableTileScreen<DeviceHiveExtractorMenu> {

    public static final String TEX_PATH = ID_THERMAL + ":textures/gui/container/hive_extractor.png";
    public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

    public DeviceHiveExtractorScreen(DeviceHiveExtractorMenu container, Inventory inv, Component titleIn) {

        super(container, inv, container.tile, titleIn);
        texture = TEXTURE;
        info = generatePanelInfo("info.thermal.device_hive_extractor");
    }

    @Override
    public void init() {

        super.init();

        addElement(setClearable(createMediumFluidStorage(this, 116, 22, tile.getTank(0)), tile, 0));
    }

}
