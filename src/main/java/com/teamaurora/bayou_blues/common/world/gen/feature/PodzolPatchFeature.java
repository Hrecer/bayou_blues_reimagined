package com.teamaurora.bayou_blues.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Fills a cube of radius 4 around the origin, converting dirt to podzol
 * with a placement probability that falls off as 1 - distanceSq / 32.
 */
public class PodzolPatchFeature extends Feature<NoneFeatureConfiguration> {
    public PodzolPatchFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int i = 0;
        for (BlockPos newPos : BlockPos.betweenClosed(origin.offset(-4, -4, -4), origin.offset(4, 4, 4))) {
            if (world.getBlockState(newPos).is(Blocks.DIRT) && random.nextFloat() <= 1.0F - (float) newPos.distSqr(origin) / 32.0F) {
                world.setBlock(newPos, Blocks.PODZOL.defaultBlockState(), 3);
                i++;
            }
        }

        return i > 0;
    }
}
