package com.teamaurora.bayou_blues.common.blockentity;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * cypress_beehive 的方块实体。
 * <p>
 * 必须自持 BE 类型：1.20.1 会在方块实体入区时校验
 * {@code getType().isValid(state)}，原版构造把类型写死为
 * {@code BlockEntityType.BEEHIVE}（validBlocks 只含原版蜂巢），
 * 校验不过会报 "Block entity invalid" 并丢弃实体，蜂箱无法存蜂。
 * 原版 BeehiveBlockEntity 没有 setter 也没有带类型的构造器，
 * 这里覆写 getType() 绑定自己的 BE 类型。
 * 仅在 Buzzier Bees 存在时随 cypress_beehive 一起注册。
 */
public class BBBeehiveBlockEntity extends BeehiveBlockEntity {

    public BBBeehiveBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BayouBluesBlockEntities.CYPRESS_BEEHIVE.get();
    }
}
