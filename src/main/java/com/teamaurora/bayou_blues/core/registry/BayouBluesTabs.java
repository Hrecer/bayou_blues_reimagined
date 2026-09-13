package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 全部新增内容统一收纳在本模组自定义标签页中，不再分散进原版标签页。
 * 唯一例外：丝柏树叶仍会加入原版"自然方块"页——它只是 Zeta 自动插入
 * 丝柏树叶篱（HedgeBlock 构造器硬编码"插到叶子旁边"）所需的定位锚点。
 */
public class BayouBluesTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BayouBlues.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BAYOU_BLUES_TAB = TABS.register("bayou_blues", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BayouBluesItems.GOOSEBERRIES.get()))
                    .title(Component.translatable("itemGroup.bayou_blues"))
                    .displayItems(BayouBluesTabs::displayItems)
                    .build());

    private static void displayItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        // 物品：船与食物
        accept(output, BayouBluesItems.CYPRESS_BOAT);
        accept(output, BayouBluesItems.GOOSEBERRIES);
        accept(output, BayouBluesItems.GOOSEBERRY_JUICE);
        accept(output, BayouBluesItems.GOOSEBERRY_PIE);
        accept(output, BayouBluesItems.HONEY_GLAZED_GOOSEBERRIES);
        accept(output, BayouBluesItems.GOOSEBERRY_JAM);
        accept(output, BayouBluesItems.GOOSEBERRY_JAM_COOKIE);
        accept(output, BayouBluesItems.ALGAE);

        // 物品：沼泽料理
        accept(output, BayouBluesItems.ALGAE_SANDWICH);
        accept(output, BayouBluesItems.ALGAE_SOUP);
        accept(output, BayouBluesItems.BAYOU_FLOWER_TEA);
        accept(output, BayouBluesItems.BAYOU_PIE);
        accept(output, BayouBluesItems.BAYOU_SALAD);
        accept(output, BayouBluesItems.BEARD_MOSS_JUICE);
        accept(output, BayouBluesItems.COOKED_ALGAE);
        accept(output, BayouBluesItems.COOKED_GOOSEBERRIES);
        accept(output, BayouBluesItems.CYPRESS_SOUP);
        accept(output, BayouBluesItems.FERN_SALAD);
        accept(output, BayouBluesItems.GOOSEBERRIES_CYPRESS_TEA);
        accept(output, BayouBluesItems.GOOSEBERRIES_MILK_TEA);
        accept(output, BayouBluesItems.GOOSEBERRIES_POPSICLE);
        accept(output, BayouBluesItems.GOOSEBERRIES_TOMATO_JAM);
        accept(output, BayouBluesItems.HONEY_GOOSEBERRIES_COOKIE);
        accept(output, BayouBluesItems.HONEY_LILY_TEA);
        accept(output, BayouBluesItems.LILY_LILY_PAD_SOUP);

        // 物品：睡莲
        accept(output, BayouBluesItems.BLUE_LILY);
        accept(output, BayouBluesItems.LIGHT_BLUE_LILY);
        accept(output, BayouBluesItems.CYAN_LILY);
        accept(output, BayouBluesItems.LIGHT_GRAY_LILY);
        accept(output, BayouBluesItems.WHITE_LILY);
        accept(output, BayouBluesItems.MAGENTA_LILY);
        accept(output, BayouBluesItems.PINK_LILY);
        accept(output, BayouBluesItems.PURPLE_LILY);

        // 丝柏木系（含 Quark 体系方块）
        acceptBlock(output, BayouBluesBlocks.CYPRESS_LOG);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_WOOD);
        acceptBlock(output, BayouBluesBlocks.STRIPPED_CYPRESS_LOG);
        acceptBlock(output, BayouBluesBlocks.STRIPPED_CYPRESS_WOOD);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_PLANKS);
        acceptBlock(output, BayouBluesBlocks.VERTICAL_CYPRESS_PLANKS);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_SLAB);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_VERTICAL_SLAB);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_STAIRS);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_FENCE);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_FENCE_GATE);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_DOOR);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_TRAPDOOR);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_PRESSURE_PLATE);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_BUTTON);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_LADDER);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_POST);
        acceptBlock(output, BayouBluesBlocks.STRIPPED_CYPRESS_POST);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_BOOKSHELF);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_SIGN);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_CHEST);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_TRAPPED_CHEST);

        // 海藻茅草系
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH);
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH_SLAB);
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH_STAIRS);
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB);
        acceptBlock(output, BayouBluesBlocks.GOOSEBERRY_SACK);

        // 植物与装饰
        acceptBlock(output, BayouBluesBlocks.CYPRESS_LEAVES);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_SAPLING);
        acceptBlock(output, BayouBluesBlocks.HANGING_CYPRESS_LEAVES);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_LEAF_CARPET);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_KNEE);
        acceptBlock(output, BayouBluesBlocks.LARGE_CYPRESS_KNEE);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_BRANCH);
        acceptBlock(output, BayouBluesBlocks.BEARD_MOSS);
        acceptBlock(output, BayouBluesBlocks.BEARD_MOSS_BLOCK);
        acceptBlock(output, BayouBluesBlocks.GIANT_FERN);

        // Buzzier Bees 联动（可选）
        acceptBlock(output, BayouBluesBlocks.CYPRESS_BEEHIVE);

        // ── Better Badlands 移植内容 ──────────────────────────────────────────
        // 引火物（Dead Bush Thatch）
        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING);
        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING_SLAB);
        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING_STAIRS);
        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING_VERTICAL_SLAB);

        // 陶瓦灯（16 色 + 素色）。light_source 是隐藏辅助方块，无物品，不进标签页。
        acceptBlock(output, BayouBluesBlocksBadlands.TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.WHITE_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.LIGHT_GRAY_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.GRAY_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.BLACK_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.BROWN_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.RED_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.ORANGE_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.YELLOW_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.LIME_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.GREEN_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.CYAN_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.LIGHT_BLUE_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.BLUE_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.PURPLE_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.MAGENTA_TERRACOTTA_LAMP);
        acceptBlock(output, BayouBluesBlocksBadlands.PINK_TERRACOTTA_LAMP);
    }

    private static void accept(CreativeModeTab.Output output, RegistryObject<Item> item) {
        output.accept(item.get());
    }

    private static void acceptBlock(CreativeModeTab.Output output, RegistryObject<Block> block) {
        // null 守卫：Quark/Buzzier Bees 体系字段由 BayouBluesBlocksQuark / 联动注册赋值
        if (block != null) {
            output.accept(block.get());
        }
    }
}
