package com.teamaurora.bayou_blues.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/**
 * 吃完后返还容器的食物：汤类返还碗，茶/汁类返还玻璃瓶。
 * 堆叠被吃空时直接返还容器，否则塞进玩家背包，装不下就丢在地上。
 */
public class ContainerFoodItem extends Item {
    private final Supplier<Item> container;

    public ContainerFoodItem(Item.Properties properties, Supplier<Item> container) {
        super(properties);
        this.container = container;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = new ItemStack(this.container.get());
        super.finishUsingItem(stack, level, entity);
        if (stack.isEmpty()) {
            return result;
        }
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            if (!player.getInventory().add(result)) {
                player.drop(result, false);
            }
        }
        return stack;
    }
}
