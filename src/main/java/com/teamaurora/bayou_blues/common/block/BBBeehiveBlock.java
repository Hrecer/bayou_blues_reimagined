package com.teamaurora.bayou_blues.common.block;

import com.teamaurora.bayou_blues.common.blockentity.BBBeehiveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 自定义蜂巢方块
 */
public class BBBeehiveBlock extends BeehiveBlock {

    public BBBeehiveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BBBeehiveBlockEntity(pos, state);
    }
}
