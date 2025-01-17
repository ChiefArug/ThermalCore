package cofh.thermal.core.common.entity.explosive;

import cofh.core.common.entity.AbstractTNTMinecart;
import cofh.lib.util.Utils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import static cofh.thermal.core.ThermalCore.ITEMS;

public class ThermalTNTMinecart extends AbstractTNTMinecart {

    protected Item item;
    protected Block block;
    protected IDetonateAction detonateAction;

    public ThermalTNTMinecart(EntityType<?> type, Level worldIn, IDetonateAction detonateAction, Block block) {

        super(type, worldIn);
        this.detonateAction = detonateAction;
        this.block = block;
        item = ITEMS.get(Utils.getRegistryName(type));
    }

    public ThermalTNTMinecart(EntityType<?> type, Level worldIn, IDetonateAction detonateAction, Block block, double posX, double posY, double posZ) {

        super(type, worldIn, posX, posY, posZ);
        this.detonateAction = detonateAction;
        this.block = block;
        item = ITEMS.get(Utils.getRegistryName(type));
    }

    @Override
    public Block getBlock() {

        return block;
    }

    @Override
    protected Item getDropItem() {

        return item;
    }

    @Override
    public ItemStack getPickResult() {

        return detonated ? new ItemStack(Items.MINECART) : new ItemStack(item);
    }

    @Override
    public void detonate(Vec3 pos) {

        detonateAction.detonate(level, this, null, pos, radius, effectDuration, effectAmplifier);
    }

}
