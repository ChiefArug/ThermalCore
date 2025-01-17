package cofh.thermal.core.client.gui.storage;

import cofh.core.client.gui.ContainerScreenCoFH;
import cofh.core.client.gui.element.ElementTexture;
import cofh.core.client.gui.element.panel.SecurityPanel;
import cofh.core.common.network.packet.server.FilterableGuiTogglePacket;
import cofh.core.util.filter.IFilterableItem;
import cofh.core.util.helpers.FilterHelper;
import cofh.core.util.helpers.RenderHelper;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.thermal.core.common.inventory.storage.SatchelMenu;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.Collections;

import static cofh.core.util.helpers.GuiHelper.*;
import static cofh.lib.util.Constants.PATH_ELEMENTS;
import static cofh.lib.util.Constants.PATH_GUI;

public class SatchelScreen extends ContainerScreenCoFH<SatchelMenu> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(PATH_GUI + "generic.png");
    public static final ResourceLocation TEXTURE_EXT = new ResourceLocation(PATH_GUI + "generic_extension.png");
    public static final ResourceLocation SLOT_OVERLAY = new ResourceLocation(PATH_ELEMENTS + "locked_overlay_slot.png");

    protected int renderExtension;

    public SatchelScreen(SatchelMenu container, Inventory inv, Component titleIn) {

        super(container, inv, titleIn);

        texture = TEXTURE;
        info = generatePanelInfo("info.thermal.satchel");

        renderExtension = container.getExtraRows() * 18;
        imageHeight += renderExtension;
    }

    @Override
    public void init() {

        super.init();

        for (int i = 0; i < menu.getContainerInventorySize(); ++i) {
            Slot slot = menu.slots.get(i);
            addElement(createSlot(this, slot.x, slot.y));
        }
        addPanel(new SecurityPanel(this, menu, SecurityHelper.getID(player)));

        // Filter Tab
        addElement(new ElementTexture(this, 4, -21)
                .setUV(24, 0)
                .setSize(24, 21)
                .setTexture(TAB_TOP, 48, 32)
                .setVisible(() -> FilterHelper.hasFilter(menu.getSatchel())));

        addElement(new ElementTexture(this, 8, -17) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

                FilterableGuiTogglePacket.openFilterGui(menu.getSatchel());
                return true;
            }
        }
                .setSize(16, 16)
                .setTexture(NAV_FILTER, 16, 16)
                .setTooltipFactory((element, mouseX, mouseY) -> ((IFilterableItem) menu.getSatchel().getItem()).getFilter(menu.getSatchel()) instanceof MenuProvider menuProvider ? Collections.singletonList(menuProvider.getDisplayName()) : Collections.emptyList())
                .setVisible(() -> FilterHelper.hasFilter(menu.getSatchel())));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {

        RenderHelper.resetShaderColor();
        RenderHelper.setPosTexShader();
        RenderHelper.setShaderTexture0(texture);

        PoseStack poseStack = guiGraphics.pose();

        drawTexturedModalRect(guiGraphics, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (renderExtension > 0) {
            RenderHelper.setShaderTexture0(TEXTURE_EXT);
            drawTexturedModalRect(guiGraphics, leftPos, topPos + renderExtension, 0, 0, imageWidth, imageHeight);
        }
        poseStack.pushPose();
        poseStack.translate(leftPos, topPos, 0.0F);

        drawPanels(guiGraphics, false);
        drawElements(guiGraphics, false);

        poseStack.popPose();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        super.renderLabels(guiGraphics, mouseX, mouseY);

        GlStateManager._enableBlend();
        RenderHelper.setPosTexShader();
        RenderHelper.setShaderTexture0(SLOT_OVERLAY);
        drawTexturedModalRect(guiGraphics, menu.lockedSlot.x, menu.lockedSlot.y, 0, 0, 16, 16, 16, 16);
        GlStateManager._disableBlend();
    }

}
