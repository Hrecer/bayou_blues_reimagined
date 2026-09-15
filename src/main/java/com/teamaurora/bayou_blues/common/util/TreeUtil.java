package com.teamaurora.bayou_blues.common.util;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import net.minecraft.util.RandomSource;

/**
 * @author JustinPlayzz
 * @author Steven
 * @author ebo2022
 */
public final class TreeUtil {
    public static void placeDirectionalLogAt(LevelWriter level, BlockPos pos, Direction direction, RandomSource rand, TreeConfiguration config) {
        setForcedState(level, pos, config.trunkProvider.getState(rand, pos).setValue(RotatedPillarBlock.AXIS, direction.getAxis()));
    }

    public static void placeLeafAt(LevelSimulatedRW world, BlockPos pos, RandomSource rand, TreeConfiguration config) {
        if (isAirOrLeaves(world, pos)) {
            setForcedState(world, pos, config.foliageProvider.getState(rand, pos).setValue(LeavesBlock.DISTANCE, 1));
        }
    }

    public static void setForcedState(LevelWriter world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 18);
    }

    public static boolean isAir(LevelSimulatedReader level, BlockPos pos) {
        if (!(level instanceof BlockGetter)) {
            return level.isStateAtPosition(pos, BlockState::isAir);
        } else {
            return level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir);
        }
    }

    public static boolean isAirOrLeaves(LevelSimulatedReader level, BlockPos pos) {
        if (level instanceof LevelReader) {
            return level.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.LEAVES));
        }
         return level.isStateAtPosition(pos, (state) -> isAir(level, pos) || state.is(BlockTags.LEAVES));
    }

    /**
     * 树干放置校验用的宽容判定：除空气/树叶外，还接受【本模组的柏树原木】。
     *
     * <h2>为什么需要它（截断树复盘，2026-09-15）</h2>
     * 区块装饰（features 阶段）是逐方块 setBlock 的长过程，随时可能被中断
     * （玩家在生成完成前退出游戏、服务端崩溃等）。中断时区块带着已写入的部分
     * 树方块按当前状态（carvers 等被保存），下次进入世界时装饰阶段重跑：
     * <ul>
     *   <li>树干放置本身是强制覆盖（setForcedState），残留原木会被重新写入；</li>
     *   <li>但【校验】用的是 isAirOrLeaves——残留的柏树原木既不是空气也不是树叶，
     *       直接判定位置被占用 ⇒ 整棵树放弃放置 ⇒ 半截树永久固化在已完成区块里。</li>
     * </ul>
     * 把本模组的柏树原木视为可通过后，二次装饰会完整重放同一棵树（随机序列按
     * 区块位置播种，基本可复现），截断树被补全；外来方块（石头、结构、其他模组
     * 的方块）依旧照旧放弃，不影响防重叠设计。树叶无需处理：placeLeafAt 本就
     * 允许覆盖树叶，且游离的残留树叶会因距离衰减自然消失。
     */
    public static boolean isValidTrunkPosition(LevelSimulatedReader level, BlockPos pos) {
        if (level instanceof LevelReader) {
            return level.isStateAtPosition(pos, state ->
                    state.isAir() || state.is(BlockTags.LEAVES) || state.is(BayouBluesBlocks.CYPRESS_LOG.get()));
        }
        return level.isStateAtPosition(pos, (state) -> isAir(level, pos) || state.is(BlockTags.LEAVES));
    }

    public static void setDirtAt(LevelAccessor level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        if (block == Blocks.GRASS_BLOCK || block == Blocks.FARMLAND) {
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 18);
        }
    }

    public static boolean isValidGround(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.PODZOL) || state.is(Blocks.FARMLAND);
    }

}
