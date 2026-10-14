package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BayouBluesTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BayouBlues.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BAYOU_BLUES_TAB = TABS.register("bayou_blues", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BayouBluesItems.GOOSEBERRIES.get()))
                    .title(Component.translatable("itemGroup.bayou_blues"))
                    .displayItems(BayouBluesTabs::displayItems)
                    .build());

    private static void displayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        accept(output, BayouBluesItems.CYPRESS_BOAT);
        accept(output, BayouBluesItems.GOOSEBERRIES);
        accept(output, BayouBluesItems.GOOSEBERRY_JUICE);
        accept(output, BayouBluesItems.GOOSEBERRY_PIE);
        accept(output, BayouBluesItems.HONEY_GLAZED_GOOSEBERRIES);
        accept(output, BayouBluesItems.GOOSEBERRY_JAM);
        accept(output, BayouBluesItems.GOOSEBERRY_JAM_COOKIE);
        accept(output, BayouBluesItems.ALGAE);
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
        accept(output, BayouBluesItems.BLUE_LILY);
        accept(output, BayouBluesItems.LIGHT_BLUE_LILY);
        accept(output, BayouBluesItems.CYAN_LILY);
        accept(output, BayouBluesItems.LIGHT_GRAY_LILY);
        accept(output, BayouBluesItems.WHITE_LILY);
        accept(output, BayouBluesItems.MAGENTA_LILY);
        accept(output, BayouBluesItems.PINK_LILY);
        accept(output, BayouBluesItems.PURPLE_LILY);

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
        accept(output, BayouBluesItems.CYPRESS_SIGN);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_CHEST);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_TRAPPED_CHEST);
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH);
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH_SLAB);
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH_STAIRS);
        acceptBlock(output, BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB);
        acceptBlock(output, BayouBluesBlocks.GOOSEBERRY_SACK);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_LEAVES);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_SAPLING);
        acceptBlock(output, BayouBluesBlocks.HANGING_CYPRESS_LEAVES);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_LEAF_CARPET);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_KNEE);
        acceptBlock(output, BayouBluesBlocks.LARGE_CYPRESS_KNEE);
        acceptBlock(output, BayouBluesBlocks.STRIPPED_CYPRESS_KNEE);
        acceptBlock(output, BayouBluesBlocks.STRIPPED_LARGE_CYPRESS_KNEE);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_BRANCH);
        acceptBlock(output, BayouBluesBlocks.BEARD_MOSS);
        acceptBlock(output, BayouBluesBlocks.BEARD_MOSS_BLOCK);
        acceptBlock(output, BayouBluesBlocks.GIANT_FERN);
        acceptBlock(output, BayouBluesBlocks.WATER_IRIS);
        acceptBlock(output, BayouBluesBlocks.CYPRESS_BEEHIVE);

        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING);
        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING_SLAB);
        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING_STAIRS);
        acceptBlock(output, BayouBluesBlocksBadlands.KINDLING_VERTICAL_SLAB);
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
        if (block != null) {
            output.accept(block.get());
        }
    }
}
