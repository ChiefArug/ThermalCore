package cofh.thermal.core.common.block.entity;

import cofh.core.util.helpers.AugmentDataHelper;
import cofh.lib.api.block.entity.ITickableTile;
import cofh.lib.common.energy.EnergyStorageCoFH;
import cofh.lib.common.inventory.ItemStorageCoFH;
import cofh.thermal.core.common.inventory.ChargeBenchMenu;
import cofh.thermal.lib.common.block.entity.AugmentableBlockEntity;
import cofh.thermal.lib.util.ThermalEnergyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static cofh.lib.api.StorageGroup.ACCESSIBLE;
import static cofh.lib.api.StorageGroup.INTERNAL;
import static cofh.lib.util.constants.NBTTags.*;
import static cofh.thermal.core.common.config.ThermalCoreConfig.storageAugments;
import static cofh.thermal.core.init.registries.TCoreBlockEntities.CHARGE_BENCH_TILE;
import static cofh.thermal.lib.util.ThermalAugmentRules.createAllowValidator;

public class ChargeBenchBlockEntity extends AugmentableBlockEntity implements ITickableTile.IServerTickable {

    public static final BiPredicate<ItemStack, List<ItemStack>> AUG_VALIDATOR = createAllowValidator(TAG_AUGMENT_TYPE_UPGRADE, TAG_AUGMENT_TYPE_RF, TAG_AUGMENT_TYPE_FILTER);

    public static final int BASE_CAPACITY = 500000;
    public static final int BASE_XFER = 4000;

    protected ItemStorageCoFH[] benchSlots = new ItemStorageCoFH[9];
    protected ItemStorageCoFH chargeSlot = new ItemStorageCoFH(1, ThermalEnergyHelper::hasEnergyHandlerCap);

    public ChargeBenchBlockEntity(BlockPos pos, BlockState state) {

        super(CHARGE_BENCH_TILE.get(), pos, state);

        energyStorage = new EnergyStorageCoFH(BASE_CAPACITY, BASE_XFER);

        for (int i = 0; i < benchSlots.length; ++i) {
            benchSlots[i] = new ItemStorageCoFH(1, (item -> filter.valid(item) && ThermalEnergyHelper.hasEnergyHandlerCap(item)));
            inventory.addSlot(benchSlots[i], ACCESSIBLE);
        }
        inventory.addSlot(chargeSlot, INTERNAL);

        addAugmentSlots(storageAugments);
        initHandlers();
    }

    @Override
    public void tickServer() {

        boolean curActive = isActive;
        isActive = false;
        if (redstoneControl().getState()) {
            chargeItems();
        }
        updateActiveState(curActive);
        chargeEnergy();
    }

    protected void chargeEnergy() {

        if (!chargeSlot.isEmpty()) {
            int maxTransfer = Math.min(energyStorage.getMaxReceive(), energyStorage.getSpace());
            chargeSlot.getItemStack().getCapability(ThermalEnergyHelper.getBaseEnergySystem(), null).ifPresent(c -> energyStorage.receiveEnergy(c.extractEnergy(maxTransfer, false), false));
        }
    }

    protected void chargeItems() {

        for (ItemStorageCoFH benchSlot : benchSlots) {
            LazyOptional<? extends IEnergyStorage> lazyOpt = benchSlot.getItemStack().getCapability(ThermalEnergyHelper.getBaseEnergySystem(), null);
            lazyOpt.ifPresent(c -> {
                if (c.getEnergyStored() < c.getMaxEnergyStored()) {
                    isActive = true;
                    if (!energyStorage.isEmpty()) {
                        int maxTransfer = Math.min(energyStorage.getMaxExtract(), energyStorage.getEnergyStored());
                        energyStorage.extractEnergy(c.receiveEnergy(maxTransfer, false), false);
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {

        return new ChargeBenchMenu(i, level, worldPosition, inventory, player);
    }

    // region AUGMENTS
    @Override
    protected Predicate<ItemStack> augValidator() {

        return item -> AugmentDataHelper.hasAugmentData(item) && AUG_VALIDATOR.test(item, getAugmentsAsList());
    }
    // endregion
}
