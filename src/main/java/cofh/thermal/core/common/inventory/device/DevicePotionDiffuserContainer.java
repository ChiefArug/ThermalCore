package cofh.thermal.core.common.inventory.device;

import cofh.core.common.inventory.TileCoFHContainer;
import cofh.lib.common.inventory.SlotCoFH;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.lib.common.block.entity.AugmentableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreContainers.DEVICE_POTION_DIFFUSER_CONTAINER;

public class DevicePotionDiffuserContainer extends TileCoFHContainer {

    public final AugmentableBlockEntity tile;

    public DevicePotionDiffuserContainer(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(DEVICE_POTION_DIFFUSER_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (AugmentableBlockEntity) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        addSlot(new SlotCoFH(tileInv, 0, 44, 35));

        bindAugmentSlots(tileInv, 1, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}