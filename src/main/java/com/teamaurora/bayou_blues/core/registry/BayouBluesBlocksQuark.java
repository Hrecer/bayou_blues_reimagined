package com.teamaurora.bayou_blues.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.bayou_blues.common.block.thatch.ThatchVerticalSlabBlock;
import com.teamaurora.bayou_blues.common.block.WoodPostBlock;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.block.HedgeBlock;
import org.violetmoon.quark.content.building.block.VariantBookshelfBlock;
import org.violetmoon.quark.content.building.block.VariantChestBlock;
import org.violetmoon.quark.content.building.block.VariantTrappedChestBlock;
import org.violetmoon.quark.content.building.block.VerticalSlabBlock;
import org.violetmoon.quark.content.building.module.HedgesModule;
import org.violetmoon.quark.content.building.module.VariantBookshelvesModule;
import org.violetmoon.zeta.module.ZetaModule;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/* ⚠ 给 bayou 群系加新特性时：如果同一 placed feature 也会被 biome_modifier 注入到其他群系
 *（尤其 vanilla swamp），必须像百合一样使用独立副本（patch_lily_*_bayou），否则会与
 * Zeta 的 biome_modifier 占位特性随机形成拓扑环，导致 "Feature order cycle" 崩溃。 */
public class BayouBluesBlocksQuark {

    // 字段声明在 BayouBluesBlocks 中（CYPRESS_BOOKSHELF 等），本类只负责赋值。
    // 注意：绝不能在本类重复声明一套同名字段——那样赋的是镜像，BayouBluesBlocks.*
    // 永远为 null，创造标签页与可燃/堆肥注册的判空会全部失效。

    public static void register() {
        // Quark 是强制依赖（mods.toml mandatory=true），此处是正式内容注册，不是联动兼容层
        BayouBlues.LOGGER.info("Registering Quark-based cypress blocks (Quark is a mandatory dependency)");

        BayouBluesBlocks.CYPRESS_BOOKSHELF = createQuarkFuelBlock("cypress_bookshelf",
                () -> new VariantBookshelfBlock("cypress", quarkModule(VariantBookshelvesModule.class), true, SoundType.WOOD), 300);

        BayouBluesBlocks.VERTICAL_CYPRESS_PLANKS = createBlock("vertical_cypress_planks",
                () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

        BayouBluesBlocks.CYPRESS_VERTICAL_SLAB = createQuarkFuelBlock("cypress_vertical_slab",
                () -> new VerticalSlabBlock(() -> BayouBluesBlocks.CYPRESS_PLANKS.get(), BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)), 150);

        BayouBluesBlocks.CYPRESS_LADDER = createQuarkFuelBlock("cypress_ladder",
                () -> new LadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER).noOcclusion()), 300);

        BayouBluesBlocks.STRIPPED_CYPRESS_POST = createQuarkFuelBlock("stripped_cypress_post",
                () -> new WoodPostBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)), 300);

        BayouBluesBlocks.CYPRESS_POST = createQuarkFuelBlock("cypress_post",
                () -> new WoodPostBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)), 300);

        BayouBluesBlocks.CYPRESS_HEDGE = createQuarkFuelBlock("cypress_hedge",
                () -> new HedgeBlock("cypress_hedge", quarkModule(HedgesModule.class),
                        BayouBluesBlocks.CYPRESS_LEAVES.get(), BayouBluesBlocks.CYPRESS_LEAVES.get()), 300);

        BayouBluesBlocks.GOOSEBERRY_SACK = createBlock("gooseberry_sack",
                () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL)));

        BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB = createQuarkBlock("algae_thatch_vertical_slab",
                () -> new ThatchVerticalSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRASS).noOcclusion()));

        // 丝柏箱子/陷阱箱：复用 Quark 的 VariantChestBlock（module 传 null 跳过 Zeta
        // 自注册，避免构造期竞态），方块实体类型与物品渲染由 BayouBluesBlockEntities /
        // BBChestItem 自持；贴图走 Quark 约定路径 quark:quark_variant_chests/cypress/*。
        // 注意 quarkModule 等调用全部在 supplier 内（RegisterEvent 时才求值，Quark 必已构造完）。
        BayouBluesBlocks.CYPRESS_CHEST = createChestBlock("cypress_chest",
                () -> new com.teamaurora.bayou_blues.common.block.BBChestBlock("cypress",
                        () -> BayouBluesBlockEntities.CYPRESS_CHEST.get(),
                        BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD)),
                () -> BayouBluesBlockEntities.CYPRESS_CHEST.get());
        BayouBluesBlocks.CYPRESS_TRAPPED_CHEST = createChestBlock("cypress_trapped_chest",
                () -> new com.teamaurora.bayou_blues.common.block.BBTrappedChestBlock("cypress",
                        () -> BayouBluesBlockEntities.CYPRESS_TRAPPED_CHEST.get(),
                        BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD)),
                () -> BayouBluesBlockEntities.CYPRESS_TRAPPED_CHEST.get());
    }

    /**
     * Fetches a live module instance from Quark's module manager. Zeta block
     * constructors call {@code module.zeta()}, so a real registered module is
     * required (a bare {@code new ZetaModule()} would NPE on its null zeta field).
     */
    private static ZetaModule quarkModule(Class<? extends ZetaModule> moduleClass) {
        ZetaModule module = Quark.ZETA.modules.get(moduleClass);
        if (module == null) {
            throw new IllegalStateException("Quark module not present: " + moduleClass.getName());
        }
        return module;
    }

    /**
     * 箱子类方块：物品用 {@link BBChestItem}（携带方块实体类型，供客户端 BEWLR 渲染）。
     */
    private static RegistryObject<Block> createChestBlock(String name, Supplier<Block> block,
            Supplier<net.minecraft.world.level.block.entity.BlockEntityType<?>> beType) {
        RegistryObject<Block> blockObj = BayouBluesBlocks.BLOCKS.register(name, block);
        BayouBluesBlocks.ITEMS.register(name, () -> new com.teamaurora.bayou_blues.common.item.BBChestItem(
                blockObj.get(), new Item.Properties(), beType));
        return blockObj;
    }

    private static RegistryObject<Block> createBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = BayouBluesBlocks.BLOCKS.register(name, block);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties()));
        return blockObj;
    }

    private static RegistryObject<Block> createQuarkBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = BayouBluesBlocks.BLOCKS.register(name, block);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties()));
        return blockObj;
    }

    private static RegistryObject<Block> createQuarkFuelBlock(String name, Supplier<Block> block, int burnTime) {
        RegistryObject<Block> blockObj = BayouBluesBlocks.BLOCKS.register(name, block);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties()) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return burnTime;
            }
        });
        return blockObj;
    }
}
