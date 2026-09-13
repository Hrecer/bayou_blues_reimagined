package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.teamaurora.bayou_blues.common.blockentity.BBBeehiveBlockEntity;
import com.teamaurora.bayou_blues.common.blockentity.BBChestBlockEntity;
import com.teamaurora.bayou_blues.common.blockentity.BBTrappedChestBlockEntity;

/**
 * 丝柏箱子/陷阱箱的方块实体类型。
 * <p>
 * 方块本体复用 Quark 的 {@link org.violetmoon.quark.content.building.block.VariantChestBlock}
 * （module 传 null 跳过 Zeta 自注册），但方块实体类型必须自持：Quark 的
 * variant_chest BE 类型在 ZRegister.Post 时按当时已登记的方块列表构建，
 * 而外部模组的构造与 Quark 构造在 FML 下并行执行，存在竞态，不能依赖。
 */
public class BayouBluesBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BayouBlues.MOD_ID);

    public static final RegistryObject<BlockEntityType<BBChestBlockEntity>> CYPRESS_CHEST =
            BLOCK_ENTITIES.register("cypress_chest", () -> BlockEntityType.Builder.of(
                    BBChestBlockEntity::new, BayouBluesBlocks.CYPRESS_CHEST.get()).build(null));

    public static final RegistryObject<BlockEntityType<BBTrappedChestBlockEntity>> CYPRESS_TRAPPED_CHEST =
            BLOCK_ENTITIES.register("cypress_trapped_chest", () -> BlockEntityType.Builder.of(
                    BBTrappedChestBlockEntity::new, BayouBluesBlocks.CYPRESS_TRAPPED_CHEST.get()).build(null));

    public static RegistryObject<BlockEntityType<BBBeehiveBlockEntity>> CYPRESS_BEEHIVE;

    /**
     * cypress_beehive 的 BE 类型，仅当 Buzzier Bees 存在时由
     * {@link BayouBluesBlocksBuzzierBees#register()} 调用注册：
     * DeferredRegister 已在构造阶段挂上事件总线，此时再补登记条目
     * 仍会在 RegistryEvent.Register 之前完成，BB 缺席时不注册即可。
     */
    public static RegistryObject<BlockEntityType<BBBeehiveBlockEntity>> registerCypressBeehive() {
        return CYPRESS_BEEHIVE = BLOCK_ENTITIES.register("cypress_beehive", () -> BlockEntityType.Builder.of(
                BBBeehiveBlockEntity::new, BayouBluesBlocks.CYPRESS_BEEHIVE.get()).build(null));
    }
}
