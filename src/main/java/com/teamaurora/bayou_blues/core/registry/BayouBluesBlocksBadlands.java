package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.block.badlands.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class BayouBluesBlocksBadlands {
    public static final RegistryObject<Block> KINDLING = createFuelBlock("kindling", () -> new KindlingBlock(kindlingProperties()), 300);
    public static final RegistryObject<Block> KINDLING_SLAB = createFuelBlock("kindling_slab", () -> new KindlingSlabBlock(kindlingProperties()), 150);
    public static final RegistryObject<Block> KINDLING_STAIRS = createFuelBlock("kindling_stairs", () -> new KindlingStairsBlock(KINDLING.get().defaultBlockState(), kindlingProperties()), 300);
    public static final RegistryObject<Block> KINDLING_VERTICAL_SLAB = createFuelBlock("kindling_vertical_slab", () -> new KindlingVerticalSlabBlock(kindlingProperties()), 150);

    public static final RegistryObject<Block> TERRACOTTA_LAMP_LIGHT_SOURCE = createBlockNoItem("light_source", () -> new MagicInvisibleLightBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NONE).lightLevel(s -> 13).noLootTable().noOcclusion().noCollission().instabreak().replaceable()
    ));

    public static final RegistryObject<Block> TERRACOTTA_LAMP = createBlock("terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> BLACK_TERRACOTTA_LAMP = createBlock("black_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> BLUE_TERRACOTTA_LAMP = createBlock("blue_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> BROWN_TERRACOTTA_LAMP = createBlock("brown_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> CYAN_TERRACOTTA_LAMP = createBlock("cyan_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> GRAY_TERRACOTTA_LAMP = createBlock("gray_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> GREEN_TERRACOTTA_LAMP = createBlock("green_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> LIGHT_BLUE_TERRACOTTA_LAMP = createBlock("light_blue_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> LIGHT_GRAY_TERRACOTTA_LAMP = createBlock("light_gray_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> LIME_TERRACOTTA_LAMP = createBlock("lime_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> MAGENTA_TERRACOTTA_LAMP = createBlock("magenta_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> ORANGE_TERRACOTTA_LAMP = createBlock("orange_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> PINK_TERRACOTTA_LAMP = createBlock("pink_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> PURPLE_TERRACOTTA_LAMP = createBlock("purple_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> RED_TERRACOTTA_LAMP = createBlock("red_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> WHITE_TERRACOTTA_LAMP = createBlock("white_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> YELLOW_TERRACOTTA_LAMP = createBlock("yellow_terracotta_lamp", () -> new TerracottaLampBlock(lampProperties()));

    private BayouBluesBlocksBadlands() {}

    private static BlockBehaviour.Properties kindlingProperties() {
        return BlockBehaviour.Properties.copy(Blocks.SCAFFOLDING)
                .mapColor(MapColor.COLOR_YELLOW)
                .strength(0.5F)
                .sound(SoundType.SCAFFOLDING)
                .noOcclusion()
                .noCollission();
    }

    private static BlockBehaviour.Properties lampProperties() {
        return BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).lightLevel(s -> 15);
    }

    public static void bootstrap() {}

    private static RegistryObject<Block> createBlock(String name, Supplier<Block> supplier) {
        RegistryObject<Block> block = BayouBluesBlocks.BLOCKS.register(name, supplier);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Block> createFuelBlock(String name, Supplier<Block> supplier, int burnTime) {
        RegistryObject<Block> block = BayouBluesBlocks.BLOCKS.register(name, supplier);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return burnTime;
            }
        });
        return block;
    }

    private static RegistryObject<Block> createBlockNoItem(String name, Supplier<Block> supplier) {
        return BayouBluesBlocks.BLOCKS.register(name, supplier);
    }
}
