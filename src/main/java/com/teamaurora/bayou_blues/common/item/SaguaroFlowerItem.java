package com.teamaurora.bayou_blues.common.item;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 萨瓜罗花物品：点沙子上放幼苗，点萨瓜罗/小枝上放花（与 Better Badlands 1.16.5 行为一致）。
 */
public class SaguaroFlowerItem extends BlockItem {

    public SaguaroFlowerItem(Properties properties) {
        super(BayouBluesBlocksBadlands.SAGUARO_FLOWER.get(), properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        if (context.getClickedFace() != Direction.UP) {
            return null;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block blockDown = level.getBlockState(pos.below()).getBlock();
        BlockState blockstate = null;
        if (blockDown.defaultBlockState().is(BlockTags.SAND)) {
            blockstate = BayouBluesBlocksBadlands.SAGUARO_SPROUT.get().getStateForPlacement(context);
        } else if (blockDown == BayouBluesBlocksBadlands.SAGUARO_CACTUS.get()
                || blockDown == BayouBluesBlocksBadlands.SMALL_SAGUARO_CACTUS.get()) {
            blockstate = BayouBluesBlocksBadlands.SAGUARO_FLOWER.get().getStateForPlacement(context);
        }
        return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
    }
}
