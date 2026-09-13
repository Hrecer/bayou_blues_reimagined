package com.teamaurora.bayou_blues.common.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Jackson (code temporarily from next/1.16.x branch of Pollen)
 * Modified for 1.20.1 compatibility
 */
public class TabInsertBlockItem extends BlockItem {
    private final Predicate<ItemStack> filter;
    private final boolean insertBefore;

    public TabInsertBlockItem(Item insertAfter, Block block, Properties properties) {
        this(stack -> stack.getItem() == insertAfter, false, block, properties);
    }

    public TabInsertBlockItem(Supplier<Item> insertAfter, Block block, Properties properties) {
        this(stack -> stack.getItem() == insertAfter.get(), false, block, properties);
    }

    public TabInsertBlockItem(Class<Item> insertAfterClass, Block block, Properties properties) {
        this(stack -> insertAfterClass.isInstance(stack.getItem()), false, block, properties);
    }

    public TabInsertBlockItem(Predicate<ItemStack> filter, Block block, Properties properties) {
        this(filter, false, block, properties);
    }

    public TabInsertBlockItem(Predicate<ItemStack> filter, boolean insertBefore, Block block, Properties properties) {
        super(block, properties);
        this.filter = filter;
        this.insertBefore = insertBefore;
    }

    // fillItemCategory removed in 1.20.1, using CreativeTabEvent instead
    // @Override
    // public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
    //     if (this.allowedIn(category)) {
    //         insert(new ItemStack(this), this.insertBefore, items, this.filter);
    //     }
    // }

    private static void insert(ItemStack stack, boolean before, NonNullList<ItemStack> items, Predicate<ItemStack> filter) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (filter.test(items.get(i))) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            items.add(before ? index : index + 1, stack);
        } else {
            items.add(stack);
        }
    }
}
