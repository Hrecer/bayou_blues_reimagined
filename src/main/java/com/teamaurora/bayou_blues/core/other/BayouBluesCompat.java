package com.teamaurora.bayou_blues.core.other;

import com.teamaurora.bayou_blues.common.block.trees.SmallDarkOakTreeGrower;
import com.teamaurora.bayou_blues.core.BayouBlues;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import com.teamaurora.bayou_blues.core.registry.BayouBluesItems;
import com.teamaurora.bayou_blues.core.util.ModUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;

import java.lang.reflect.Field;

public class BayouBluesCompat {
    public static void registerCompostables() {
        ModUtil.registerCompostable(BayouBluesBlocks.CYPRESS_LEAVES.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesBlocks.CYPRESS_SAPLING.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesBlocks.CYPRESS_LEAF_CARPET.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesItems.GOOSEBERRIES.get(), 0.65F);
        if (BayouBluesBlocks.GOOSEBERRY_SACK != null) {
            ModUtil.registerCompostable(BayouBluesBlocks.GOOSEBERRY_SACK.get(), 1.0F);
        }
        ModUtil.registerCompostable(BayouBluesBlocks.ALGAE.get(), 0.15F);
        ModUtil.registerCompostable(BayouBluesBlocks.ALGAE_THATCH.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.ALGAE_THATCH_SLAB.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.ALGAE_THATCH_STAIRS.get(), 0.65F);
        if (BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB != null) {
            ModUtil.registerCompostable(BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB.get(), 0.65F);
        }
        ModUtil.registerCompostable(BayouBluesBlocks.BEARD_MOSS.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesBlocks.BEARD_MOSS_BLOCK.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.BLUE_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.LIGHT_BLUE_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.CYAN_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.LIGHT_GRAY_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.WHITE_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.MAGENTA_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.PINK_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.PURPLE_LILY.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocks.WATER_IRIS.get(), 0.65F);
        ModUtil.registerCompostable(BayouBluesBlocksBadlands.KINDLING.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesBlocksBadlands.KINDLING_SLAB.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesBlocksBadlands.KINDLING_STAIRS.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesBlocksBadlands.KINDLING_VERTICAL_SLAB.get(), 0.3F);
        ModUtil.registerCompostable(BayouBluesBlocksBadlands.SAGUARO_CACTUS.get(), 0.5F);
        ModUtil.registerCompostable(BayouBluesBlocksBadlands.SMALL_SAGUARO_CACTUS.get(), 0.5F);
        ModUtil.registerCompostable(BayouBluesItems.SAGUARO_FLOWER.get(), 0.5F);
    }

    /**
     * 替换原版深色橡木树苗的 TreeGrower（见 {@link SmallDarkOakTreeGrower} 的类注释）：
     * 1.20.1 对单个树苗不会触发 SaplingGrowTreeEvent，只能从 grower 源头接管。
     */
    public static void replaceDarkOakGrower() {
        try {
            for (Field field : SaplingBlock.class.getDeclaredFields()) {
                if (AbstractTreeGrower.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    field.set(Blocks.DARK_OAK_SAPLING, new SmallDarkOakTreeGrower());
                    return;
                }
            }
            BayouBlues.LOGGER.error("No AbstractTreeGrower field found on SaplingBlock; small dark oaks will not grow from single saplings");
        } catch (Throwable t) {
            BayouBlues.LOGGER.error("Failed to replace dark oak sapling grower", t);
        }
    }

    public static void registerFlammables() {
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_LEAVES.get(), 30, 60);
        ModUtil.registerFlammable(BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get(), 30, 60);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_LEAF_CARPET.get(), 30, 60);
        if (BayouBluesBlocks.CYPRESS_HEDGE != null) {
            ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_HEDGE.get(), 5, 20);
        }
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_LOG.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_WOOD.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.STRIPPED_CYPRESS_LOG.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.STRIPPED_CYPRESS_WOOD.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_PLANKS.get(), 5, 20);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_SLAB.get(), 5, 20);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_STAIRS.get(), 5, 20);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_FENCE.get(), 5, 20);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_FENCE_GATE.get(), 5, 20);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_KNEE.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.LARGE_CYPRESS_KNEE.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.STRIPPED_CYPRESS_KNEE.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.STRIPPED_LARGE_CYPRESS_KNEE.get(), 5, 5);
        ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_BRANCH.get(), 60, 100);
        if (BayouBluesBlocks.VERTICAL_CYPRESS_PLANKS != null) {
            ModUtil.registerFlammable(BayouBluesBlocks.VERTICAL_CYPRESS_PLANKS.get(), 5, 20);
        }
        if (BayouBluesBlocks.CYPRESS_VERTICAL_SLAB != null) {
            ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_VERTICAL_SLAB.get(), 5, 20);
        }
        if (BayouBluesBlocks.CYPRESS_BOOKSHELF != null) {
            ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_BOOKSHELF.get(), 30, 20);
        }
        if (BayouBluesBlocks.CYPRESS_POST != null) {
            ModUtil.registerFlammable(BayouBluesBlocks.CYPRESS_POST.get(), 5, 20);
        }
        if (BayouBluesBlocks.STRIPPED_CYPRESS_POST != null) {
            ModUtil.registerFlammable(BayouBluesBlocks.STRIPPED_CYPRESS_POST.get(), 5, 20);
        }
        ModUtil.registerFlammable(BayouBluesBlocks.ALGAE_THATCH.get(), 60, 20);
        ModUtil.registerFlammable(BayouBluesBlocks.ALGAE_THATCH_SLAB.get(), 60, 20);
        ModUtil.registerFlammable(BayouBluesBlocks.ALGAE_THATCH_STAIRS.get(), 60, 20);
        if (BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB != null) {
            ModUtil.registerFlammable(BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB.get(), 60, 20);
        }
        ModUtil.registerFlammable(BayouBluesBlocks.BEARD_MOSS.get(), 15, 100);
        ModUtil.registerFlammable(BayouBluesBlocks.BEARD_MOSS_BLOCK.get(), 15, 100);
        ModUtil.registerFlammable(BayouBluesBlocks.GIANT_FERN.get(), 60, 100);
        ModUtil.registerFlammable(BayouBluesBlocks.WATER_IRIS.get(), 60, 100);
    }
}
