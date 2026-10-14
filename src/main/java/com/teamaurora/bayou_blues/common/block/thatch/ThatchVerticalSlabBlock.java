package com.teamaurora.bayou_blues.common.block.thatch;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.violetmoon.quark.content.building.block.VerticalSlabBlock;

import java.util.function.Supplier;

public class ThatchVerticalSlabBlock extends VerticalSlabBlock {
    public ThatchVerticalSlabBlock(BlockBehaviour.Properties properties) {
        this(BayouBluesBlocks.ALGAE_THATCH, properties);
    }

    public ThatchVerticalSlabBlock(Supplier<Block> parent, BlockBehaviour.Properties properties) {
        super(parent, properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new Vec3(0.8D, 1.0D, 0.8D));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }
}
