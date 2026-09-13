package com.teamaurora.bayou_blues.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.util.TreeUtil;
import com.teamaurora.bayou_blues.common.block.BeardMossBlock;
import com.teamaurora.bayou_blues.common.block.BeardMossBlockBlock;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class BeardMossTreeDecorator extends TreeDecorator {
    public static final BeardMossTreeDecorator DECORATOR = new BeardMossTreeDecorator();
    public static final Codec<BeardMossTreeDecorator> CODEC;

    @Override
    protected TreeDecoratorType<?> type() {
        return BayouBluesFeatures.BEARD_MOSS.get();
    }

    static {
        CODEC = Codec.unit(() -> DECORATOR);
    }

    @Override
    public void place(TreeDecorator.Context context) {
        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();

        // Place beard moss on logs
        for (BlockPos pos : context.logs()) {
            BlockPos below = pos.below();
            if (TreeUtil.isAirOrLeaves(level, below)) {
                boolean flag = true;
                int rand1 = random.nextInt(3) + 1;
                int i;
                for (i = 0; i < rand1; i++) {
                    if (!TreeUtil.isAirOrLeaves(level, below.below(i))) {
                        flag = false;
                        break;
                    }
                }
                if (!TreeUtil.isAir(level, below.below(rand1))) flag = false;
                if (flag) {
                    for (i = 0; i < rand1; i++) {
                        context.setBlock(below.below(i), 
                            BayouBluesBlocks.BEARD_MOSS_BLOCK.get().defaultBlockState()
                                .setValue(BeardMossBlockBlock.PERSISTENT, false));
                    }
                    int rand2 = random.nextInt(6) + 1;
                    for (int j = 0; j < rand2; j++) {
                        if (!level.isStateAtPosition(below.below(rand1 + j), BlockState::isAir)) {
                            if (j > 0) {
                                context.setBlock(below.below(rand1 + j - 1), 
                                    BayouBluesBlocks.BEARD_MOSS.get().defaultBlockState());
                            }
                            break;
                        }
                        if (j == rand2 - 1) {
                            context.setBlock(below.below(rand1 + j), 
                                BayouBluesBlocks.BEARD_MOSS.get().defaultBlockState());
                        } else {
                            context.setBlock(below.below(rand1 + j), 
                                BayouBluesBlocks.BEARD_MOSS.get().defaultBlockState()
                                    .setValue(BeardMossBlock.HALF, DoubleBlockHalf.UPPER));
                        }
                    }
                }
            }
        }

        // Place beard moss on leaves
        for (BlockPos pos : context.leaves()) {
            if (random.nextInt(6) == 0) {
                BlockPos below = pos.below();
                if (TreeUtil.isAirOrLeaves(level, below)) {
                    boolean flag = true;
                    int rand1 = random.nextInt(2) + 1;
                    int i;
                    for (i = 0; i < rand1; i++) {
                        if (!TreeUtil.isAirOrLeaves(level, below.below(i))) {
                            flag = false;
                            break;
                        }
                    }
                    if (!TreeUtil.isAir(level, below.below(rand1))) flag = false;
                    if (flag) {
                        for (i = 0; i < rand1; i++) {
                            context.setBlock(below.below(i), 
                                BayouBluesBlocks.BEARD_MOSS_BLOCK.get().defaultBlockState()
                                    .setValue(BeardMossBlockBlock.PERSISTENT, false));
                        }
                        int rand2 = random.nextInt(4) + 1;
                        for (int j = 0; j < rand2; j++) {
                            if (!level.isStateAtPosition(below.below(rand1 + j), BlockState::isAir)) {
                                if (j > 0) {
                                    context.setBlock(below.below(rand1 + j - 1), 
                                        BayouBluesBlocks.BEARD_MOSS.get().defaultBlockState());
                                }
                                break;
                            }
                            if (j == rand2 - 1) {
                                context.setBlock(below.below(rand1 + j), 
                                    BayouBluesBlocks.BEARD_MOSS.get().defaultBlockState());
                            } else {
                                context.setBlock(below.below(rand1 + j), 
                                    BayouBluesBlocks.BEARD_MOSS.get().defaultBlockState()
                                        .setValue(BeardMossBlock.HALF, DoubleBlockHalf.UPPER));
                            }
                        }
                    }
                }
            }
        }
    }
}
