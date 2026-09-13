package com.teamaurora.bayou_blues.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

/**
 * Fills a cube of radius 6 around the origin, placing the state wherever it can survive,
 * with a placement probability that falls off as 1 - distanceSq / 72.
 */
public class LargePatchFeature extends Feature<BlockStateConfiguration> {
    public LargePatchFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockState state = context.config().state;

        int i = 0;
        for (BlockPos newPos : BlockPos.betweenClosed(origin.offset(-6, -6, -6), origin.offset(6, 6, 6))) {
            if (state.canSurvive(world, newPos) && random.nextFloat() <= 1.0F - (float) newPos.distSqr(origin) / 72.0F) {
                world.setBlock(newPos, state, 3);
                i++;
            }
        }

        return i > 0;
    }
}
