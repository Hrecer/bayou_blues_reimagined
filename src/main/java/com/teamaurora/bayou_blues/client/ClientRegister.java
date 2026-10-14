package com.teamaurora.bayou_blues.client;

import com.teamaurora.bayou_blues.client.particle.TwigParticle;
import com.teamaurora.bayou_blues.client.renderer.boat.CypressBoatRenderer;
import com.teamaurora.bayou_blues.core.BayouBlues;
import com.teamaurora.bayou_blues.core.registry.*;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.violetmoon.quark.content.building.block.HedgeBlock;
import org.violetmoon.quark.content.building.client.render.be.VariantChestRenderer;
import org.violetmoon.zeta.client.AlikeColorHandler;

@Mod.EventBusSubscriber(modid = BayouBlues.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegister {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setupRenderLayers();
            Sheets.addWoodType(BayouBluesWoodTypes.CYPRESS);
        });
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BayouBluesParticles.TWIG.get(), TwigParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BayouBluesEntities.CYPRESS_BOAT.get(), CypressBoatRenderer::new);
        BlockEntityRenderers.register(BayouBluesBlockEntities.CYPRESS_CHEST.get(), ctx -> new VariantChestRenderer(ctx, false));
        BlockEntityRenderers.register(BayouBluesBlockEntities.CYPRESS_TRAPPED_CHEST.get(), ctx -> new VariantChestRenderer(ctx, true));
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();
        blockColors.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.getDefaultColor(),
                BayouBluesBlocks.CYPRESS_LEAVES.get(),
                BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get(),
                BayouBluesBlocks.CYPRESS_LEAF_CARPET.get());

        if (BayouBluesBlocks.CYPRESS_HEDGE != null) {
            blockColors.register(new AlikeColorHandler((HedgeBlock) BayouBluesBlocks.CYPRESS_HEDGE.get(), b -> ((HedgeBlock) b).getLeaf()),
                    BayouBluesBlocks.CYPRESS_HEDGE.get());
        }

        blockColors.register((state, level, pos, tintIndex) -> level != null && pos != null ? 2129968 : 7455580,
                BayouBluesBlocks.BLUE_LILY.get(),
                BayouBluesBlocks.LIGHT_GRAY_LILY.get(),
                BayouBluesBlocks.CYAN_LILY.get(),
                BayouBluesBlocks.LIGHT_BLUE_LILY.get(),
                BayouBluesBlocks.MAGENTA_LILY.get(),
                BayouBluesBlocks.PINK_LILY.get(),
                BayouBluesBlocks.PURPLE_LILY.get(),
                BayouBluesBlocks.WHITE_LILY.get());

        blockColors.register((state, level, pos, tintIndex) -> {
            if (level != null && pos != null) {
                BlockPos targetPos = state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
                return BiomeColors.getAverageGrassColor(level, targetPos);
            }
            return GrassColor.get(0.5D, 1.0D);
        }, BayouBluesBlocks.GIANT_FERN.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> FoliageColor.getDefaultColor(),
                BayouBluesBlocks.CYPRESS_LEAVES.get(),
                BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get(),
                BayouBluesBlocks.CYPRESS_LEAF_CARPET.get());

        if (BayouBluesBlocks.CYPRESS_HEDGE != null) {
            itemColors.register(new AlikeColorHandler(BayouBluesBlocks.CYPRESS_HEDGE.get().asItem(), b -> ((HedgeBlock) b).getLeaf()),
                    BayouBluesBlocks.CYPRESS_HEDGE.get());
        }

        itemColors.register((stack, tintIndex) -> GrassColor.get(0.5D, 1.0D),
                BayouBluesBlocks.GIANT_FERN.get());
    }

    @SuppressWarnings("removal")
    public static void setupRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_CYPRESS_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_LEAF_CARPET.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_KNEE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.LARGE_CYPRESS_KNEE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.STRIPPED_CYPRESS_KNEE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.STRIPPED_LARGE_CYPRESS_KNEE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.WATER_IRIS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_BRANCH.get(), RenderType.cutout());
        if (BayouBluesBlocks.CYPRESS_LADDER != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_LADDER.get(), RenderType.cutout());
        }
        if (BayouBluesBlocks.CYPRESS_POST != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_POST.get(), RenderType.cutout());
        }
        if (BayouBluesBlocks.STRIPPED_CYPRESS_POST != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.STRIPPED_CYPRESS_POST.get(), RenderType.cutout());
        }
        if (BayouBluesBlocks.CYPRESS_HEDGE != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_HEDGE.get(), RenderType.cutout());
        }
        if (BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB.get(), RenderType.cutout());
        }
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH_SLAB.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH_STAIRS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.BEARD_MOSS_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.BEARD_MOSS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.LIGHT_GRAY_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYAN_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.LIGHT_BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.MAGENTA_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.PINK_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.PURPLE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.WHITE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_LIGHT_GRAY_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_CYAN_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_LIGHT_BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_MAGENTA_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_PINK_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_PURPLE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_WHITE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.GIANT_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING_SLAB.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING_STAIRS.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING_VERTICAL_SLAB.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.SAGUARO_SPROUT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.POTTED_SAGUARO_SPROUT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.SAGUARO_FLOWER.get(), RenderType.cutout());
    }
}
