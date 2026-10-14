package com.teamaurora.bayou_blues.core;

import com.teamaurora.bayou_blues.core.forge.ConditionalBiomeModifier;
import com.teamaurora.bayou_blues.core.forge.ConfigCondition;
import com.teamaurora.bayou_blues.core.forge.CopyFeaturesBiomeModifier;
import com.teamaurora.bayou_blues.core.other.BayouBluesCompat;
import com.teamaurora.bayou_blues.core.other.CypressBoatDispenseBehavior;
import com.teamaurora.bayou_blues.core.registry.*;
import com.teamaurora.bayou_blues.core.terrablender.BayouBluesTerraBlender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Mod(BayouBlues.MOD_ID)
public class BayouBlues {
    public static final String MOD_ID = "bayou_blues";
    public static final Logger LOGGER = LogManager.getLogger();

    public BayouBlues() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        BayouBluesBlocks.BLOCKS.register(modEventBus);
        BayouBluesBlocks.ITEMS.register(modEventBus);
        BayouBluesItems.ITEMS.register(modEventBus);
        BayouBluesBlocksBadlands.bootstrap();
        BayouBluesEntities.ENTITIES.register(modEventBus);
        BayouBluesBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        BayouBluesTabs.TABS.register(modEventBus);
        BayouBluesParticles.PARTICLES.register(modEventBus);
        BayouBluesEffects.EFFECTS.register(modEventBus);
        BayouBluesEffects.POTIONS.register(modEventBus);
        BayouBluesBlocks.registerCompatBlocks();
        BayouBluesFeatures.FEATURES.register(modEventBus);
        BayouBluesFeatures.TREE_DECORATORS.register(modEventBus);
        BayouBluesBiomes.BIOMES.register(modEventBus);
        ConditionalBiomeModifier.SERIALIZERS.register(modEventBus);
        CopyFeaturesBiomeModifier.SERIALIZERS.register(modEventBus);
        CraftingHelper.register(ConfigCondition.Serializer.INSTANCE);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BayouBluesConfig.COMMON_SPEC);
        modEventBus.addListener(this::commonSetup);

        LOGGER.info("Bayou Blues initialized");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BayouBluesCompat.registerCompostables();
            BayouBluesCompat.registerFlammables();
            BayouBluesCompat.replaceDarkOakGrower();
            BayouBluesBlocks.registerFlowerPots();
            BayouBluesEffects.registerBrewingRecipes();
            DispenserBlock.registerBehavior(BayouBluesItems.CYPRESS_BOAT.get(), new CypressBoatDispenseBehavior());
            BayouBluesTerraBlender.register();
            registerSignBlockEntities();
        });
    }

    public static void registerSignBlockEntities() {
        try {
            BlockEntityType<?> signType = ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(new ResourceLocation("minecraft", "sign"));
            if (signType != null) {
                for (Field f : BlockEntityType.class.getDeclaredFields()) {
                    if (Set.class.isAssignableFrom(f.getType())) {
                        f.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        Set<Block> oldSet = (Set<Block>) f.get(signType);
                        Set<Block> newSet = new HashSet<>(oldSet);
                        newSet.add(BayouBluesBlocks.CYPRESS_SIGN.get());
                        newSet.add(BayouBluesBlocks.CYPRESS_WALL_SIGN.get());
                        f.set(signType, newSet);
                        break;
                    }
                }
            }
        } catch (Throwable t) {
            LOGGER.error("Failed to register cypress signs to BlockEntityType.SIGN", t);
        }
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
