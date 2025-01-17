package cofh.thermal.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static cofh.core.init.CoreMobEffects.ENDERFERENCE;
import static cofh.lib.util.Constants.FULL_CUBE_COLLISION;

public class EnderiumGlassBlock extends HardenedGlassBlock {

    protected static int duration = 100;

    public EnderiumGlassBlock(Properties properties) {

        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return FULL_CUBE_COLLISION;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {

        if (entityIn instanceof LivingEntity) {
            ((LivingEntity) entityIn).addEffect(new MobEffectInstance(ENDERFERENCE.get(), duration, 0, false, false));
        }
    }

}
