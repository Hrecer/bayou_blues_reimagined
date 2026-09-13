package com.teamaurora.bayou_blues.common.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.util.TreeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 「小深色橡木」——恶地专属的矮小、弯折深色橡木。移植自 Better Badlands 的
 * {@code com.teamaurora.better_badlands.common.world.gen.feature.SmallDarkOakFeature}。
 *
 * <p>形态算法与原模组逐行一致：
 * <ol>
 *   <li>树高 = 3~5，分裂高度 = 2~(height-1)，随机水平方向。</li>
 *   <li>主干下段竖直，上段沿随机方向整体平移一格（形成「折」）。</li>
 *   <li>树冠是手写的叶盘：3×3×3 实心核 + 四向 ±2 伸出的臂 + 顶端与侧向散点。</li>
 *   <li>所有原点必须是空气/树叶，否则放弃生成；随后放置原木与树叶，
 *       最后按 Y 排序交给 {@link TreeDecorator}。</li>
 * </ol>
 *
 * <p><b>移植注记</b>（1.16.5 → 1.20.1）：
 * <ul>
 *   <li>{@code Feature.generate(ISeedReader, ChunkGenerator, Random, BlockPos, FC)}
 *       → {@link Feature#place(FeaturePlaceContext)}，随机源为 {@link RandomSource}。</li>
 *   <li>{@code BlockPos.betweenClosed(a, b)} 返回的可变对象在迭代中被复用，
 *       不能直接收进 List；这里改为显式坐标循环，语义完全等价。</li>
 *   <li>{@code MutableBoundingBox} 已从装饰器接口移除，改为
 *       {@link TreeDecorator.Context}。</li>
 *   <li>原模组用 AbnormalsCore {@code TreeUtil.isValidGround(level, pos, new SaplingBlock(...))}，
 *       即「能否种下深色橡树苗」；1.20.1 用 {@code DARK_OAK_SAPLING.canSurvive} 等价实现。</li>
 * </ul>
 */
public class SmallDarkOakFeature extends Feature<TreeConfiguration> {

    public SmallDarkOakFeature(Codec<TreeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        TreeConfiguration config = context.config();

        int height = rand.nextInt(3) + 3;                 // 3..5
        int splitHeight = rand.nextInt(height - 2) + 2;   // 2..height-1
        Direction direction = Direction.from2DDataValue(rand.nextInt(4));

        if (position.getY() < 1 || position.getY() + height + 2 > level.getMaxBuildHeight()) {
            return false;
        }
        if (!isValidGround(level, position.below())) {
            return false;
        }

        List<BlockPos> logs = new ArrayList<>();
        List<BlockPos> leaves = new ArrayList<>();

        // ── 下段：竖直 ──
        for (int dy = 0; dy <= splitHeight; dy++) {
            logs.add(position.above(dy));
        }
        // ── 上段：沿 direction 平移一格 ──
        for (int dy = splitHeight; dy <= height; dy++) {
            logs.add(position.relative(direction).above(dy));
        }
        BlockPos bendEnd = position.relative(direction).above(height);

        this.placeLeavesAt(leaves, bendEnd, direction, rand);
        List<BlockPos> leavesClean = this.cleanLeavesArray(leaves, logs);

        boolean canPlaceLeaf = false;

        // 全部原木目标格子必须是空气或树叶，否则放弃（保持原模组行为）
        for (BlockPos log : logs) {
            if (!TreeUtil.isAirOrLeaves(level, log)) {
                return false;
            }
        }

        // ── 树叶也必须做一次可行性检查 ──
        // 原模组只用「原木是否被占」来决定成败，树叶则是「能放就放」。这在 1.16.5 的
        // 恶地可行；但在 1.20.1 恶地是**实心台地地形**，树冠 3×3×3 核 + 伸臂很容易
        // 整片埋进陶瓦里 —— 木桩插进地里、树叶全被拒，看起来就是「种了一根光杆」。
        // 因此这里要求至少有一片树叶能真正落下，否则整体放弃，让生成器换个位置再试。
        for (BlockPos leaf : leavesClean) {
            if (TreeUtil.isAirOrLeaves(level, leaf)) {
                canPlaceLeaf = true;
                break;
            }
        }
        if (!canPlaceLeaf) {
            return false;
        }

        TreeUtil.setDirtAt(level, position.below());

        for (BlockPos log : logs) {
            TreeUtil.setForcedState(level, log, config.trunkProvider.getState(rand, log));
        }
        for (BlockPos leaf : leavesClean) {
            TreeUtil.placeLeafAt(level, leaf, rand, config);
        }

        this.placeDecorators(level, rand, config, logs, leavesClean);
        return true;
    }

    /**
     * 判断某格能否作为小深色橡木的落脚点。
     *
     * <p><b>为什么不能照抄原模组的「能否种下深色橡树苗」</b>：
     * 原模组调用 AbnormalsCore {@code TreeUtil.isValidGround(level, pos, new SaplingBlock(...))}，
     * 而 {@link Blocks#DARK_OAK_SAPLING} 的 {@code canSurvive} 只认
     * {@code BlockTags.DIRT}（草方块/泥土/砂土/灰化土/ rooted dirt/苔藓…）。
     * 但 1.20.1 的 {@code minecraft:wooded_badlands} <b>地表是粗泥（COARSE_DIRT）+ 陶瓦带</b>
     * （见 {@code net.minecraft.data.worldgen.SurfaceRuleData}：恶地分支只放
     * {@code TERRACOTTA} / {@code COARSE_DIRT}，从不放草方块或普通泥土）。
     * 粗泥虽然属于 {@code minecraft:dirt} 物品标签，却<b>不在 {@code BlockTags.DIRT} 方块标签里</b>，
     * 因此原判断在恶地恒为 false —— 树的每一次生成尝试都会在第一步被否掉，
     * 表现为「跑遍恶地一棵树都没有」。
     *
     * <p>这里改为直接判断「恶地真实地表方块」，这才是「小深色橡木长在恶地高原上」的本意。
     */
    private static boolean isValidGround(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(BlockTags.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.MYCELIUM)
                || state.is(BlockTags.TERRACOTTA)
                || state.is(Blocks.RED_SAND)
                || state.is(Blocks.RED_SANDSTONE)
                || state.is(Blocks.SAND)
                || state.is(Blocks.SANDSTONE)
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                // 兜底：问一次深色橡树苗（覆盖其它模组加的地表方块）
                || Blocks.DARK_OAK_SAPLING.defaultBlockState().canSurvive(level, pos);
    }

    /** 放置装饰器（树藤、苔藓等）。 */
    private void placeDecorators(WorldGenLevel level, RandomSource rand, TreeConfiguration config,
                                 List<BlockPos> logs, List<BlockPos> leavesClean) {
        if (config.decorators.isEmpty()) {
            return;
        }
        List<BlockPos> logsSorted = new ArrayList<>(logs);
        List<BlockPos> leavesSorted = new ArrayList<>(leavesClean);
        logsSorted.sort(Comparator.comparingInt(BlockPos::getY));
        leavesSorted.sort(Comparator.comparingInt(BlockPos::getY));
        Set<BlockPos> logsSet = new HashSet<>(logsSorted);
        Set<BlockPos> leavesSet = new HashSet<>(leavesSorted);
        Set<BlockPos> rootsSet = new HashSet<>();
        TreeDecorator.Context decoratorContext = new TreeDecorator.Context(
                level,
                (pos, state) -> level.setBlock(pos, state, 3),
                rand,
                logsSet,
                leavesSet,
                rootsSet);
        config.decorators.forEach(decorator -> decorator.place(decoratorContext));
    }

    /**
     * 树冠形状：位置 {@code pos} 处的 3×3×3 核 + 十字臂 + 顶端与侧向散点。
     * 与原模组 {@code placeLeavesAt} 逐条对应。
     */
    private void placeLeavesAt(List<BlockPos> leaf, BlockPos pos, Direction dir, RandomSource rand) {
        // 3x3x3 实心核：pos + (-1..1, -1..1, -1..1)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    leaf.add(pos.offset(dx, dy, dz));
                }
            }
        }
        // 四个方向的 ±2 水平臂（厚度 1，沿轴向 3 格）
        for (int dz = -1; dz <= 1; dz++) {
            leaf.add(pos.offset(-2, 0, dz));
            leaf.add(pos.offset(2, 0, dz));
        }
        for (int dx = -1; dx <= 1; dx++) {
            leaf.add(pos.offset(dx, 0, -2));
            leaf.add(pos.offset(dx, 0, 2));
        }
        // 斜角上抬
        leaf.add(pos.offset(-2, 1, 0));
        leaf.add(pos.offset(2, 1, 0));
        leaf.add(pos.offset(0, 1, -2));
        leaf.add(pos.offset(0, 1, 2));
        // 顶冠
        leaf.add(pos.above(2));
        leaf.add(pos.relative(dir, 3));
        this.placeRandomLeafAt(pos.above(2).relative(dir), rand, leaf);
        // 沿 dir 方向的前伸臂（随机）
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                this.placeRandomLeafAt(pos.offset(dx, 0, dz).relative(dir, 2), rand, leaf);
            }
        }
        this.placeRandomLeafAt(pos.below().relative(dir, -2), rand, leaf);
    }

    private void placeRandomLeafAt(BlockPos pos, RandomSource rand, List<BlockPos> leaf) {
        if (rand.nextBoolean()) {
            leaf.add(pos);
        }
    }

    private List<BlockPos> cleanLeavesArray(List<BlockPos> leaves, List<BlockPos> logs) {
        List<BlockPos> newLeaves = new ArrayList<>();
        for (BlockPos leaf : leaves) {
            if (!logs.contains(leaf)) {
                newLeaves.add(leaf);
            }
        }
        return newLeaves;
    }
}
