package com.teamaurora.bayou_blues.common.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teamaurora.bayou_blues.common.util.TreeUtil;
import com.teamaurora.bayou_blues.common.util.DirectionalBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaterMegaCypressFeature extends Feature<TreeConfiguration> {
    public WaterMegaCypressFeature(Codec<TreeConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        BlockPos posIn = context.origin();
        TreeConfiguration config = context.config();

        int height = rand.nextInt(7) + 18;
        boolean bald = (rand.nextInt(15) == 0);
        if (posIn.getY() <= 0 || posIn.getY() + height > world.getMaxBuildHeight() - 1) {
            return false;
        }

        int surfaceY = world.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.OCEAN_FLOOR, posIn.getX(), posIn.getZ());
        int waterY = world.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, posIn.getX(), posIn.getZ());

        if (waterY <= surfaceY) {
            return false;
        }

        BlockPos position = new BlockPos(posIn.getX(), waterY, posIn.getZ());
        BlockPos bottom = new BlockPos(posIn.getX(), surfaceY, posIn.getZ());

        // Check if the 2x2 area is clear
        for (BlockPos pos2 : BlockPos.betweenClosed(bottom, bottom.offset(1, 0, 1))) {
            if (!isAirOrWaterOrLeaves(world, pos2)) {
                return false;
            }
        }

        List<DirectionalBlockPos> logs = new ArrayList<>();
        List<BlockPos> leaves = new ArrayList<>();

        // Build underwater trunk (4x4)
        for (int i = 0; i <= waterY - surfaceY; i++) {
            for (int x = -1; x <= 2; x++) {
                for (int z = -1; z <= 2; z++) {
                    if (i == 0 && world.getFluidState(bottom.offset(x, -1, z)).getType() == Fluids.WATER) {
                        return false;
                    }
                    logs.add(new DirectionalBlockPos(bottom.offset(x, i, z), Direction.UP));
                }
            }
        }

        // Build above-water trunk
        for (int i = 1; i <= height; i++) {
            if (i <= 2) {
                // Cross pattern for first 2 blocks above water (2x2 center)
                for (int x = -1; x <= 2; x++) {
                    for (int z = -1; z <= 2; z++) {
                        if ((x != -1 && x != 2) || (z != -1 && z != 2)) {
                            logs.add(new DirectionalBlockPos(position.offset(x, i, z), Direction.UP));
                        }
                    }
                }
            } else {
                logs.add(new DirectionalBlockPos(position.above(i), Direction.UP));
                logs.add(new DirectionalBlockPos(position.offset(1, i, 0), Direction.UP));
                logs.add(new DirectionalBlockPos(position.offset(0, i, 1), Direction.UP));
                logs.add(new DirectionalBlockPos(position.offset(1, i, 1), Direction.UP));
            }
        }

        // Add branches
        int numBranches = rand.nextInt(5) + 4;
        for (int j = 0; j < numBranches; j++) {
            int branchHeight;
            if (bald) {
                branchHeight = rand.nextInt(height - 5) + 7;
            } else {
                branchHeight = rand.nextInt(height - 7) + 7;
            }

            Direction dir = Direction.from2DDataValue(rand.nextInt(4));
            if (dir == Direction.NORTH) {
                addBranch(position.offset(rand.nextInt(2), branchHeight, 0), dir, logs, leaves, rand);
            } else if (dir == Direction.EAST) {
                addBranch(position.offset(1, branchHeight, rand.nextInt(2)), dir, logs, leaves, rand);
            } else if (dir == Direction.SOUTH) {
                addBranch(position.offset(rand.nextInt(2), branchHeight, 1), dir, logs, leaves, rand);
            } else if (dir == Direction.WEST) {
                addBranch(position.offset(0, branchHeight, rand.nextInt(2)), dir, logs, leaves, rand);
            }
        }

        // Add top
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

        // Check if space is clear
        boolean flag = true;
        for (DirectionalBlockPos log : logs) {
            if (!TreeUtil.isAirOrLeaves(world, log.pos) && world.getBlockState(log.pos).getBlock() != Blocks.WATER) {
                flag = false;
                break;
            }
        }
        if (!flag) return false;

        // Place dirt at base
        TreeUtil.setDirtAt(world, position.below());

        // Place logs
        for (DirectionalBlockPos log : logs) {
            TreeUtil.placeDirectionalLogAt(world, log.pos, log.direction, rand, config);
        }

        // Place leaves
        for (BlockPos leaf : leavesClean) {
            TreeUtil.placeLeafAt(world, leaf, rand, config);
        }

        // Apply decorators
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

    public static boolean isAirOrWater(WorldGenLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.getFluidState().is(FluidTags.WATER);
    }

    public static boolean isAirOrWaterOrLeaves(WorldGenLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.getFluidState().is(FluidTags.WATER) || state.is(BlockTags.LEAVES);
    }
}
