package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.block.BBBeehiveBlock;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Buzzier Bees compatibility blocks - only loaded when Buzzier Bees is present.
 * This class is in a separate file to avoid class loading issues.
 */
public class BayouBluesBlocksBuzzierBees {

    public static RegistryObject<Block> CYPRESS_BEEHIVE;

    public static void register() {
        BayouBlues.LOGGER.info("Registering Buzzier Bees compatibility blocks");

        CYPRESS_BEEHIVE = createCompatBlock("cypress_beehive",
                () -> new BBBeehiveBlock(BlockBehaviour.Properties.copy(Blocks.BEEHIVE)));
        BayouBluesBlockEntities.registerCypressBeehive();
    }

    private static RegistryObject<Block> createCompatBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = BayouBluesBlocks.BLOCKS.register(name, block);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties()));
        return blockObj;
    }
}
