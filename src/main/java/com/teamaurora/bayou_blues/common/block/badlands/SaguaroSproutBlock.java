package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.common.block.trees.SaguaroCactusTree;
import com.teamaurora.bayou_blues.core.registry.BayouBluesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 萨瓜罗幼苗（无自身物品——捡起/破坏会得到"萨瓜罗花"，见 {@link BayouBluesItems#SAGUARO_FLOWER}）。
 * 只能长在沙子上，骨粉/随机刻后长成完整萨瓜罗（{@link SaguaroCactusTree}）。
 */
public class SaguaroSproutBlock extends SaplingBlock {

    public SaguaroSproutBlock(SaguaroCactusTree tree, Properties properties) {
        super(tree, properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(BayouBluesItems.SAGUARO_FLOWER.get());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(BlockTags.SAND);
    }
}
