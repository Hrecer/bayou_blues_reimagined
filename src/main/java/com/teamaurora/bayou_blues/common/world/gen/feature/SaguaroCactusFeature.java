package com.teamaurora.bayou_blues.common.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.util.TreeUtil;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

/**
 * 萨瓜罗仙人掌生成。移植自 Better Badlands 的 {@code SaguaroCactusFeature}（1.16.5）：
 * 主柱 4~6 高、顶端一花；一到两条侧枝（小枝块）在中段伸出，枝顶随机开花。
 * 只长在沙子上；每个原木位必须是空气/树叶。配置（TreeConfiguration）仅供加载占位。
 */
public class SaguaroCactusFeature extends Feature<TreeConfiguration> {

    public SaguaroCactusFeature(Codec<TreeConfiguration> config) {
        super(config);
    }

    private static int randBetween(int low, int high, RandomSource rand) {
        return low + rand.nextInt(high - low + 1);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();

        int height = rand.nextInt(3) + 4;
        Direction dir = Direction.from2DDataValue(rand.nextInt(4));
        boolean back = rand.nextBoolean();
        if (position.getY() <= 0 || position.getY() + height + 1 > level.getMaxBuildHeight()) {
            return false;
        }
        if (!level.getBlockState(position.below()).is(BlockTags.SAND)) {
            return false;
        }

        for (BlockPos pos : BlockPos.betweenClosed(position.above().relative(dir), position.above(height).relative(dir, back ? -1 : 0))) {
            if (!TreeUtil.isAirOrLeaves(level, pos)) {
                return false;
            }
        }

        for (int i = 0; i <= height; i++) {
            TreeUtil.setForcedState(level, position.above(i), BayouBluesBlocksBadlands.SAGUARO_CACTUS.get().defaultBlockState());
        }
        TreeUtil.setForcedState(level, position.above(height + 1), BayouBluesBlocksBadlands.SAGUARO_FLOWER.get().defaultBlockState());

        int depth1 = 1 + rand.nextInt(height - 3);
        int depth2 = 1 + rand.nextInt(height - 3);
        int height1 = randBetween(depth1 + 1, height - 1, rand);
        int height2 = randBetween(depth2 + 1, height - 1, rand);

        for (int i = depth1; i <= height1; i++) {
            BlockState sag1 = BayouBluesBlocksBadlands.SMALL_SAGUARO_CACTUS.get().defaultBlockState();
            if (i == depth1) {
                sag1 = sag1.setValue(PipeBlock.UP, true).setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir.getOpposite()), true);
            } else if (i == height1) {
                if (rand.nextBoolean()) {
                    sag1 = sag1.setValue(PipeBlock.DOWN, true).setValue(PipeBlock.UP, true);
                    TreeUtil.setForcedState(level, position.relative(dir).above(i + 1), BayouBluesBlocksBadlands.SAGUARO_FLOWER.get().defaultBlockState());
                } else {
                    sag1 = sag1.setValue(PipeBlock.DOWN, true);
                }
            } else {
                sag1 = sag1.setValue(PipeBlock.UP, true).setValue(PipeBlock.DOWN, true);
            }
            TreeUtil.setForcedState(level, position.relative(dir).above(i), sag1);
        }
        if (back) {
            for (int i = depth2; i <= height2; i++) {
                BlockState sag2 = BayouBluesBlocksBadlands.SMALL_SAGUARO_CACTUS.get().defaultBlockState();
                if (i == depth2) {
                    sag2 = sag2.setValue(PipeBlock.UP, true).setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), true);
                } else if (i == height2) {
                    if (rand.nextBoolean()) {
                        sag2 = sag2.setValue(PipeBlock.DOWN, true).setValue(PipeBlock.UP, true);
                        TreeUtil.setForcedState(level, position.relative(dir.getOpposite()).above(i + 1), BayouBluesBlocksBadlands.SAGUARO_FLOWER.get().defaultBlockState());
                    } else {
                        sag2 = sag2.setValue(PipeBlock.DOWN, true);
                    }
                } else {
                    sag2 = sag2.setValue(PipeBlock.UP, true).setValue(PipeBlock.DOWN, true);
                }
                TreeUtil.setForcedState(level, position.relative(dir.getOpposite()).above(i), sag2);
            }
        }

        return true;
    }
}
