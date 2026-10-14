package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.common.block.thatch.ThatchVerticalSlabBlock;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class KindlingVerticalSlabBlock extends ThatchVerticalSlabBlock implements IKindling {
    public KindlingVerticalSlabBlock(BlockBehaviour.Properties properties) {
        super(BayouBluesBlocksBadlands.KINDLING, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(BURN_DISTANCE, 0).setValue(IS_BURNED, false));
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        this.onProjectileHitKindling(level, state, hit, projectile);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = this.useKindling(state, level, pos, player, hand, hit);
        return result.consumesAction() ? result : super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.neighborChangedKindling(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        this.onPlaceKindling(state, level, pos, oldState, isMoving);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.tickKindling(state, level, pos, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BURN_DISTANCE, IS_BURNED);
    }
}
