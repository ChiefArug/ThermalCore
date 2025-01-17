package cofh.thermal.core.common.inventory.device;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.core.common.network.packet.server.ContainerConfigPacket;
import cofh.lib.common.inventory.SlotCoFH;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.core.common.block.entity.device.DeviceNullifierBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreMenus.DEVICE_NULLIFIER_CONTAINER;

public class DeviceNullifierMenu extends BlockEntityCoFHMenu {

    public final DeviceNullifierBlockEntity tile;

    public DeviceNullifierMenu(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(DEVICE_NULLIFIER_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (DeviceNullifierBlockEntity) world.getBlockEntity(pos);

        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new SlotCoFH(tileInv, i * 3 + j + 1, 44 + j * 18, 17 + i * 18));
            }
        }
        bindAugmentSlots(tileInv, 10, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

    public void emptyBin() {

        ContainerConfigPacket.sendToServer(this);
    }

    @Override
    protected int getMergeableSlotCount() {

        return baseTile.invSize() - 1;
    }

    // region NETWORK
    @Override
    public void handleConfigPacket(FriendlyByteBuf buffer) {

        tile.emptyBin();
    }
    // endregion
}
