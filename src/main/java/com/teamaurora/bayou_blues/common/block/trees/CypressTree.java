package com.teamaurora.bayou_blues.common.block.trees;

import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class CypressTree extends AbstractMegaTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean largeHive) {
        return BayouBluesFeatures.Configured.CYPRESS_GROWN;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
        return BayouBluesFeatures.Configured.MEGA_CYPRESS_GROWN;
    }
}
