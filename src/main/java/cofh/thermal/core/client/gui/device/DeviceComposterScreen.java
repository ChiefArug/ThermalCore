package cofh.thermal.core.client.gui.device;

import cofh.thermal.core.client.gui.ThermalGuiHelper;
import cofh.thermal.core.common.inventory.device.DeviceComposterMenu;
import cofh.thermal.lib.client.gui.AugmentableTileScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static cofh.core.util.helpers.GuiHelper.PROG_ARROW_RIGHT;
import static cofh.core.util.helpers.GuiHelper.generatePanelInfo;
import static cofh.lib.util.constants.ModIds.ID_THERMAL;

public class DeviceComposterScreen extends AugmentableTileScreen<DeviceComposterMenu> {

    public static final String TEX_PATH = ID_THERMAL + ":textures/gui/container/composter.png";
    public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

    public DeviceComposterScreen(DeviceComposterMenu container, Inventory inv, Component titleIn) {

        super(container, inv, container.tile, titleIn);
        texture = TEXTURE;
        info = generatePanelInfo("info.thermal.device_composter");
    }

    @Override
    public void init() {

        super.init();

        addElement(ThermalGuiHelper.createDefaultProgress(this, 87, 35, PROG_ARROW_RIGHT, tile));
    }

}
