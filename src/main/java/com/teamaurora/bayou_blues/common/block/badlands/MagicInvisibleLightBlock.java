package com.teamaurora.bayou_blues.common.block.badlands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 不可见光源方块。移植自 Better Badlands 的 {@code MagicInvisibleLightBlock}。
 *
 * <p>由 {@link TerracottaLampBlock} 在自身正面自动放置，提供光照但不参与渲染/碰撞/寻路，
 * 也没有掉落物。当支撑它的陶瓦灯消失或转向后，自身会立即消失。
 *
 * <p><b>移植注记</b>：
 * <ul>
 *   <li>1.16.5 的 {@code func_200123_i}({@code skipRendering}) 在 1.20.1 已被
 *       {@code BlockBehaviour.canOcclude} 替代，而后者是<b>静态</b>读取
 *       {@code Properties} 的，不能 override。原意「不可见方块不遮挡邻面」在本类中
 *       通过 {@code Properties.noOcclusion()} 表达（见注册处）。</li>
 *   <li>1.16.5 的 {@code func_220076_a}({@code getDrops}) 返回空表；1.20.1 改为在
 *       {@code Properties} 上声明 {@code noLootTable()}，见注册处。</li>
 * </ul>
 */
public class MagicInvisibleLightBlock extends Block {

    public MagicInvisibleLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    /**
     * <b>本类最关键的一处覆写</b>：让本方块在<b>射线检测和选中轮廓</b>层面完全「不存在」。
     *
     * <p>只把 {@link #getVisualShape} / {@link #getInteractionShape} 置空是不够的：
     * 玩家准星指向的方块由 {@code BlockGetter#clip} → {@code BlockState#getShape} 决定，
     * 而后者的默认实现回退到<b>碰撞箱</b>。本方块碰撞箱是满格
     * （{@code Properties} 未声明 {@code noCollission}），于是会出现：
     * <ul>
     *   <li>准星明明对着空气，却选中了这个不可见方块，画出满格轮廓线；</li>
     *   <li>挖掘事件全部作用在它身上；它 {@code instabreak} 且无掉落，
     *       玩家就表现为「挖掘进度卡住/一闪一闪，下面那块方块永远挖不掉」。</li>
     * </ul>
     * 返回 {@link Shapes#empty()} 后，射线直接穿过本方块命中它后面的真实方块，
     * 既保留照明，又完全不干扰交互。
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return type == PathComputationType.AIR && !state.isAir() || super.isPathfindable(state, level, pos, type);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
                                  net.minecraft.world.level.LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return !state.canSurvive(level, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, false);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, false);
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public void tick(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource rand) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    /**
     * 只有当「正对」本格的那盏灯存在时才算合法。
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean valid = false;
        for (Direction dir : Direction.values()) {
            BlockState neighbour = level.getBlockState(pos.relative(dir));
            if (!(neighbour.getBlock() instanceof TerracottaLampBlock)) {
                continue;
            }
            Direction lampDir = neighbour.getValue(TerracottaLampBlock.FACING);
            if (dir == lampDir.getOpposite()) {
                valid = true;
            }
        }
        return valid;
    }

    /**
     * 陶瓦灯被移除时用：判断本光源除了「来自 {@code dirp} 方向的那盏灯」之外，
     * 是否还有别的灯在支撑它。
     */
    public static boolean isValidPositionFromDir(BlockState state, LevelReader level, BlockPos pos, Direction dirp) {
        boolean valid = false;
        for (Direction dir : Direction.values()) {
            if (dir == dirp.getOpposite()) {
                continue;
            }
            BlockState neighbour = level.getBlockState(pos.relative(dir));
            if (!(neighbour.getBlock() instanceof TerracottaLampBlock)) {
                continue;
            }
            Direction lampDir = neighbour.getValue(TerracottaLampBlock.FACING);
            if (dir == lampDir.getOpposite()) {
                valid = true;
            }
        }
        return valid;
    }
}
