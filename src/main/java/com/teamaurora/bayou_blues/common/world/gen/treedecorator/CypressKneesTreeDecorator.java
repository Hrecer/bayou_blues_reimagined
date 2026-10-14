package com.teamaurora.bayou_blues.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.block.CypressKneeBlock;
import com.teamaurora.bayou_blues.common.block.DoubleCypressKneeBlock;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class CypressKneesTreeDecorator extends TreeDecorator {
    public static final Codec<CypressKneesTreeDecorator> CODEC = Codec.unit(() -> CypressKneesTreeDecorator.INSTANCE);
    public static final CypressKneesTreeDecorator INSTANCE = new CypressKneesTreeDecorator();

    @Override
    protected TreeDecoratorType<?> type() {
        return BayouBluesFeatures.CYPRESS_KNEES.get();
    }

    @Override
    public void place(Context context) {
        LevelSimulatedReader level = context.level();
        RandomSource rand = context.random();
        List<BlockPos> logs = context.logs();
        if (logs.isEmpty()) return;

        // 膝根基准高度：默认树干最低一格；若树干触水则抬到"邻水最高的那格"（水线）。
        // 水里的丝柏因此会在水面高度围一圈膝根：大膝根下段没入水中、上段露出水面。
        int minY = Integer.MAX_VALUE;
        int maxWaterY = 0;
        for (BlockPos pos : logs) {
            if (pos.getY() < minY) {
                minY = pos.getY();
            }
            if (pos.getY() > maxWaterY) {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    if (isWater(level, pos.relative(dir))) {
                        maxWaterY = pos.getY();
                    }
                }
            }
        }
        if (maxWaterY > minY) {
            minY = maxWaterY;
        }

        for (BlockPos pos : logs) {
            if (pos.getY() != minY) continue;
            placeKneesAround(context, level, rand, pos);
        }
    }

    private void placeKneesAround(Context context, LevelSimulatedReader level, RandomSource rand, BlockPos pos) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos target = pos.offset(x, y, z);
                    if (pos.distSqr(target) >= 9.0D) continue;
                    if (isGrass(level, target.below()) && isAir(level, target)) {
                        if (rand.nextInt(9) == 0) {
                            if (rand.nextInt(3) == 0 && isAir(level, target.above())) {
                                placeLargeKnee(context, level, target);
                            } else {
                                context.setBlock(target, BayouBluesBlocks.CYPRESS_KNEE.get().defaultBlockState()
                                        .setValue(CypressKneeBlock.WATERLOGGED, false));
                            }
                        }
                    } else if (isWater(level, target) && isSolid(level, target.below())) {
                        if (rand.nextInt(8) == 0) {
                            if (rand.nextInt(4) != 0 && isAir(level, target.above())) {
                                placeLargeKnee(context, level, target);
                            } else {
                                context.setBlock(target, BayouBluesBlocks.CYPRESS_KNEE.get().defaultBlockState()
                                        .setValue(CypressKneeBlock.WATERLOGGED, true));
                            }
                        }
                    }
                }
            }
        }
    }

    private static void placeLargeKnee(Context context, LevelSimulatedReader level, BlockPos pos) {
        context.setBlock(pos, BayouBluesBlocks.LARGE_CYPRESS_KNEE.get().defaultBlockState()
                .setValue(DoubleCypressKneeBlock.HALF, DoubleBlockHalf.LOWER)
                .setValue(DoubleCypressKneeBlock.WATERLOGGED, isWater(level, pos)));
        context.setBlock(pos.above(), BayouBluesBlocks.LARGE_CYPRESS_KNEE.get().defaultBlockState()
                .setValue(DoubleCypressKneeBlock.HALF, DoubleBlockHalf.UPPER)
                .setValue(DoubleCypressKneeBlock.WATERLOGGED, isWater(level, pos.above())));
    }

    private static boolean isGrass(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.is(Blocks.GRASS_BLOCK));
    }

    private static boolean isAir(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, BlockState::isAir);
    }

    private static boolean isWater(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.getFluidState().is(FluidTags.WATER));
    }

    private static boolean isSolid(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, BlockState::isSolid);
    }
}
