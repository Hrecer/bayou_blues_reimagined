package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 粒子注册表。{@code twig} 移植自 Better Badlands 的
 * {@code BetterBadlandsParticles.TWIG}（引火物烧尽时飞散的碎枝）。
 *
 * <p>1.16.5 的 {@code BasicParticleType} 在 1.20.1 更名 {@link SimpleParticleType}；
 * {@code ForgeRegistries.PARTICLE_TYPES} → {@code Registries.PARTICLE_TYPE}。
 */
public class BayouBluesParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, BayouBlues.MOD_ID);

    public static final RegistryObject<SimpleParticleType> TWIG =
            PARTICLES.register("twig", () -> new SimpleParticleType(false));
}
