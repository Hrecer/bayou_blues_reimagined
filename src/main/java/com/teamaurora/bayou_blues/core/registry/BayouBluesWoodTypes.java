package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BayouBluesWoodTypes {
    public static final BlockSetType CYPRESS_BLOCK_SET = BlockSetType.register(new BlockSetType(BayouBlues.MOD_ID + ":cypress"));
    public static final WoodType CYPRESS = WoodType.register(new WoodType(BayouBlues.MOD_ID + ":cypress", CYPRESS_BLOCK_SET));
}
