package com.teamaurora.bayou_blues.common.blockentity;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.content.building.block.be.VariantChestBlockEntity;

/**
 * 丝柏箱子方块实体。Quark 的 VariantChestBlockEntity 无参构造把类型写死为
 * Quark 自己的 chestTEType（有效方块不含我们的箱子，放置时 BE 校验失败被丢弃，
 * 导致箱子不可交互、不渲染），这里通过受保护的类型构造器绑定自己的 BE 类型。
 */
public class BBChestBlockEntity extends VariantChestBlockEntity {

    public BBChestBlockEntity(BlockPos pos, BlockState state) {
        super(BayouBluesBlockEntities.CYPRESS_CHEST.get(), pos, state);
    }
}
