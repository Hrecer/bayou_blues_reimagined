package com.teamaurora.bayou_blues.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * BayouBlues 自定义告示牌方块
 */
public final class BBSignBlocks {

    private BBSignBlocks() {}

    /**
     * 站立的告示牌方块
     */
    public static class StandingSign extends StandingSignBlock {
        public StandingSign(Properties properties, WoodType woodType) {
            super(properties, woodType);
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new SignBlockEntity(pos, state);
        }
    }

    /**
     * 墙上的告示牌方块
     */
    public static class WallSign extends WallSignBlock {
        public WallSign(Properties properties, WoodType woodType) {
            super(properties, woodType);
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new SignBlockEntity(pos, state);
        }
    }
}
