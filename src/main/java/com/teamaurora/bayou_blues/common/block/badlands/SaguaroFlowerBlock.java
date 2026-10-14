package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;

/**
 * 萨瓜罗花。只能坐在萨瓜罗主体或小枝上（放置由 {@link com.teamaurora.bayou_blues.common.item.SaguaroFlowerItem}
 * 决定：点沙子上给幼苗，点萨瓜罗上给花）。
 */
public class SaguaroFlowerBlock extends BushBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public SaguaroFlowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Block below = level.getBlockState(pos.below()).getBlock();
        return below == BayouBluesBlocksBadlands.SAGUARO_CACTUS.get()
                || below == BayouBluesBlocksBadlands.SMALL_SAGUARO_CACTUS.get();
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.DESERT;
    }
}
