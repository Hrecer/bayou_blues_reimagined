package com.teamaurora.bayou_blues.common.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

/**
 * @author Steven
 * Pretty hacky I might say
 * Totally not "borrowed" from differentiate
 * Modified for 1.20.1 compatibility
 */
public class FollowItemLike extends BlockItem {
    private final ItemLike followItem;

    public FollowItemLike(Block block, Properties properties, ItemLike followItem) {
        super(block, properties);
        this.followItem = followItem;
    }

    // fillItemCategory removed in 1.20.1, using CreativeTabEvent instead
    // @Override
    // public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
    //     fillItem(this.asItem(), this.followItem.asItem(), tab, items);
    // }

    public static int getIndex(Item item, NonNullList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static void fillItem(Item item, Item followItem, CreativeModeTab currentTab, NonNullList<ItemStack> items) {
        if (currentTab.equals(CreativeModeTabs.SEARCH)) {
            items.add(new ItemStack(item));
            return;
        }
        
        int index = getIndex(followItem, items);
        if (index != -1) {
            items.add(++index, new ItemStack(item));
        } else {
            items.add(new ItemStack(item));
        }
    }
}
