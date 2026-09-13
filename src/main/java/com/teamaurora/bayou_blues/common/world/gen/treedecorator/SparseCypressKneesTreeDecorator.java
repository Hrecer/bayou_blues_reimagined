package com.teamaurora.bayou_blues.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.block.CypressKneeBlock;
import com.teamaurora.bayou_blues.common.block.DoubleCypressKneeBlock;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class SparseCypressKneesTreeDecorator extends TreeDecorator {
    public static final SparseCypressKneesTreeDecorator DECORATOR = new SparseCypressKneesTreeDecorator();
    public static final Codec<SparseCypressKneesTreeDecorator> CODEC;

    @Override
    protected TreeDecoratorType<?> type() {
        return BayouBluesFeatures.SPARSE_CYPRESS_KNEES.get();
    }

    static {
        CODEC = Codec.unit(() -> DECORATOR);
    }

    @Override
    public void place(TreeDecorator.Context context) {
        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();

        int minY = Integer.MAX_VALUE;
        int maxWaterY = 0;

        // Find the lowest log position and highest water-adjacent position
        for (BlockPos pos : context.logs()) {
            if (pos.getY() < minY) minY = pos.getY();
            if (pos.getY() > maxWaterY) {
                for (int i = 0; i < 4; i++) {
                    Direction dir = Direction.from2DDataValue(i);
                    BlockPos offset = pos.relative(dir);
                    if (level.isFluidAtPosition(offset, fluidState -> fluidState.getType() == Fluids.WATER)) {
                        maxWaterY = pos.getY();
                    }
                }
            }
        }

        if (maxWaterY > minY) minY = maxWaterY;

        // Only place knees at the lowest log level with reduced chance (sparse)
        for (BlockPos pos : context.logs()) {
            if (pos.getY() == minY && random.nextInt(6) == 0) { // 1/6 chance (sparse)
                for (int x = -2; x <= 2; x++) {
                    for (int y = -2; y <= 2; y++) {
                        for (int z = -2; z <= 2; z++) {
                            BlockPos newPos = pos.offset(x, y, z);
                            if (pos.distSqr(newPos) <= 9.0D) {
                                if (level.isStateAtPosition(newPos.below(), state -> state.is(Blocks.DIRT)) && isAir(level, newPos)) {
                                    if (random.nextInt(9) == 0) {
                                        if (random.nextInt(3) == 0 && isAir(level, newPos.above())) {
                                            // DoubleCypressKneeBlock.placeAt requires LevelAccessor
                                            context.setBlock(newPos, BayouBluesBlocks.LARGE_CYPRESS_KNEE.get().defaultBlockState());
                                        } else {
                                            context.setBlock(newPos,
                                                BayouBluesBlocks.CYPRESS_KNEE.get().defaultBlockState()
                                                    .setValue(CypressKneeBlock.WATERLOGGED, false));
                                        }
                                    }
                                } else if (level.isFluidAtPosition(newPos, fluidState -> fluidState.getType() == Fluids.WATER)
                                        && level.isStateAtPosition(newPos.below(), BlockState::isSolid)
                                        && random.nextInt(8) == 0) {
                                    if (random.nextInt(4) != 0 && isAir(level, newPos.above())) {
                                        // DoubleCypressKneeBlock.placeAt requires LevelAccessor
                                        context.setBlock(newPos, BayouBluesBlocks.LARGE_CYPRESS_KNEE.get().defaultBlockState());
                                    } else {
                                        context.setBlock(newPos,
                                            BayouBluesBlocks.CYPRESS_KNEE.get().defaultBlockState()
                                                .setValue(CypressKneeBlock.WATERLOGGED, true));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isAir(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, BlockState::isAir);
    }
}
