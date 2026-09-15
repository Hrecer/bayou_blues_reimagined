package com.teamaurora.bayou_blues.core;

import com.teamaurora.bayou_blues.core.other.BayouBluesCompat;
import com.teamaurora.bayou_blues.core.other.CypressBoatDispenseBehavior;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBiomes;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesEntities;
import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import com.teamaurora.bayou_blues.core.registry.BayouBluesItems;
import com.teamaurora.bayou_blues.core.registry.BayouBluesTabs;
import com.teamaurora.bayou_blues.core.terrablender.BayouBluesTerraBlender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("removal")
@Mod(BayouBlues.MOD_ID)
public class BayouBlues {
    public static final String MOD_ID = "bayou_blues";
    public static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("removal")
    public BayouBlues() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Register Blocks and Items
        BayouBluesBlocks.BLOCKS.register(modEventBus);
        BayouBluesBlocks.ITEMS.register(modEventBus);
        BayouBluesItems.ITEMS.register(modEventBus);

        // Better Badlands 移植内容：必须先触发静态初始化，让 DeferredRegister 条目
        // 在 RegisterEvent 之前完成登记（否则客户端 setupRenderLayers 首次访问时已过窗口）。
        com.teamaurora.bayou_blues.core.registry.BayouBluesBlocksBadlands.bootstrap();

        // Register Entities
        BayouBluesEntities.ENTITIES.register(modEventBus);

        // Register Block Entity Types
        com.teamaurora.bayou_blues.core.registry.BayouBluesBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        // Register Creative Tab
        BayouBluesTabs.TABS.register(modEventBus);

        // Register Particles (Better Badlands 移植：twig 碎枝粒子)
        com.teamaurora.bayou_blues.core.registry.BayouBluesParticles.PARTICLES.register(modEventBus);

        // Register blocks: Quark 体系方块（强制依赖，非联动）+ Buzzier Bees 可选联动
        BayouBluesBlocks.registerCompatBlocks();

        // Register Features and TreeDecorators
        BayouBluesFeatures.FEATURES.register(modEventBus);
        BayouBluesFeatures.TREE_DECORATORS.register(modEventBus);

        // Register Biomes
        BayouBluesBiomes.BIOMES.register(modEventBus);

        // Register conditional biome modifier serializer
        com.teamaurora.bayou_blues.core.forge.ConditionalBiomeModifier.SERIALIZERS.register(modEventBus);
        // Register copy_features biome modifier serializer（独立副本注入，防 Feature order cycle）
        com.teamaurora.bayou_blues.core.forge.CopyFeaturesBiomeModifier.SERIALIZERS.register(modEventBus);
        net.minecraftforge.common.crafting.CraftingHelper.register(com.teamaurora.bayou_blues.core.forge.ConfigCondition.Serializer.INSTANCE);
        
        // Register Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BayouBluesConfig.COMMON_SPEC);
        
        // Register mod content
        modEventBus.addListener(this::commonSetup);

        LOGGER.info("Bayou Blues initialized");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Register compostables and flammables
            BayouBluesCompat.registerCompostables();
            BayouBluesCompat.registerFlammables();

            // Register potted plants (manual flower pot placement)
            BayouBluesBlocks.registerFlowerPots();

            // Register cypress boat dispenser behavior
            DispenserBlock.registerBehavior(BayouBluesItems.CYPRESS_BOAT.get(), new CypressBoatDispenseBehavior());

            // Register TerraBlender region
            BayouBluesTerraBlender.register();
        });
    }

    @SuppressWarnings("removal")
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
