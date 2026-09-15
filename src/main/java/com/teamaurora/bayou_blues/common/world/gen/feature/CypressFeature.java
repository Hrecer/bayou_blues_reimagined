package com.teamaurora.bayou_blues.common.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.util.TreeUtil;
import com.teamaurora.bayou_blues.common.util.DirectionalBlockPos;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CypressFeature extends Feature<TreeConfiguration> {
    public CypressFeature(Codec<TreeConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        TreeConfiguration config = context.config();
        
        int height = rand.nextInt(5) + 9;
        boolean bald = (rand.nextInt(15) == 0);
        if (position.getY() <= 0 || position.getY() + height > world.getMaxBuildHeight() - 1) {
            return false;
        }
        if (!TreeUtil.isValidGround(world, position.below())) {
            return false;
        }
        
        List<DirectionalBlockPos> logs = new ArrayList<>();
        List<BlockPos> leaves = new ArrayList<>();
        
        for (int i = 0; i <= height; i++) {
            logs.add(new DirectionalBlockPos(position.above(i), Direction.UP));
        }
        int numBranches = rand.nextInt(4) + 1;
        if (numBranches == 4) numBranches = 2;
        for (int j = 0; j < numBranches; j++) {
            int x;
            Direction dir = Direction.from2DDataValue(rand.nextInt(4));
            
            if (bald) {
                x = rand.nextInt(height - 3) + 3;
            } else {
                x = rand.nextInt(height - 5) + 3;
            }
            logs.add(new DirectionalBlockPos(position.above(x).relative(dir), dir));
            logs.add(new DirectionalBlockPos(position.above(x).relative(dir, 2), dir));
            disc2H(position.above(x).relative(dir, 2), leaves, rand);
            disc1(position.above(x + 1).relative(dir, 2), leaves);
        }
        if (!bald) {
            disc1(position.above(height - 1), leaves);
            disc3H(position.above(height), leaves, rand);
            disc2(position.above(height + 1), leaves);
        }
        
        List<BlockPos> leavesClean = cleanLeavesArray(leaves, logs);
        
        boolean flag = true;
        for (DirectionalBlockPos log : logs) {
            // 用宽容判定：本模组残留的柏树原木（上次装饰被中断的半截树）不视为
            // 障碍，强制覆盖放置即可补全整棵树；详见 TreeUtil.isValidTrunkPosition。
            if (!TreeUtil.isValidTrunkPosition(world, log.pos)) {
                flag = false;
            }
        }
        if (!flag) return false;
        
        TreeUtil.setDirtAt(world, position.below());
        
        for (DirectionalBlockPos log : logs) {
            TreeUtil.placeDirectionalLogAt(world, log.pos, log.direction, rand, config);
        }
        for (BlockPos leaf : leavesClean) {
            TreeUtil.placeLeafAt(world, leaf, rand, config);
        }

        List<BlockPos> logsPos = new ArrayList<>();
        for (DirectionalBlockPos log : logs) {
            logsPos.add(log.pos);
        }
        
        if (!config.decorators.isEmpty()) {
            logsPos.sort(Comparator.comparingInt(BlockPos::getY));
            leavesClean.sort(Comparator.comparingInt(BlockPos::getY));
            Set<BlockPos> logsSet = new HashSet<>(logsPos);
            Set<BlockPos> leavesSet = new HashSet<>(leavesClean);
            Set<BlockPos> decorators = new HashSet<>();
            TreeDecorator.Context decoratorContext = new TreeDecorator.Context(world, (pos, state) -> world.setBlock(pos, state, 3), rand, logsSet, leavesSet, decorators);
            config.decorators.forEach(decorator -> decorator.place(decoratorContext));
        }
        
        return true;
    }
    
    private void disc1(BlockPos pos, List<BlockPos> leaves) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (Math.abs(x) != 1 || Math.abs(z) != 1) {
                    leaves.add(pos.offset(x, 0, z));
                }
            }
        }
    }
    
    private void disc2(BlockPos pos, List<BlockPos> leaves) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) != 2 || Math.abs(z) != 2) {
                    leaves.add(pos.offset(x, 0, z));
                }
            }
        }
    }
    
    private void disc2H(BlockPos pos, List<BlockPos> leaves, RandomSource rand) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) != 2 || Math.abs(z) != 2) {
                    leaves.add(pos.offset(x, 0, z));
                    if (rand.nextInt(3) == 0) {
                        leaves.add(pos.offset(x, -1, z));
                        if (rand.nextInt(3) == 0) {
                            leaves.add(pos.offset(x, -2, z));
                        }
                    }
                }
            }
        }
    }
    
    private void disc3H(BlockPos pos, List<BlockPos> leaves, RandomSource rand) {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) != 3 || Math.abs(z) != 3) {
                    leaves.add(pos.offset(x, 0, z));
                    if (rand.nextInt(3) == 0) {
                        leaves.add(pos.offset(x, -1, z));
                        if (rand.nextBoolean()) {
                            leaves.add(pos.offset(x, -2, z));
                        }
                    }
                }
            }
        }
    }
    
    private List<BlockPos> cleanLeavesArray(List<BlockPos> leaves, List<DirectionalBlockPos> logs) {
        List<BlockPos> logsPos = new ArrayList<>();
        for (DirectionalBlockPos log : logs) {
            logsPos.add(log.pos);
        }
        List<BlockPos> newLeaves = new ArrayList<>();
        for (BlockPos leaf : leaves) {
            if (!logsPos.contains(leaf)) {
                newLeaves.add(leaf);
            }
        }
        return newLeaves;
    }
}
