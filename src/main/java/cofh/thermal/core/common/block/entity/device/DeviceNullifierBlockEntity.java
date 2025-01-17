package cofh.thermal.core.common.block.entity.device;

import cofh.core.util.helpers.AugmentDataHelper;
import cofh.lib.common.fluid.FluidStorageCoFH;
import cofh.lib.common.fluid.NullFluidStorage;
import cofh.lib.common.inventory.ItemStorageCoFH;
import cofh.lib.common.inventory.NullItemStorage;
import cofh.thermal.core.common.config.ThermalCoreConfig;
import cofh.thermal.core.common.inventory.device.DeviceNullifierMenu;
import cofh.thermal.lib.common.block.entity.DeviceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static cofh.lib.api.StorageGroup.INPUT;
import static cofh.lib.api.StorageGroup.INTERNAL;
import static cofh.lib.util.Constants.BUCKET_VOLUME;
import static cofh.lib.util.constants.NBTTags.TAG_AUGMENT_TYPE_FILTER;
import static cofh.thermal.core.init.registries.TCoreBlockEntities.DEVICE_NULLIFIER_TILE;
import static cofh.thermal.lib.util.ThermalAugmentRules.createAllowValidator;

public class DeviceNullifierBlockEntity extends DeviceBlockEntity {

    public static final BiPredicate<ItemStack, List<ItemStack>> AUG_VALIDATOR = createAllowValidator(TAG_AUGMENT_TYPE_FILTER);

    protected ItemStorageCoFH nullSlot = new NullItemStorage(item -> filter.valid(item)).setEnabled(() -> isActive);
    protected ItemStorageCoFH[] binSlots;
    // protected ItemStorageCoFH tankSlot = new ItemStorageCoFH(1, (item) -> FluidHelper.hasFluidHandlerCap(item) || item.getItem() == Items.POTION);

    protected FluidStorageCoFH nullTank = new NullFluidStorage(BUCKET_VOLUME * 64, fluid -> filter.valid(fluid)).setEnabled(() -> isActive);

    public DeviceNullifierBlockEntity(BlockPos pos, BlockState state) {

        super(DEVICE_NULLIFIER_TILE.get(), pos, state);

        inventory.addSlot(nullSlot, INPUT);

        binSlots = new ItemStorageCoFH[9];
        for (int i = 0; i < binSlots.length; ++i) {
            binSlots[i] = new ItemStorageCoFH(item -> filter.valid(item));
            inventory.addSlot(binSlots[i], INTERNAL);
        }
        tankInv.addTank(nullTank, INPUT);

        addAugmentSlots(ThermalCoreConfig.deviceAugments);
        initHandlers();
    }

    public boolean binHasItems() {

        for (ItemStorageCoFH slot : binSlots) {
            if (!slot.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void emptyBin() {

        for (ItemStorageCoFH slot : binSlots) {
            slot.clear();
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {

        return new DeviceNullifierMenu(i, level, worldPosition, inventory, player);
    }

    // region AUGMENTS
    @Override
    protected Predicate<ItemStack> augValidator() {

        return item -> AugmentDataHelper.hasAugmentData(item) && AUG_VALIDATOR.test(item, getAugmentsAsList());
    }
    // endregion
}
