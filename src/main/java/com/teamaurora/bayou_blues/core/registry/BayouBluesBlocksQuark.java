package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.block.BBChestBlock;
import com.teamaurora.bayou_blues.common.block.BBTrappedChestBlock;
import com.teamaurora.bayou_blues.common.block.WoodPostBlock;
import com.teamaurora.bayou_blues.common.block.thatch.ThatchVerticalSlabBlock;
import com.teamaurora.bayou_blues.common.item.BBChestItem;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.block.HedgeBlock;
import org.violetmoon.quark.content.building.block.VariantBookshelfBlock;
import org.violetmoon.quark.content.building.block.VerticalSlabBlock;
import org.violetmoon.quark.content.building.module.HedgesModule;
import org.violetmoon.quark.content.building.module.VariantBookshelvesModule;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.function.Supplier;

public class BayouBluesBlocksQuark {
    public static void register() {
        BayouBlues.LOGGER.info("Registering Quark-based cypress blocks (Quark is a mandatory dependency)");
        BayouBluesBlocks.CYPRESS_BOOKSHELF = createQuarkFuelBlock("cypress_bookshelf", () -> new VariantBookshelfBlock("cypress", quarkModule(VariantBookshelvesModule.class), true, SoundType.WOOD), 300);
        BayouBluesBlocks.VERTICAL_CYPRESS_PLANKS = createBlock("vertical_cypress_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        BayouBluesBlocks.CYPRESS_VERTICAL_SLAB = createQuarkFuelBlock("cypress_vertical_slab", () -> new VerticalSlabBlock(() -> BayouBluesBlocks.CYPRESS_PLANKS.get(), BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)), 150);
        BayouBluesBlocks.CYPRESS_LADDER = createQuarkFuelBlock("cypress_ladder", () -> new LadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER).noOcclusion()), 300);
        BayouBluesBlocks.STRIPPED_CYPRESS_POST = createQuarkFuelBlock("stripped_cypress_post", () -> new WoodPostBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)), 300);
        BayouBluesBlocks.CYPRESS_POST = createQuarkFuelBlock("cypress_post", () -> new WoodPostBlock(BayouBluesBlocks.STRIPPED_CYPRESS_POST, BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)), 300);
        BayouBluesBlocks.CYPRESS_HEDGE = createQuarkFuelBlock("cypress_hedge", () -> new HedgeBlock("cypress_hedge", quarkModule(HedgesModule.class), BayouBluesBlocks.CYPRESS_LEAVES.get(), BayouBluesBlocks.CYPRESS_LEAVES.get()), 300);
        BayouBluesBlocks.GOOSEBERRY_SACK = createBlock("gooseberry_sack", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(0.5F).sound(SoundType.WOOL)));
        BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB = createQuarkBlock("algae_thatch_vertical_slab", () -> new ThatchVerticalSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SCAFFOLDING).noOcclusion()));
        BayouBluesBlocks.CYPRESS_CHEST = createChestBlock("cypress_chest", () -> new BBChestBlock("cypress", () -> BayouBluesBlockEntities.CYPRESS_CHEST.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD)), () -> BayouBluesBlockEntities.CYPRESS_CHEST.get());
        BayouBluesBlocks.CYPRESS_TRAPPED_CHEST = createChestBlock("cypress_trapped_chest", () -> new BBTrappedChestBlock("cypress", () -> BayouBluesBlockEntities.CYPRESS_TRAPPED_CHEST.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD)), () -> BayouBluesBlockEntities.CYPRESS_TRAPPED_CHEST.get());
    }

    private static ZetaModule quarkModule(Class<? extends ZetaModule> clazz) {
        ZetaModule module = Quark.ZETA.modules.get(clazz);
        if (module == null) {
            throw new IllegalStateException("Missing Quark module " + clazz.getName());
        }
        return module;
    }

    private static RegistryObject<Block> createChestBlock(String name, Supplier<Block> supplier, Supplier<BlockEntityType<?>> tileSupplier) {
        RegistryObject<Block> block = BayouBluesBlocks.BLOCKS.register(name, supplier);
        BayouBluesBlocks.ITEMS.register(name, () -> new BBChestItem(block.get(), new Item.Properties(), tileSupplier));
        return block;
    }

    private static RegistryObject<Block> createBlock(String name, Supplier<Block> supplier) {
        RegistryObject<Block> block = BayouBluesBlocks.BLOCKS.register(name, supplier);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Block> createQuarkBlock(String name, Supplier<Block> supplier) {
        RegistryObject<Block> block = BayouBluesBlocks.BLOCKS.register(name, supplier);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Block> createQuarkFuelBlock(String name, Supplier<Block> supplier, int burnTime) {
        RegistryObject<Block> block = BayouBluesBlocks.BLOCKS.register(name, supplier);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return burnTime;
            }
        });
        return block;
    }
}
