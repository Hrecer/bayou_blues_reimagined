package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import com.teamaurora.bayou_blues.core.registry.BayouBluesEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.Nullable;

/**
 * 萨瓜罗仙人掌的"小枝"（六向连接块，原版是 SixWayBlock，1.17+ 更名为 {@link PipeBlock}）。
 * 从主体侧向伸出，可连接沙子/主体/小花；触碰同样造成伤害与"衰亡"。
 */
public class SmallSaguaroCactusBlock extends PipeBlock implements IPlantable {
    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public SmallSaguaroCactusBlock(Properties properties) {
        super(0.25F, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(EAST, false).setValue(WEST, false)
                .setValue(UP, false).setValue(DOWN, false));
    }

    public boolean canConnect(Block block) {
        return block.defaultBlockState().is(BlockTags.SAND)
                || block == this
                || block == BayouBluesBlocksBadlands.SAGUARO_CACTUS.get()
                || block == BayouBluesBlocksBadlands.SAGUARO_FLOWER.get();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState connected = this.defaultBlockState();
        Direction placedOnDir = context.getClickedFace().getOpposite();
        BlockState faceState = context.getLevel().getBlockState(context.getClickedPos().relative(placedOnDir));
        return canConnect(faceState.getBlock()) ? connected.setValue(PROPERTY_BY_DIRECTION.get(placedOnDir), true) : null;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor levelIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState newState = (stateIn.getValue(PROPERTY_BY_DIRECTION.get(facing)) && !this.canConnect(facingState.getBlock()))
                ? stateIn
                : stateIn.setValue(PROPERTY_BY_DIRECTION.get(facing), this.canConnect(facingState.getBlock()));
        if (!newState.canSurvive(levelIn, currentPos)) {
            levelIn.scheduleTick(currentPos, this, 1);
        }
        return newState;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (dir != Direction.UP && state.getValue(PROPERTY_BY_DIRECTION.get(dir))) {
                if (canConnect(level.getBlockState(pos.relative(dir)).getBlock())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        entityIn.hurt(level.damageSources().cactus(), 1.0F);
        if (entityIn instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(BayouBluesEffects.SUCCUMBING.get(), 1200, 0, false, true, true));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_OTHER;
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public BlockState getPlant(BlockGetter level, BlockPos pos) {
        return this.defaultBlockState();
    }
}
