package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.common.block.thatch.ThatchVerticalSlabBlock;
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
import net.minecraft.world.phys.BlockHitResult;

/**
 * 引火物竖直半砖。移植自 Better Badlands 的 {@code KindlingVerticalSlabBlock}。
 *
 * <p><b>联动注记</b>：原模组通过 AbnormalsCore 的 {@code createCompatBlock("quark", ...)}
 * 只在 Quark 的 vertical_slabs 模块开启时注册该方块。在本项目中 Quark 是强制依赖，
 * 且本模组自己有 {@link ThatchVerticalSlabBlock} 竖砖基类（见
 * 青苔茅草竖直半砖 {@code algae_thatch_vertical_slab}），因此直接继承它，
 * 与原模组走 AbnormalsCore {@code ThatchVerticalSlabBlock} 的做法一致。
 */
public class KindlingVerticalSlabBlock extends ThatchVerticalSlabBlock implements IKindling {

    public KindlingVerticalSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BURN_DISTANCE, 0)
                .setValue(IS_BURNED, false));
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
        // 父类会写入 FACING / WATERLOGGED，这里追加引火物属性
        super.createBlockStateDefinition(this.addKindlingProperties(builder));
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
