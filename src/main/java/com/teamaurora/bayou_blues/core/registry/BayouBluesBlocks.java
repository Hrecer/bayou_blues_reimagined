package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.block.*;
import com.teamaurora.bayou_blues.common.block.trees.CypressTree;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

public class BayouBluesBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BayouBlues.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BayouBlues.MOD_ID);

    public static final RegistryObject<Block> STRIPPED_CYPRESS_LOG = createBlock("stripped_cypress_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).mapColor(MapColor.WOOD)));
    public static final RegistryObject<Block> STRIPPED_CYPRESS_WOOD = createBlock("stripped_cypress_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.WOOD)));
    public static final RegistryObject<Block> CYPRESS_LOG = createBlock("cypress_log", () -> new BBBlocks.LogBlock(() -> STRIPPED_CYPRESS_LOG.get(), BlockBehaviour.Properties.copy(Blocks.OAK_LOG).mapColor(s -> MapColor.WOOD)));
    public static final RegistryObject<Block> CYPRESS_WOOD = createBlock("cypress_wood", () -> new BBBlocks.WoodBlock(() -> STRIPPED_CYPRESS_WOOD.get(), BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).mapColor(s -> MapColor.WOOD)));
    public static final RegistryObject<Block> CYPRESS_PLANKS = createBlock("cypress_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).mapColor(MapColor.WOOD)));
    public static final RegistryObject<Block> CYPRESS_SLAB = createBlock("cypress_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> CYPRESS_STAIRS = createBlock("cypress_stairs", () -> new StairBlock(CYPRESS_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> CYPRESS_PRESSURE_PLATE = createBlock("cypress_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));
    public static final RegistryObject<Block> CYPRESS_BUTTON = createBlock("cypress_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> CYPRESS_FENCE = createFuelBlock("cypress_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)), 300);
    public static final RegistryObject<Block> CYPRESS_FENCE_GATE = createFuelBlock("cypress_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), BayouBluesWoodTypes.CYPRESS), 300);
    public static final RegistryObject<Block> CYPRESS_DOOR = createBlock("cypress_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).noOcclusion(), BayouBluesWoodTypes.CYPRESS_BLOCK_SET));
    public static final RegistryObject<Block> CYPRESS_TRAPDOOR = createBlock("cypress_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).noOcclusion(), BayouBluesWoodTypes.CYPRESS_BLOCK_SET));
    public static final RegistryObject<Block> CYPRESS_SIGN = createBlockNoItem("cypress_sign", () -> new BBSignBlocks.StandingSign(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), BayouBluesWoodTypes.CYPRESS));
    public static final RegistryObject<Block> CYPRESS_WALL_SIGN = createBlockNoItem("cypress_wall_sign", () -> new BBSignBlocks.WallSign(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN), BayouBluesWoodTypes.CYPRESS));
    public static final RegistryObject<Block> CYPRESS_LEAVES = createBlock("cypress_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> CYPRESS_SAPLING = createBlock("cypress_sapling", () -> new SaplingBlock(new CypressTree(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> POTTED_CYPRESS_SAPLING = createBlockNoItem("potted_cypress_sapling", () -> new FlowerPotBlock(CYPRESS_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_OAK_SAPLING)));
    public static final RegistryObject<Block> CYPRESS_LEAF_CARPET = createBlock("cypress_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> HANGING_CYPRESS_LEAVES = createBlock("hanging_cypress_leaves", () -> new HangingCypressLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> STRIPPED_CYPRESS_KNEE = createBlock("stripped_cypress_knee", () -> new CypressKneeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> STRIPPED_LARGE_CYPRESS_KNEE = createBlock("stripped_large_cypress_knee", () -> new DoubleCypressKneeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> CYPRESS_KNEE = createBlock("cypress_knee", () -> new CypressKneeBlock(STRIPPED_CYPRESS_KNEE, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> LARGE_CYPRESS_KNEE = createBlock("large_cypress_knee", () -> new DoubleCypressKneeBlock(STRIPPED_LARGE_CYPRESS_KNEE, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> CYPRESS_BRANCH = createBlock("cypress_branch", () -> new CypressBranchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).instabreak().noCollission().sound(SoundType.SWEET_BERRY_BUSH)));

    public static final RegistryObject<Block> BLUE_LILY = createBlockNoItem("blue_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> LIGHT_GRAY_LILY = createBlockNoItem("light_gray_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> CYAN_LILY = createBlockNoItem("cyan_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> LIGHT_BLUE_LILY = createBlockNoItem("light_blue_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> MAGENTA_LILY = createBlockNoItem("magenta_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> PINK_LILY = createBlockNoItem("pink_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> PURPLE_LILY = createBlockNoItem("purple_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> WHITE_LILY = createBlockNoItem("white_lily", () -> new LilyFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_OF_THE_VALLEY)));

    public static final RegistryObject<Block> POTTED_BLUE_LILY = createBlockNoItem("potted_blue_lily", () -> new FlowerPotBlock(BLUE_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> POTTED_LIGHT_GRAY_LILY = createBlockNoItem("potted_light_gray_lily", () -> new FlowerPotBlock(LIGHT_GRAY_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> POTTED_CYAN_LILY = createBlockNoItem("potted_cyan_lily", () -> new FlowerPotBlock(CYAN_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> POTTED_LIGHT_BLUE_LILY = createBlockNoItem("potted_light_blue_lily", () -> new FlowerPotBlock(LIGHT_BLUE_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> POTTED_MAGENTA_LILY = createBlockNoItem("potted_magenta_lily", () -> new FlowerPotBlock(MAGENTA_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> POTTED_PINK_LILY = createBlockNoItem("potted_pink_lily", () -> new FlowerPotBlock(PINK_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> POTTED_PURPLE_LILY = createBlockNoItem("potted_purple_lily", () -> new FlowerPotBlock(PURPLE_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));
    public static final RegistryObject<Block> POTTED_WHITE_LILY = createBlockNoItem("potted_white_lily", () -> new FlowerPotBlock(WHITE_LILY.get(), BlockBehaviour.Properties.copy(Blocks.POTTED_LILY_OF_THE_VALLEY)));

    public static final RegistryObject<Block> ALGAE = createBlockNoItem("algae", () -> new AlgaeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noLootTable().sound(SoundType.LILY_PAD).noOcclusion().noCollission()));
    public static final RegistryObject<Block> ALGAE_THATCH = createBlock("algae_thatch", () -> new BBBlocks.ThatchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SCAFFOLDING).noOcclusion()));
    public static final RegistryObject<Block> ALGAE_THATCH_SLAB = createBlock("algae_thatch_slab", () -> new BBBlocks.ThatchSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SCAFFOLDING).noOcclusion()));
    public static final RegistryObject<Block> ALGAE_THATCH_STAIRS = createBlock("algae_thatch_stairs", () -> new BBBlocks.ThatchStairBlock(ALGAE_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SCAFFOLDING).noOcclusion()));
    public static final RegistryObject<Block> BEARD_MOSS = createFuelBlock("beard_moss", () -> new BeardMossBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noLootTable().sound(SoundType.MOSS).noOcclusion().noCollission().instabreak()), 800);
    public static final RegistryObject<Block> BEARD_MOSS_BLOCK = createFuelBlock("beard_moss_block", () -> new BeardMossBlockBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.1F).sound(SoundType.MOSS).noOcclusion()), 800);
    public static final RegistryObject<Block> GIANT_FERN = createBlock("giant_fern", () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.LARGE_FERN)));
    public static final RegistryObject<Block> WATER_IRIS = createBlock("water_iris", () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.LARGE_FERN)));

    public static RegistryObject<Block> CYPRESS_BOOKSHELF;
    public static RegistryObject<Block> VERTICAL_CYPRESS_PLANKS;
    public static RegistryObject<Block> CYPRESS_VERTICAL_SLAB;
    public static RegistryObject<Block> CYPRESS_LADDER;
    public static RegistryObject<Block> STRIPPED_CYPRESS_POST;
    public static RegistryObject<Block> CYPRESS_POST;
    public static RegistryObject<Block> CYPRESS_HEDGE;
    public static RegistryObject<Block> GOOSEBERRY_SACK;
    public static RegistryObject<Block> ALGAE_THATCH_VERTICAL_SLAB;
    public static RegistryObject<Block> CYPRESS_CHEST;
    public static RegistryObject<Block> CYPRESS_TRAPPED_CHEST;
    public static RegistryObject<Block> CYPRESS_BEEHIVE;

    private static RegistryObject<Block> createBlock(String name, Supplier<Block> supplier) {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Block> createBlockNoItem(String name, Supplier<Block> supplier) {
        return BLOCKS.register(name, supplier);
    }

    public static void registerFlowerPots() {
        for (RegistryObject<Block> block : BLOCKS.getEntries()) {
            if (block.get() instanceof FlowerPotBlock flowerPot) {
                ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(flowerPot.getContent())), block);
            }
        }
    }

    private static RegistryObject<Block> createFuelBlock(String name, Supplier<Block> supplier, int burnTime) {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return burnTime;
            }
        });
        return block;
    }

    public static void registerCompatBlocks() {
        BayouBluesBlocksQuark.register();
        if (ModList.get().isLoaded("buzzier_bees")) {
            try {
                Class<?> clazz = Class.forName("com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBuzzierBees");
                Method method = clazz.getMethod("register");
                method.invoke(null);
                CYPRESS_BEEHIVE = (RegistryObject<Block>) clazz.getField("CYPRESS_BEEHIVE").get(null);
                BayouBlues.LOGGER.info("Buzzier Bees compatibility blocks registered successfully");
            } catch (Exception e) {
                BayouBlues.LOGGER.error("Failed to register Buzzier Bees compatibility blocks", e);
            }
        }
    }
}
