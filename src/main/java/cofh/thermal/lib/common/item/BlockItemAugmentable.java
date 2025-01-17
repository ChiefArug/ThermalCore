package cofh.thermal.lib.common.item;

import cofh.core.common.item.BlockItemCoFH;
import cofh.core.common.item.IAugmentableItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.IntSupplier;

public class BlockItemAugmentable extends BlockItemCoFH implements IAugmentableItem {

    protected IntSupplier numSlots = () -> 0;
    protected BiPredicate<ItemStack, List<ItemStack>> augValidator = (e, f) -> true;

    public BlockItemAugmentable(Block blockIn, Properties builder) {

        super(blockIn, builder);
    }

    public BlockItemAugmentable setNumSlots(IntSupplier numSlots) {

        this.numSlots = numSlots;
        return this;
    }

    public BlockItemAugmentable setAugValidator(BiPredicate<ItemStack, List<ItemStack>> augValidator) {

        this.augValidator = augValidator;
        return this;
    }

    // region IAugmentableItem
    @Override
    public int getAugmentSlots(ItemStack augmentable) {

        return numSlots.getAsInt();
    }

    @Override
    public boolean validAugment(ItemStack augmentable, ItemStack augment, List<ItemStack> augments) {

        return augValidator.test(augment, augments);
    }

    @Override
    public void updateAugmentState(ItemStack augmentable, List<ItemStack> augments) {

    }
    // endregion
}
