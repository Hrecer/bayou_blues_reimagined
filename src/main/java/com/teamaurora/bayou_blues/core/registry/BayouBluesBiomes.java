package com.teamaurora.bayou_blues.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Biome registry for Bayou Blues.
 * Biomes are now defined via datapack JSON files in data/bayou_blues/worldgen/biome/
 * This class provides ResourceKey references for code access.
 */
public class BayouBluesBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, BayouBlues.MOD_ID);

    // Biome ResourceKeys - biomes are defined in datapack JSON files
    @SuppressWarnings("removal")
    public static final ResourceKey<Biome> BAYOU = ResourceKey.create(Registries.BIOME, new ResourceLocation(BayouBlues.MOD_ID, "bayou"));
}
