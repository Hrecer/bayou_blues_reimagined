package com.teamaurora.bayou_blues.common.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * "衰亡"（Succumbing）效果。移植自 Better Badlands 的 {@code SuccumbingEffect}：
 * 有害、无实际效果（原版即空实现，仅作为萨瓜罗触碰的负面标记/未来的机制预留）。
 */
public class SuccumbingEffect extends MobEffect {

    public SuccumbingEffect() {
        super(MobEffectCategory.HARMFUL, 5999141);
    }
}
