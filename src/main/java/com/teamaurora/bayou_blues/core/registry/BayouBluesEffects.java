package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.potion.PotionMixingBrewingRecipe;
import com.teamaurora.bayou_blues.common.potion.SuccumbingEffect;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 状态效果与药水（移植自 Better Badlands 的 {@code BetterBadlandsEffects}）。
 * 衰亡/衰亡（延长）/衰亡 II 三档，用萨瓜罗花在酿造台从粗制的药水起酿。
 */
public class BayouBluesEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BayouBlues.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, BayouBlues.MOD_ID);

    public static final RegistryObject<MobEffect> SUCCUMBING = EFFECTS.register("succumbing", SuccumbingEffect::new);

    public static final RegistryObject<Potion> SUCCUMBING_NORMAL = POTIONS.register("succumbing", () -> new Potion(new MobEffectInstance(SUCCUMBING.get(), 900)));
    public static final RegistryObject<Potion> SUCCUMBING_LONG = POTIONS.register("succumbing_long", () -> new Potion(new MobEffectInstance(SUCCUMBING.get(), 1800)));
    public static final RegistryObject<Potion> SUCCUMBING_STRONG = POTIONS.register("succumbing_strong", () -> new Potion(new MobEffectInstance(SUCCUMBING.get(), 432, 1)));

    public static void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(new PotionMixingBrewingRecipe(Potions.AWKWARD, BayouBluesItems.SAGUARO_FLOWER.get(), SUCCUMBING_NORMAL.get()));
        BrewingRecipeRegistry.addRecipe(new PotionMixingBrewingRecipe(SUCCUMBING_NORMAL.get(), Items.GLOWSTONE_DUST, SUCCUMBING_STRONG.get()));
        BrewingRecipeRegistry.addRecipe(new PotionMixingBrewingRecipe(SUCCUMBING_NORMAL.get(), Items.REDSTONE, SUCCUMBING_LONG.get()));
    }
}
