package com.teamaurora.bayou_blues.common.blockentity;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.building.block.be.VariantChestBlockEntity;

/**
 * 丝柏陷阱箱方块实体：绑定自己的 BE 类型，并保留 Quark 陷阱箱
 * 开关时发出红石信号的行为。
 */
public class BBTrappedChestBlockEntity extends VariantChestBlockEntity {

    public BBTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        super(BayouBluesBlockEntities.CYPRESS_TRAPPED_CHEST.get(), pos, state);
    }

    @Override
    protected void signalOpenCount(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, int prevOpenCount, int openCount) {
        super.signalOpenCount(world, pos, state, prevOpenCount, openCount);
        if (prevOpenCount != openCount) {
            Block block = state.getBlock();
            world.updateNeighborsAt(pos, block);
            world.updateNeighborsAt(pos.below(), block);
        }
    }
}
