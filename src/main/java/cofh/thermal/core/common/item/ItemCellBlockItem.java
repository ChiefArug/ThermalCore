package cofh.thermal.core.common.item;

import cofh.thermal.lib.common.item.BlockItemAugmentable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import static cofh.core.util.helpers.AugmentableHelper.setAttributeFromAugmentMax;
import static cofh.lib.util.constants.NBTTags.*;

public class ItemCellBlockItem extends BlockItemAugmentable {

    public ItemCellBlockItem(Block blockIn, Properties builder) {

        super(blockIn, builder);

        setEnchantability(5);
    }

    //    @Override
    //    protected void tooltipDelegate(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    //
    //        boolean creative = isCreative(stack, ITEM);
    //    }

    protected void setAttributesFromAugment(ItemStack container, CompoundTag augmentData) {

        CompoundTag subTag = container.getTagElement(TAG_PROPERTIES);
        if (subTag == null) {
            return;
        }
        setAttributeFromAugmentMax(subTag, augmentData, TAG_AUGMENT_BASE_MOD);
        setAttributeFromAugmentMax(subTag, augmentData, TAG_AUGMENT_ITEM_STORAGE);
        setAttributeFromAugmentMax(subTag, augmentData, TAG_AUGMENT_ITEM_CREATIVE);
    }

    //    @Override
    //    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    //
    //        return new FluidContainerItemWrapper(stack, this);
    //    }

    //    // region IFluidContainerItem
    //    @Override
    //    public CompoundTag getOrCreateTankTag(ItemStack container) {
    //
    //        CompoundTag blockTag = container.getOrCreateChildTag(TAG_BLOCK_ENTITY);
    //        ListNBT tanks = blockTag.getList(TAG_TANK_INV, TAG_COMPOUND);
    //        if (tanks.isEmpty()) {
    //            CompoundTag tag = new CompoundTag();
    //            tag.putByte(TAG_TANK, (byte) 0);
    //            new FluidStorageCoFH(FluidCellTile.BASE_CAPACITY).write(tag);
    //            tanks.add(tag);
    //            blockTag.put(TAG_TANK_INV, tanks);
    //        }
    //        return tanks.getCompound(0);
    //    }
    //
    //    @Override
    //    public FluidStack getFluid(ItemStack container) {
    //
    //        CompoundTag tag = getOrCreateTankTag(container);
    //        return FluidStack.loadFluidStackFromNBT(tag);
    //    }
    //
    //    @Override
    //    public int getCapacity(ItemStack container) {
    //
    //        CompoundTag tag = getOrCreateTankTag(container);
    //        if (tag == null) {
    //            return 0;
    //        }
    //        float base = getPropertyWithDefault(container, TAG_AUGMENT_BASE_MOD, 1.0F);
    //        float mod = getPropertyWithDefault(container, TAG_AUGMENT_FLUID_STORAGE, 1.0F);
    //        return getMaxStored(container, Math.round(tag.getInt(TAG_CAPACITY) * mod * base));
    //    }
    //
    //    @Override
    //    public int fill(ItemStack container, FluidStack resource, FluidAction action) {
    //
    //        CompoundTag containerTag = getOrCreateTankTag(container);
    //        if (resource.isEmpty() || !isFluidValid(container, resource)) {
    //            return 0;
    //        }
    //        FluidStorageCoFH tank = new FluidStorageCoFH(FluidCellTile.BASE_CAPACITY).setCapacity(getCapacity(container)).read(containerTag);
    //        if (isCreative(container, FLUID)) {
    //            if (action.execute()) {
    //                tank.setFluidStack(new FluidStack(resource, tank.getCapacity()));
    //                tank.write(containerTag);
    //            }
    //            return resource.getAmount();
    //        }
    //        int ret = tank.fill(resource, action);
    //        tank.write(containerTag);
    //        return ret;
    //    }
    //
    //    @Override
    //    public FluidStack drain(ItemStack container, int maxDrain, FluidAction action) {
    //
    //        CompoundTag containerTag = getOrCreateTankTag(container);
    //        FluidStorageCoFH tank = new FluidStorageCoFH(FluidCellTile.BASE_CAPACITY).setCapacity(getCapacity(container)).read(containerTag);
    //        if (isCreative(container, FLUID)) {
    //            return new FluidStack(tank.getFluidStack(), maxDrain);
    //        }
    //        FluidStack ret = tank.drain(maxDrain, action);
    //        tank.write(containerTag);
    //        return ret;
    //    }
    //    // endregion
    //
    //    // region IAugmentableItem
    //    @Override
    //    public void updateAugmentState(ItemStack container, List<ItemStack> augments) {
    //
    //        container.getOrCreateTag().put(TAG_PROPERTIES, new CompoundTag());
    //        for (ItemStack augment : augments) {
    //            CompoundTag augmentData = AugmentDataHelper.getAugmentData(augment);
    //            if (augmentData == null) {
    //                continue;
    //            }
    //            setAttributesFromAugment(container, augmentData);
    //        }
    //        int fluidExcess = getFluidAmount(container) - getCapacity(container);
    //        if (fluidExcess > 0) {
    //            drain(container, fluidExcess, EXECUTE);
    //        }
    //    }
    //    // endregion
}
