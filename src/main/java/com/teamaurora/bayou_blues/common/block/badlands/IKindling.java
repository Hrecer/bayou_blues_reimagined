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
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 引火物接口，按 Better Badlands 1.16.5（master，1.0.5）逐行移植。
 *
 * <p>燃烧链：点燃（burn_distance=1）→ 30 tick 后向四邻蔓延（每格 +1，上限 21）
 * 并把自身置为 burned → 下一 tick 爆出碎枝粒子、移除自身；若此时邻接可燃方块，
 * 则在本格放火（原版的 <code>catchFire + setBlockState(FIRE)</code> 净效果）。
 * 邻接岩浆时方块无掉落销毁（放置/邻居更新两条路径）。
 *
 * <p><b>与原版唯一的行为差异</b>：原版只在方块放置时安排一次 30 tick 计时器，
 * 若放置超时后再用打火石点燃，状态会停在 burn_distance=1 永远不推进（卡死缺陷）。
 * 这里在所有点燃路径（打火石/火焰弹/火焰邻居/投射物/邻火刻）都补了一次排程，
 * 保证燃烧链必然启动；其余数值（30 tick 节奏、1/21 上限、粒子与音效）与原版一致。
 */
public interface IKindling {
    IntegerProperty BURN_DISTANCE = IntegerProperty.create("burn_distance", 0, 21);
    BooleanProperty IS_BURNED = BooleanProperty.create("is_burned");
    int TO_SCHEDULE = 30;

    /** 原版 getEquation：dist ≤ 21 时恒为 0，即自毁相位在下一 tick 执行。 */
    static int getEquation(int x) {
        int i = 8;
        return (x * (x / 5)) / (25 * i);
    }

    default void ignite(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(BURN_DISTANCE, 1), 3);
        level.scheduleTick(pos, state.getBlock(), TO_SCHEDULE);
    }

    default void onProjectileHitKindling(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire()) {
            this.ignite(level, hit.getBlockPos(), state);
        }
    }

    default InteractionResult useKindling(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            if (!level.isClientSide) {
                this.ignite(level, pos, state);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    default void neighborChangedKindling(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) {
            return;
        }
        boolean lava = false;
        boolean fire = false;
        for (Direction dir : Direction.values()) {
            BlockState neighbor = level.getBlockState(pos.relative(dir));
            if (neighbor.is(Blocks.LAVA)) {
                lava = true;
            } else if (neighbor.is(Blocks.FIRE)) {
                fire = true;
            }
        }
        if (lava) {
            level.destroyBlock(pos, false);
        } else if (fire) {
            this.ignite(level, pos, state);
        }
    }

    default void onPlaceKindling(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level.isClientSide) {
            return;
        }
        boolean lava = false;
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(pos.relative(dir)).is(Blocks.LAVA)) {
                lava = true;
                break;
            }
        }
        if (lava) {
            level.destroyBlock(pos, false);
        } else {
            level.scheduleTick(pos, state.getBlock(), TO_SCHEDULE);
        }
    }

    default void tickKindling(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        int dist = state.getValue(BURN_DISTANCE);
        if (state.getValue(IS_BURNED)) {
            VoxelShape shape = state.getShape(level, pos);
            double x = pos.getX() + Math.min(shape.max(Direction.Axis.X), rand.nextDouble()) * 0.1D;
            double y = pos.getY() + Math.min(shape.max(Direction.Axis.Y), rand.nextDouble());
            double z = pos.getZ() + Math.min(shape.max(Direction.Axis.Z), rand.nextDouble()) * 0.1D;
            level.sendParticles(ParticleTypes.LAVA, x, y, z, rand.nextInt(5) + 1, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.0D);
            level.sendParticles(BayouBluesParticles.TWIG.get(), x, y, z, rand.nextInt(10) + 5, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.0D);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            level.playSound(null, pos, SoundEvents.GENERIC_BURN, SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                BlockState neighbor = level.getBlockState(neighborPos);
                if (neighbor.isFlammable(level, neighborPos, direction.getOpposite())) {
                    level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
                    if (neighbor.getBlock() instanceof TntBlock) {
                        level.setBlock(neighborPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
            return;
        }
        if (dist > 0) {
            level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            if (dist < 21) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighborPos = pos.relative(dir);
                    BlockState neighbor = level.getBlockState(neighborPos);
                    if (neighbor.getBlock() instanceof IKindling) {
                        if (neighbor.getValue(BURN_DISTANCE) == 0) {
                            level.setBlock(neighborPos, neighbor.setValue(BURN_DISTANCE, dist + 1), 3);
                            level.scheduleTick(neighborPos, neighbor.getBlock(), TO_SCHEDULE);
                        }
                    } else if (neighbor.getBlock() instanceof TntBlock) {
                        neighbor.getBlock().onCaughtFire(neighbor, level, neighborPos, dir.getOpposite(), null);
                    }
                }
            }
            level.setBlock(pos, state.setValue(IS_BURNED, true), 3);
            level.scheduleTick(pos, state.getBlock(), getEquation(dist));
        } else {
            for (Direction dir : Direction.values()) {
                if (level.getBlockState(pos.relative(dir)).is(Blocks.FIRE)) {
                    this.ignite(level, pos, state);
                    break;
                }
            }
        }
    }

    /** 客户端每帧：阴燃时冒出火焰与烟，并偶尔播放火焰环境音。 */
    default void animateTickKindling(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (state.getValue(BURN_DISTANCE) > 0) {
            for (int i = 0; i < 20; ++i) {
                double x = pos.getX() + rand.nextDouble() + rand.nextDouble() / 6.0;
                double y = pos.getY() + rand.nextDouble() + rand.nextDouble() / 6.0;
                double z = pos.getZ() + rand.nextDouble() + rand.nextDouble() / 6.0;
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
            }
            int smokeCount = rand.nextInt(35) + 5;
            for (int i = 0; i < smokeCount; ++i) {
                double x = pos.getX() + rand.nextDouble() + rand.nextDouble() / 6.0;
                double y = pos.getY() + rand.nextDouble() + rand.nextDouble() / 6.0;
                double z = pos.getZ() + rand.nextDouble() + rand.nextDouble() / 6.0;
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
            }
            if (rand.nextInt(64) == 0) {
                level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS,
                        rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F, false);
            }
        }
    }
}
