package com.teamaurora.bayou_blues.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class SparseLeaveVineTreeDecorator extends TreeDecorator {
    public static final SparseLeaveVineTreeDecorator DECORATOR = new SparseLeaveVineTreeDecorator();
    public static final Codec<SparseLeaveVineTreeDecorator> CODEC;

    @Override
    protected TreeDecoratorType<?> type() {
        return BayouBluesFeatures.SPARSE_LEAVE_VINE.get();
    }

    static {
        CODEC = Codec.unit(() -> DECORATOR);
    }

    @Override
    public void place(TreeDecorator.Context context) {
        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();

        for (BlockPos pos : context.leaves()) {
            // West
            if (random.nextInt(16) == 0) {
                BlockPos blockpos = pos.west();
                if (TreeUtil.isAir(level, blockpos)) {
                    placeVine(level, blockpos, VineBlock.EAST, context);
                }
            }
            // East
            if (random.nextInt(16) == 0) {
                BlockPos blockpos = pos.east();
                if (TreeUtil.isAir(level, blockpos)) {
                    placeVine(level, blockpos, VineBlock.WEST, context);
                }
            }
            // North
            if (random.nextInt(16) == 0) {
                BlockPos blockpos = pos.north();
                if (TreeUtil.isAir(level, blockpos)) {
                    placeVine(level, blockpos, VineBlock.SOUTH, context);
                }
            }
            // South
            if (random.nextInt(16) == 0) {
                BlockPos blockpos = pos.south();
                if (TreeUtil.isAir(level, blockpos)) {
                    placeVine(level, blockpos, VineBlock.NORTH, context);
                }
            }
        }
    }

    private void placeVine(LevelSimulatedReader level, BlockPos pos, net.minecraft.world.level.block.state.properties.BooleanProperty direction, TreeDecorator.Context context) {
        context.setBlock(pos, Blocks.VINE.defaultBlockState().setValue(direction, true));
        int length = 4;

        for (BlockPos blockpos = pos.below(); TreeUtil.isAir(level, blockpos) && length > 0; length--) {
            context.setBlock(blockpos, Blocks.VINE.defaultBlockState().setValue(direction, true));
            blockpos = blockpos.below();
        }
    }

    // Helper class for TreeUtil methods
    private static class TreeUtil {
        public static boolean isAir(LevelSimulatedReader level, BlockPos pos) {
            return level.isStateAtPosition(pos, BlockState::isAir);
        }
    }
}
