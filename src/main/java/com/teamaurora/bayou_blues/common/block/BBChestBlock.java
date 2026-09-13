package com.teamaurora.bayou_blues.common.block;

import com.teamaurora.bayou_blues.common.blockentity.BBChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.content.building.block.VariantChestBlock;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.function.Supplier;

/**
 * 丝柏箱子方块：覆写 newBlockEntity，使放置时产生绑定自己 BE 类型的
 * {@link BBChestBlockEntity}（Quark 父类产生的 BE 类型写死为 Quark 的
 * chestTEType，有效方块不含我们的箱子，放置后 BE 被判无效移除）。
 */
public class BBChestBlock extends VariantChestBlock {

    public BBChestBlock(String type, Supplier<net.minecraft.world.level.block.entity.BlockEntityType<? extends net.minecraft.world.level.block.entity.ChestBlockEntity>> beType, Properties props) {
        super(type, (ZetaModule) null, beType, props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BBChestBlockEntity(pos, state);
    }
}
