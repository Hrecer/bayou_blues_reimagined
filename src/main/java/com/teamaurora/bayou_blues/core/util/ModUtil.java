package com.teamaurora.bayou_blues.core.util;

import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

/**
 * 简化版的通用工具类
 * 只包含 BayouBlues 实际使用的方法
 */
public final class ModUtil {

    private static final Method SET_FLAMMABLE;

    static {
        SET_FLAMMABLE = ObfuscationReflectionHelper.findMethod(FireBlock.class, "m_53444_", Block.class, int.class, int.class);
        SET_FLAMMABLE.setAccessible(true);
    }

    /**
     * 注册方块为可燃
     */
    public static void registerFlammable(Block block, int encouragement, int flammability) {
        if (block == null) {
            BayouBlues.LOGGER.warn("Attempted to register null block as flammable");
            return;
        }
        try {
            SET_FLAMMABLE.invoke(Blocks.FIRE, block, encouragement, flammability);
        } catch (Exception e) {
            BayouBlues.LOGGER.error("Failed to register flammable block: " + block, e);
        }
    }

    /**
     * 注册物品为堆肥材料
     */
    public static void registerCompostable(ItemLike item, float chance) {
        if (item == null) {
            BayouBlues.LOGGER.warn("Attempted to register null item as compostable");
            return;
        }
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }
}
