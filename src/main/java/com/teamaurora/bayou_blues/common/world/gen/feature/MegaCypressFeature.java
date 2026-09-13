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

public class MegaCypressFeature extends Feature<TreeConfiguration> {
    public MegaCypressFeature(Codec<TreeConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        TreeConfiguration config = context.config();
        
        int height = rand.nextInt(7) + 15;
        boolean bald = (rand.nextInt(15) == 0);
        if (position.getY() <= 0 || position.getY() + height > world.getMaxBuildHeight() - 1) {
            return false;
        }
        for (BlockPos pos2 : BlockPos.betweenClosed(position, position.offset(1, 0, 1))) {
            if (!TreeUtil.isValidGround(world, pos2.below())) {
                return false;
            }
        }

        List<DirectionalBlockPos> logs = new ArrayList<>();
        List<BlockPos> leaves = new ArrayList<>();

        for (int i = 0; i <= height; i++) {
            logs.add(new DirectionalBlockPos(position.above(i), Direction.UP));
            logs.add(new DirectionalBlockPos(position.offset(1, i, 0), Direction.UP));
            logs.add(new DirectionalBlockPos(position.offset(0, i, 1), Direction.UP));
            logs.add(new DirectionalBlockPos(position.offset(1, i, 1), Direction.UP));
        }
        int numBranches = rand.nextInt(5) + 4;
        for (int j = 0; j < numBranches; j++) {
            int x;
            if (bald) {
                x = rand.nextInt(height - 5) + 4;
            } else {
                x = rand.nextInt(height - 7) + 4;
            }
            Direction dir = Direction.from2DDataValue(rand.nextInt(4));
            if (dir == Direction.NORTH) {
                addBranch(position.offset(rand.nextInt(2), x, 0), dir, logs, leaves, rand);
            } else if (dir == Direction.EAST) {
                addBranch(position.offset(1, x, rand.nextInt(2)), dir, logs, leaves, rand);
            } else if (dir == Direction.SOUTH) {
                addBranch(position.offset(rand.nextInt(2), x, 1), dir, logs, leaves, rand);
            } else if (dir == Direction.WEST) {
                addBranch(position.offset(0, x, rand.nextInt(2)), dir, logs, leaves, rand);
            }
        }
        if (bald) {
            int variant = rand.nextInt(4);
            switch (variant) {
                case 0:
                    logs.add(new DirectionalBlockPos(position.above(height + 1), Direction.UP));
                    break;
                case 1:
                    logs.add(new DirectionalBlockPos(position.offset(1, height + 1, 0), Direction.UP));
                    break;
                case 2:
                    logs.add(new DirectionalBlockPos(position.offset(0, height + 1, 1), Direction.UP));
                    break;
                case 3:
                    logs.add(new DirectionalBlockPos(position.offset(1, height + 1, 1), Direction.UP));
                    break;
            }
        } else {
            canopyDisc1(position.above(height - 2), leaves);
            canopyDisc3Bottom(position.above(height - 1), leaves, rand);
            canopyDisc3Top(position.above(height), leaves);
            canopyDisc1(position.above(height + 1), leaves);
        }

        List<BlockPos> leavesClean = cleanLeavesArray(leaves, logs);

        boolean flag = true;
        for (DirectionalBlockPos log : logs) {
            if (!TreeUtil.isAirOrLeaves(world, log.pos)) {
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

    private void addBranch(BlockPos pos, Direction dir, List<DirectionalBlockPos> logs, List<BlockPos> leaves, RandomSource rand) {
        logs.add(new DirectionalBlockPos(pos.relative(dir), dir));
        logs.add(new DirectionalBlockPos(pos.relative(dir, 2), dir));
        disc2H(pos.relative(dir, 2), leaves, rand);
        disc1(pos.relative(dir, 2).above(), leaves);
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

    private void canopyDisc1(BlockPos pos, List<BlockPos> leaves) {
        for (int x = -1; x <= 2; x++) {
            for (int z = -1; z <= 2; z++) {
                if ((x != -1 && x != 2) || (z != -1 && z != 2)) {
                    leaves.add(pos.offset(x, 0, z));
                }
            }
        }
    }

    private void canopyDisc3Top(BlockPos pos, List<BlockPos> leaves) {
        for (int x = -3; x <= 4; x++) {
            for (int z = -3; z <= 4; z++) {
                if ((x > -2 && x < 3) || (z > -2 && z < 3) || ((x == -2 || x == 3) && (z == -2 || z == 3))) {
                    leaves.add(pos.offset(x, 0, z));
                }
            }
        }
    }

    private void canopyDisc3Bottom(BlockPos pos, List<BlockPos> leaves, RandomSource rand) {
        for (int x = -3; x <= 4; x++) {
            for (int z = -3; z <= 4; z++) {
                if ((x != -3 && x != 4) || (z != -3 && z != 4)) {
                    leaves.add(pos.offset(x, 0, z));
                    if (rand.nextBoolean()) {
                        leaves.add(pos.offset(x, -1, z));
                        if (rand.nextInt(3) != 0) {
                            leaves.add(pos.offset(x, -2, z));
                            if (rand.nextBoolean()) {
                                leaves.add(pos.offset(x, -3, z));
                            }
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
