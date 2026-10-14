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
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;

/**
 * 引火物台阶。移植自 Better Badlands 的 {@code KindlingSlabBlock}。
 * <p>1.16.5 的 {@code SlabBlock.TYPE/WATERLOGGED} 在 1.20.1 位于
 * {@link BlockStateProperties}；{@code SlabType} 仍为 {@code SlabType}。
 */
public class KindlingSlabBlock extends BBBlocks.ThatchSlabBlock implements IKindling {

    public KindlingSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BURN_DISTANCE, 0)
                .setValue(IS_BURNED, false)
                .setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
                .setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        this.onProjectileHitKindling(level, state, hit, projectile);
        super.onProjectileHit(level, state, hit, projectile);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        this.animateTickKindling(state, level, pos, rand);
        super.animateTick(state, level, pos, rand);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BURN_DISTANCE, IS_BURNED);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        this.onPlaceKindling(state, level, pos, oldState, isMoving);
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
