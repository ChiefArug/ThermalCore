package cofh.thermal.lib.common.item;

import cofh.core.common.item.IAugmentItem;
import cofh.core.common.item.ItemCoFH;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class AugmentItem extends ItemCoFH implements IAugmentItem {

    private CompoundTag augmentData;

    public AugmentItem(Properties builder, CompoundTag augmentData) {

        super(builder);
        setAugmentData(augmentData);
    }

    public boolean setAugmentData(CompoundTag augmentData) {

        if (augmentData == null || augmentData.isEmpty()) {
            return false;
        }
        this.augmentData = augmentData;
        return true;
    }

    // region IAugmentItem
    @Nullable
    @Override
    public CompoundTag getAugmentData(ItemStack augment) {

        return augmentData;
    }
    // endregion
}
