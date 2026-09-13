package com.teamaurora.bayou_blues.common.block;

import com.teamaurora.bayou_blues.common.blockentity.BBTrappedChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.content.building.block.VariantTrappedChestBlock;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.function.Supplier;

/**
 * 丝柏陷阱箱方块：覆写 newBlockEntity，产生绑定自己 BE 类型的
 * {@link BBTrappedChestBlockEntity}（原因同 {@link BBChestBlock}）。
 */
public class BBTrappedChestBlock extends VariantTrappedChestBlock {

    public BBTrappedChestBlock(String type, Supplier<net.minecraft.world.level.block.entity.BlockEntityType<? extends net.minecraft.world.level.block.entity.ChestBlockEntity>> beType, Properties props) {
        super(type, (ZetaModule) null, beType, props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BBTrappedChestBlockEntity(pos, state);
    }
}
