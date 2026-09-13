package com.teamaurora.bayou_blues.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * BayouBlues 自定义方块类集合
 * 方块常量类，轻量和集成化
 */
public final class BBBlocks {

    private BBBlocks() {}

    /**
     * 可剥皮的原木方块
     */
    public static class LogBlock extends RotatedPillarBlock {
        private final Supplier<Block> strippedBlock;

        public LogBlock(Supplier<Block> strippedBlock, Properties properties) {
            super(properties);
            this.strippedBlock = strippedBlock;
        }

        @Override
        @Nullable
        public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
            if (action == ToolActions.AXE_STRIP && strippedBlock != null) {
                BlockState strippedState = strippedBlock.get().defaultBlockState();
                if (state.hasProperty(BlockStateProperties.AXIS) && strippedState.hasProperty(BlockStateProperties.AXIS)) {
                    return strippedState.setValue(BlockStateProperties.AXIS, state.getValue(BlockStateProperties.AXIS));
                }
                return strippedState;
            }
            return super.getToolModifiedState(state, context, action, simulate);
        }
    }

    /**
     * 可剥皮的木头方块
     */
    public static class WoodBlock extends RotatedPillarBlock {
        private final Supplier<Block> strippedBlock;

        public WoodBlock(Supplier<Block> strippedBlock, Properties properties) {
            super(properties);
            this.strippedBlock = strippedBlock;
        }

        @Override
        @Nullable
        public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
            if (action == ToolActions.AXE_STRIP && strippedBlock != null) {
                BlockState strippedState = strippedBlock.get().defaultBlockState();
                if (state.hasProperty(BlockStateProperties.AXIS) && strippedState.hasProperty(BlockStateProperties.AXIS)) {
                    return strippedState.setValue(BlockStateProperties.AXIS, state.getValue(BlockStateProperties.AXIS));
                }
                return strippedState;
            }
            return super.getToolModifiedState(state, context, action, simulate);
        }
    }

    /**
     * 茅草方块 - 实体在其上会减速，并放开天光遮蔽（对应 Blueprint/AbnormalsCore 的
     * {@code ThatchBlock}：{@code getShadeBrightness=1}、{@code propagatesSkylightDown=true}）。
     */
    public static class ThatchBlock extends Block {
        public ThatchBlock(Properties properties) {
            super(properties);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
            return 1.0F;
        }

        @Override
        public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
            return true;
        }

        @Override
        public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.8D, 1.0D, 0.8D));
            super.stepOn(level, pos, state, entity);
        }
    }

    /**
     * 茅草台阶
     */
    public static class ThatchSlabBlock extends SlabBlock {
        public ThatchSlabBlock(Properties properties) {
            super(properties);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
            return 1.0F;
        }

        @Override
        public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
            return true;
        }

        @Override
        public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.8D, 1.0D, 0.8D));
            super.stepOn(level, pos, state, entity);
        }
    }

    /**
     * 茅草楼梯
     */
    public static class ThatchStairBlock extends StairBlock {
        public ThatchStairBlock(BlockState baseState, Properties properties) {
            super(baseState, properties);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
            return 1.0F;
        }

        @Override
        public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
            return true;
        }

        @Override
        public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.8D, 1.0D, 0.8D));
            super.stepOn(level, pos, state, entity);
        }
    }
}
