package cofh.thermal.lib.client.gui;

import cofh.core.client.gui.ContainerScreenCoFH;
import cofh.core.client.gui.element.ElementTexture;
import cofh.core.client.gui.element.ElementXpStorage;
import cofh.core.client.gui.element.panel.AugmentPanel;
import cofh.core.client.gui.element.panel.RSControlPanel;
import cofh.core.client.gui.element.panel.SecurityPanel;
import cofh.core.common.inventory.ContainerMenuCoFH;
import cofh.core.common.network.packet.server.FilterableGuiTogglePacket;
import cofh.core.util.helpers.FilterHelper;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.thermal.lib.common.block.entity.AugmentableBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;

import static cofh.core.util.helpers.GuiHelper.*;

public class AugmentableTileScreen<T extends ContainerMenuCoFH> extends ContainerScreenCoFH<T> {

    protected AugmentableBlockEntity tile;

    public AugmentableTileScreen(T container, Inventory inv, AugmentableBlockEntity tile, Component titleIn) {

        super(container, inv, titleIn);
        this.tile = tile;
    }

    @Override
    public void init() {

        super.init();

        // TODO: Enchantment Panel
        // addPanel(new PanelEnchantment(this, "This block can be enchanted."));
        addPanel(new SecurityPanel(this, tile, SecurityHelper.getID(player)));

        if (menu.getAugmentSlots().size() > 0) {
            addPanel(new AugmentPanel(this, menu::getNumAugmentSlots, menu.getAugmentSlots()));
        }
        addPanel(new RSControlPanel(this, tile));

        if (tile.getXpStorage() != null) {
            addElement(setClaimable((ElementXpStorage) createDefaultXpStorage(this, 152, 65, tile.getXpStorage()).setVisible(() -> tile.getXpStorage().getMaxXpStored() > 0), tile));
        }

        // Filter Tab
        addElement(new ElementTexture(this, 4, -21)
                .setUV(24, 0)
                .setSize(24, 21)
                .setTexture(TAB_TOP, 48, 32)
                .setVisible(() -> FilterHelper.hasFilter((BlockEntity) tile)));

        addElement(new ElementTexture(this, 8, -17) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

                FilterableGuiTogglePacket.openFilterGui(tile);
                return true;
            }
        }
                .setSize(16, 16)
                .setTexture(NAV_FILTER, 16, 16)
                .setTooltipFactory((element, mouseX, mouseY) -> tile.getFilter() instanceof MenuProvider menuProvider ? Collections.singletonList(menuProvider.getDisplayName()) : Collections.emptyList())
                .setVisible(() -> FilterHelper.hasFilter((BlockEntity) tile)));
    }

}
