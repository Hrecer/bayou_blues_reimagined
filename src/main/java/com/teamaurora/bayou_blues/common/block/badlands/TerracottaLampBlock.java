package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;

/**
 * 陶瓦灯（Terracotta Lamp）。移植自 Better Badlands 的 {@code TerracottaLampBlock}。
 *
 * <p>六向朝向方块：放置时朝向玩家视线的反方向；正面（{@code facing} 指向的那一格）为空时
 * 自动补一块不可见的 {@link MagicInvisibleLightBlock} 作为光源。灯被破坏时会把这块
 * 辅助光源一起清掉。
 *
 * <p><b>移植注记</b>：1.16.5 的 {@code DirectionalBlock.FACING} 在 1.20.1 是
 * {@link BlockStateProperties#FACING}；{@code BlockState.get(prop)} → {@code getValue}；
 * {@code World.isAirBlock} → {@code Level.isEmptyBlock}。
 */
public class TerracottaLampBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public TerracottaLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /** 在第 6 面补充光源方块。 */
    private void tryPlaceLight(BlockState state, Level level, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        BlockPos lightPos = pos.relative(dir);
        if (level.isEmptyBlock(lightPos)) {
            level.setBlockAndUpdate(lightPos, BayouBluesBlocksBadlands.TERRACOTTA_LAMP_LIGHT_SOURCE.get().defaultBlockState());
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        this.tryPlaceLight(state, level, pos);
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.tryPlaceLight(state, level, pos);
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        Direction dir = state.getValue(FACING);
        BlockPos lightPos = pos.relative(dir);
        BlockState lightState = level.getBlockState(lightPos);
        if (lightState.getBlock() == BayouBluesBlocksBadlands.TERRACOTTA_LAMP_LIGHT_SOURCE.get()
                && !MagicInvisibleLightBlock.isValidPositionFromDir(lightState, level, lightPos, dir)) {
            level.setBlockAndUpdate(lightPos, Blocks.AIR.defaultBlockState());
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public static Direction getDir(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }
}
