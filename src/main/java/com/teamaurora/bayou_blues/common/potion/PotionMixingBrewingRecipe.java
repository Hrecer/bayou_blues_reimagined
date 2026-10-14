package com.teamaurora.bayou_blues.common.potion;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

/**
 * 保留容器类型的酿造配方（等价 1.16.5 {@code PotionBrewing.addMix} 的容器行为）：
 * 普通/喷溅/滞留药水都可以参与，输出沿用输入的容器与自定义效果。
 *
 * <p>1.20.1 的 {@code PotionBrewing.addMix} 已私有化，Forge 的 {@code BrewingRecipe}
 * 输出是固定 ItemStack（会把喷溅/滞留降级为普通药水），故自实现。
 */
public class PotionMixingBrewingRecipe implements IBrewingRecipe {
    private final Potion from;
    private final Item ingredient;
    private final Potion to;

    public PotionMixingBrewingRecipe(Potion from, Item ingredient, Potion to) {
        this.from = from;
        this.ingredient = ingredient;
        this.to = to;
    }

    private static boolean isPotionContainer(ItemStack stack) {
        return stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION);
    }

    @Override
    public boolean isInput(ItemStack stack) {
        return isPotionContainer(stack) && PotionUtils.getPotion(stack) == this.from;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return stack.is(this.ingredient);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!isInput(input) || !isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }
        ItemStack output = PotionUtils.setPotion(new ItemStack(input.getItem()), this.to);
        PotionUtils.setCustomEffects(output, PotionUtils.getCustomEffects(input));
        return output;
    }
}
