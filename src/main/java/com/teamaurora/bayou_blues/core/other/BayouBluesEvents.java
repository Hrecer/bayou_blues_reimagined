package com.teamaurora.bayou_blues.core.other;

import com.teamaurora.bayou_blues.common.block.LilyFlowerBlock;
import com.teamaurora.bayou_blues.core.BayouBluesConfig;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import com.teamaurora.bayou_blues.core.registry.BayouBluesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "bayou_blues")
public class BayouBluesEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().is(DamageTypes.FALL)) {
            BlockPos pos = event.getEntity().blockPosition().below();
            Level level = event.getEntity().level();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (block instanceof com.teamaurora.bayou_blues.common.block.CypressKneeBlock 
                    || block instanceof com.teamaurora.bayou_blues.common.block.DoubleCypressKneeBlock) {
                // Landing on cypress knees doubles the incoming generic damage.
                event.getEntity().hurt(level.damageSources().generic(), event.getAmount() * 2.0F);
            }
        }
    }

    private static boolean checkAdjacentForSolid(Level level, BlockPos pos) {
        for (int i = 0; i < 4; i++) {
            Direction dir = Direction.from2DDataValue(i);
            BlockPos poffset = pos.relative(dir);

            if (level.getFluidState(poffset).getType() != Fluids.WATER) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onBonemealUse(BonemealEvent event) {
        BlockState state = event.getBlock();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();

        // Lily pad bonemeal behavior
        if (state.getBlock() == Blocks.LILY_PAD
                && BayouBluesConfig.COMMON.lilyBonemealBehavior.get() == 1) {

            if (ModList.get().isLoaded("environmental")
                    && !level.getRandom().nextBoolean()
                    && !checkAdjacentForSolid(level, pos.below())) {
                // 装了 Environmental 且为开阔水面时改长它的大睡莲
                if (!level.isClientSide) {
                    BayouBluesEnvironmentalCompat.tryGrowLargeLilyPad(level, pos);
                    event.setResult(Event.Result.ALLOW);
                }
            } else {
                Block lily = LilyFlowerBlock.getRandomLily(level.getRandom());

                if (!level.isClientSide) {
                    level.setBlock(pos, lily.defaultBlockState(), 3);
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }

        // Lily flower bonemeal behavior (duplicate item)
        if (BayouBluesConfig.COMMON.lilyBonemealBehavior.get() == 2) {
            Block stateBlock = state.getBlock();
            if (stateBlock instanceof LilyFlowerBlock) {
                Block.popResource(level, pos, new ItemStack(stateBlock.asItem()));
                event.setResult(Event.Result.ALLOW);
            }
        }

        // Giant fern bonemeal
        if (state.getBlock() == Blocks.LARGE_FERN) {
            if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                DoublePlantBlock.placeAt(level, BayouBluesBlocks.GIANT_FERN.get().defaultBlockState(), pos, 3);
            } else {
                DoublePlantBlock.placeAt(level, BayouBluesBlocks.GIANT_FERN.get().defaultBlockState(), pos.below(), 3);
            }
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onWandererTradesEvent(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        // Generic trades
        genericTrades.add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 1),
                new ItemStack(BayouBluesBlocks.ALGAE.get().asItem(), 2),
                6, 1, 0.05F));
        
        genericTrades.add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 5),
                new ItemStack(BayouBluesBlocks.CYPRESS_SAPLING.get().asItem(), 1),
                8, 1, 0.05F));

        // Rare trades - lilies
        if (BayouBluesConfig.COMMON.doLiliesSpawn.get() 
                || BayouBluesConfig.COMMON.lilyBonemealBehavior.get() != 0) {
            
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.BLUE_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.CYAN_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.LIGHT_BLUE_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.LIGHT_GRAY_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.WHITE_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.PINK_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.PURPLE_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
            rareTrades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesBlocks.MAGENTA_LILY.get().asItem(), 1),
                    7, 1, 0.05F));
        }
    }

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType().equals(VillagerProfession.FARMER)) {
            List<VillagerTrades.ItemListing> trades = event.getTrades().get(2);
            
            trades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesItems.GOOSEBERRY_PIE.get(), 4),
                    12, 5, 0.05F));
            
            trades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BayouBluesItems.GOOSEBERRY_JAM_COOKIE.get(), 18),
                    12, 5, 0.05F));
            
            trades.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(BayouBluesItems.GOOSEBERRIES.get(), 16),
                    12, 10, 0.05F));
        }
    }
}
