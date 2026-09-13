package com.teamaurora.bayou_blues.client;

import com.teamaurora.bayou_blues.client.particle.TwigParticle;
import com.teamaurora.bayou_blues.client.renderer.boat.CypressBoatRenderer;
import com.teamaurora.bayou_blues.core.BayouBlues;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands;
import com.teamaurora.bayou_blues.core.registry.BayouBluesEntities;
import com.teamaurora.bayou_blues.core.registry.BayouBluesParticles;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.core.BlockPos;
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
import org.violetmoon.zeta.client.AlikeColorHandler;

@Mod.EventBusSubscriber(modid = BayouBlues.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegister {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setupRenderLayers();
        });
    }

    /**
     * 注册 Better Badlands 移植内容的自定义粒子（碎枝 twig）。
     * 1.16.5 的 {@code ParticleFactoryRegisterEvent} 在 1.20.1 是
     * {@link RegisterParticleProvidersEvent}，粒子类型也于 1.19 起更名
     * （{@code BasicParticleType} → {@code SimpleParticleType}）。
     */
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BayouBluesParticles.TWIG.get(), TwigParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BayouBluesEntities.CYPRESS_BOAT.get(), CypressBoatRenderer::new);

        // 丝柏箱子/陷阱箱：复用 Quark 的 VariantChestRenderer（按方块读
        // quark:quark_variant_chests/cypress/* 贴图，isTrap 区分陷阱箱）
        BlockEntityRenderers.register(com.teamaurora.bayou_blues.core.registry.BayouBluesBlockEntities.CYPRESS_CHEST.get(),
                ctx -> new org.violetmoon.quark.content.building.client.render.be.VariantChestRenderer(ctx, false));
        BlockEntityRenderers.register(com.teamaurora.bayou_blues.core.registry.BayouBluesBlockEntities.CYPRESS_TRAPPED_CHEST.get(),
                ctx -> new org.violetmoon.quark.content.building.client.render.be.VariantChestRenderer(ctx, true));
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();

        // Cypress leaves color
        blockColors.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return BiomeColors.getAverageFoliageColor(world, pos);
            }
            return FoliageColor.getDefaultColor();
        },
                BayouBluesBlocks.CYPRESS_LEAVES.get(),
                BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get(),
                BayouBluesBlocks.CYPRESS_LEAF_CARPET.get());

        // Cypress hedge - Quark 标准染色方式（HedgesModule 同款 AlikeColorHandler）：
        // 树篱永远与其叶子方块（cypress_leaves）取到完全相同的颜色（方块=群系叶色，物品=默认叶色）。
        if (BayouBluesBlocks.CYPRESS_HEDGE != null) {
            blockColors.register(new AlikeColorHandler((HedgeBlock) BayouBluesBlocks.CYPRESS_HEDGE.get(), HedgeBlock::getLeaf),
                    BayouBluesBlocks.CYPRESS_HEDGE.get());
        }

        // Lily colors - water color
        blockColors.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return 2129968; // Water color
            }
            return 7455580;
        },
                BayouBluesBlocks.BLUE_LILY.get(),
                BayouBluesBlocks.LIGHT_GRAY_LILY.get(),
                BayouBluesBlocks.CYAN_LILY.get(),
                BayouBluesBlocks.LIGHT_BLUE_LILY.get(),
                BayouBluesBlocks.MAGENTA_LILY.get(),
                BayouBluesBlocks.PINK_LILY.get(),
                BayouBluesBlocks.PURPLE_LILY.get(),
                BayouBluesBlocks.WHITE_LILY.get());

        // Giant fern grass color
        blockColors.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                BlockState actualState = state;
                BlockPos actualPos = pos;
                if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                    actualPos = pos.below();
                }
                return BiomeColors.getAverageGrassColor(world, actualPos);
            }
            return GrassColor.get(0.5D, 1.0D);
        }, BayouBluesBlocks.GIANT_FERN.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColors itemColors = event.getItemColors();

        // Cypress leaves item color
        itemColors.register((stack, tintIndex) -> FoliageColor.getDefaultColor(),
                BayouBluesBlocks.CYPRESS_LEAVES.get(),
                BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get(),
                BayouBluesBlocks.CYPRESS_LEAF_CARPET.get());

        // Cypress hedge - Quark 标准 AlikeColorHandler：物品形态也与其叶子物品取相同颜色
        if (BayouBluesBlocks.CYPRESS_HEDGE != null) {
            itemColors.register(new AlikeColorHandler(BayouBluesBlocks.CYPRESS_HEDGE.get().asItem(),
                            i -> ((HedgeBlock) i).getLeaf()),
                    BayouBluesBlocks.CYPRESS_HEDGE.get());
        }
        itemColors.register((stack, tintIndex) -> GrassColor.get(0.5D, 1.0D),
                BayouBluesBlocks.GIANT_FERN.get());
    }

    @SuppressWarnings("removal")
    public static void setupRenderLayers() {
        // Cutout mipped
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.HANGING_CYPRESS_LEAVES.get(), RenderType.cutoutMipped());

        // Cutout
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_CYPRESS_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_LEAF_CARPET.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_KNEE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.LARGE_CYPRESS_KNEE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_BRANCH.get(), RenderType.cutout());

        // Quark blocks - only if loaded
        if (BayouBluesBlocks.CYPRESS_LADDER != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_LADDER.get(), RenderType.cutout());
        }
        if (BayouBluesBlocks.CYPRESS_HEDGE != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYPRESS_HEDGE.get(), RenderType.cutout());
        }
        if (BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB != null) {
            ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH_VERTICAL_SLAB.get(), RenderType.cutout());
        }

        // Algae and thatch
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH_SLAB.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.ALGAE_THATCH_STAIRS.get(), RenderType.cutout());

        // Beard moss
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.BEARD_MOSS_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.BEARD_MOSS.get(), RenderType.cutout());

        // Lilies
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.LIGHT_GRAY_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.CYAN_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.LIGHT_BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.MAGENTA_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.PINK_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.PURPLE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.WHITE_LILY.get(), RenderType.cutout());

        // Potted lilies
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_LIGHT_GRAY_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_CYAN_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_LIGHT_BLUE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_MAGENTA_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_PINK_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_PURPLE_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.POTTED_WHITE_LILY.get(), RenderType.cutout());

        // Giant fern
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocks.GIANT_FERN.get(), RenderType.cutout());

        // ── Better Badlands 移植：引火物家族用 cutout（同原模组 ClientRegister）──────
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING_SLAB.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING_STAIRS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BayouBluesBlocksBadlands.KINDLING_VERTICAL_SLAB.get(), RenderType.cutout());
    }
}
