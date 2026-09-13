package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.common.block.BBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;

/**
 * 引火物楼梯。移植自 Better Badlands 的 {@code KindlingStairsBlock}。
 * <p>1.16.5 的 {@code StairsBlock.FACING/HALF/SHAPE/WATERLOGGED} 在 1.20.1 位于
 * {@link BlockStateProperties}。
 */
public class KindlingStairsBlock extends BBBlocks.ThatchStairBlock implements IKindling {

    public KindlingStairsBlock(BlockState baseState, Properties properties) {
        super(baseState, properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BURN_DISTANCE, 0)
                .setValue(IS_BURNED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, net.minecraft.core.Direction.NORTH)
                .setValue(BlockStateProperties.HALF, Half.BOTTOM)
                .setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT)
                .setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        this.onProjectileCollisionKindling(level, state, hit, projectile);
        super.onProjectileHit(level, state, hit, projectile);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        this.addKindlingProperties(builder)
                .add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF,
                        BlockStateProperties.STAIRS_SHAPE, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        this.animateTickKindling(state, level, pos, rand);
        super.animateTick(state, level, pos, rand);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        this.onBlockAddedKindling(state, level, pos, oldState, isMoving);
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.neighborChangedKindling(state, level, pos, block, fromPos, isMoving);
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        this.tickKindling(state, level, pos, rand);
        super.tick(state, level, pos, rand);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        return this.useKindling(state, level, pos, player, hand, hit);
    }
}
