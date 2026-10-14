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

import java.util.ArrayList;
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

        int minY = logs.get(0).getY();
        for (BlockPos pos : logs) {
            if (pos.getY() < minY) {
                minY = pos.getY();
            }
        }

        List<BlockPos> lowestLogs = new ArrayList<>();
        for (BlockPos pos : logs) {
            if (pos.getY() == minY) {
                lowestLogs.add(pos);
            }
        }

        int avgX = 0;
        int avgZ = 0;
        for (BlockPos pos : lowestLogs) {
            avgX += pos.getX();
            avgZ += pos.getZ();
        }
        avgX /= lowestLogs.size();
        avgZ /= lowestLogs.size();

        BlockPos center = new BlockPos(avgX, minY, avgZ);
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) + Math.abs(z) <= 4 && (x != 0 || z != 0)) {
                    BlockPos checkPos = center.offset(x, 0, z);
                    for (int y = -2; y <= 1; y++) {
                        BlockPos target = checkPos.above(y);
                        if (isValidGround(level, target.below()) && isAirOrWater(level, target)) {
                            boolean waterBelow = isWater(level, target);
                            if (rand.nextInt(4) == 0) {
                                if (rand.nextInt(3) == 0 && isAirOrWater(level, target.above())) {
                                    boolean waterAbove = isWater(level, target.above());
                                    context.setBlock(target, BayouBluesBlocks.LARGE_CYPRESS_KNEE.get().defaultBlockState()
                                            .setValue(DoubleCypressKneeBlock.HALF, DoubleBlockHalf.LOWER)
                                            .setValue(DoubleCypressKneeBlock.WATERLOGGED, waterBelow));
                                    context.setBlock(target.above(), BayouBluesBlocks.LARGE_CYPRESS_KNEE.get().defaultBlockState()
                                            .setValue(DoubleCypressKneeBlock.HALF, DoubleBlockHalf.UPPER)
                                            .setValue(DoubleCypressKneeBlock.WATERLOGGED, waterAbove));
                                } else {
                                    context.setBlock(target, BayouBluesBlocks.CYPRESS_KNEE.get().defaultBlockState()
                                            .setValue(CypressKneeBlock.WATERLOGGED, waterBelow));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean isValidGround(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state ->
                state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT)
                        || state.is(Blocks.PODZOL) || state.is(Blocks.CLAY) || state.is(Blocks.GRAVEL) || state.is(Blocks.SAND));
    }

    private static boolean isAirOrWater(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.isAir() || state.getFluidState().is(FluidTags.WATER));
    }

    private static boolean isWater(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.getFluidState().is(FluidTags.WATER));
    }
}
