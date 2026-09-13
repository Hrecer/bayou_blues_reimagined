package com.teamaurora.bayou_blues.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.block.CypressBranchBlock;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class CypressBranchTreeDecorator extends TreeDecorator {
    public static final Codec<CypressBranchTreeDecorator> CODEC;
    public static final CypressBranchTreeDecorator DECORATOR = new CypressBranchTreeDecorator();

    @Override
    protected TreeDecoratorType<?> type() {
        return BayouBluesFeatures.CYPRESS_BRANCH.get();
    }

    static {
        CODEC = Codec.unit(() -> DECORATOR);
    }

    @Override
    public void place(TreeDecorator.Context context) {
        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();
        
        for (BlockPos pos : context.logs()) {
            if (random.nextInt(25) == 0) {
                Direction dir = Direction.from2DDataValue(random.nextInt(4));
                BlockPos placePos = pos.relative(dir);
                if (level.isStateAtPosition(placePos, BlockState::isAir)) {
                    context.setBlock(placePos, 
                        BayouBluesBlocks.CYPRESS_BRANCH.get().defaultBlockState()
                            .setValue(CypressBranchBlock.FACING, dir)
                            .setValue(CypressBranchBlock.AGE, 2));
                }
            }
        }
    }
}
