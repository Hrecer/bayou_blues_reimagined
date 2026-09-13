package com.teamaurora.bayou_blues.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class HangingCypressLeavesTreeDecorator extends TreeDecorator {
    public static final Codec<HangingCypressLeavesTreeDecorator> CODEC;
    public static final HangingCypressLeavesTreeDecorator DECORATOR = new HangingCypressLeavesTreeDecorator();

    @Override
    protected TreeDecoratorType<?> type() {
        return BayouBluesFeatures.HANGING_CYPRESS_LEAVES.get();
    }

    static {
        CODEC = Codec.unit(() -> DECORATOR);
    }

    @Override
    public void place(TreeDecorator.Context context) {
        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();
        
        for (BlockPos pos : context.leaves()) {
            BlockPos below = pos.below();
            if (level.isStateAtPosition(below, BlockState::isAir) && random.nextInt(3) == 0) {
                context.setBlock(below, BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get().defaultBlockState());
            }
        }
    }
}
