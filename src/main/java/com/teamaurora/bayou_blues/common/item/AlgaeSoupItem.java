package com.teamaurora.bayou_blues.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/**
 * 藻汤：吃完返还碗，并给予 3 秒（60 tick）再生 II。
 */
public class AlgaeSoupItem extends ContainerFoodItem {
    private static final int REGENERATION_DURATION = 60;
    private static final int REGENERATION_AMPLIFIER = 1;

    public AlgaeSoupItem(Item.Properties properties, Supplier<Item> container) {
        super(properties, container);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, REGENERATION_DURATION, REGENERATION_AMPLIFIER));
        }
        return result;
    }
}
