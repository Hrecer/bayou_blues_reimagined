package com.teamaurora.bayou_blues.common.block.badlands;

import com.teamaurora.bayou_blues.core.registry.BayouBluesParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public interface IKindling {
    IntegerProperty BURN_DISTANCE = IntegerProperty.create("burn_distance", 0, 21);
    BooleanProperty IS_BURNED = BooleanProperty.create("is_burned");

    default void onProjectileHitKindling(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire()) {
            BlockPos pos = hit.getBlockPos();
            level.setBlock(pos, state.setValue(BURN_DISTANCE, 1).setValue(IS_BURNED, false), 3);
            level.scheduleTick(pos, state.getBlock(), 10);
        }
    }

    default InteractionResult useKindling(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.FLINT_AND_STEEL)) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(BURN_DISTANCE, 1).setValue(IS_BURNED, false), 3);
                level.scheduleTick(pos, state.getBlock(), 10);
                itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (itemstack.is(Items.FIRE_CHARGE)) {
            level.playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(BURN_DISTANCE, 1).setValue(IS_BURNED, false), 3);
                level.scheduleTick(pos, state.getBlock(), 10);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    default void neighborChangedKindling(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide && state.getValue(BURN_DISTANCE) == 0 && isNextToFire(level, pos)) {
            level.setBlock(pos, state.setValue(BURN_DISTANCE, 1).setValue(IS_BURNED, false), 3);
            level.scheduleTick(pos, state.getBlock(), 10);
        }
    }

    default void onPlaceKindling(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide && state.getValue(BURN_DISTANCE) == 0 && isNextToFire(level, pos)) {
            level.setBlock(pos, state.setValue(BURN_DISTANCE, 1).setValue(IS_BURNED, false), 3);
            level.scheduleTick(pos, state.getBlock(), 10);
        }
    }

    default boolean isNextToFire(Level level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockState adj = level.getBlockState(pos.relative(dir));
            if (adj.is(Blocks.FIRE) || adj.is(Blocks.SOUL_FIRE) || adj.is(Blocks.LAVA)) {
                return true;
            }
        }
        return false;
    }

    default void tickKindling(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int dist = state.getValue(BURN_DISTANCE);
        boolean burned = state.getValue(IS_BURNED);
        if (dist > 0) {
            if (!burned) {
                for (Direction direction : Direction.values()) {
                    BlockPos adjPos = pos.relative(direction);
                    BlockState adjState = level.getBlockState(adjPos);
                    Block adjBlock = adjState.getBlock();
                    if (adjBlock instanceof IKindling) {
                        int adjDist = adjState.getValue(BURN_DISTANCE);
                        if (adjDist == 0 && dist < 21) {
                            level.setBlock(adjPos, adjState.setValue(BURN_DISTANCE, dist + 1).setValue(IS_BURNED, false), 3);
                            level.scheduleTick(adjPos, adjBlock, 10 + random.nextInt(5));
                        }
                    } else if (adjBlock instanceof TntBlock) {
                        adjState.onCaughtFire(level, adjPos, direction.getOpposite(), null);
                        level.removeBlock(adjPos, false);
                    } else if (direction == Direction.UP && adjState.isAir() && dist == 21) {
                        level.setBlock(adjPos, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
                level.setBlock(pos, state.setValue(IS_BURNED, true), 3);
                level.scheduleTick(pos, state.getBlock(), 10);
            } else {
                level.removeBlock(pos, false);
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
                level.sendParticles(BayouBluesParticles.TWIG.get(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 15, 0.25D, 0.25D, 0.25D, 0.0D);
                level.sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5, 0.25D, 0.25D, 0.25D, 0.0D);
                level.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5, 0.25D, 0.25D, 0.25D, 0.0D);
            }
        }
    }
}
