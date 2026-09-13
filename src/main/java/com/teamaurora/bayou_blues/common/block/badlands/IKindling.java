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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 「引火物」（Kindling）方块家族的共用行为。
 *
 * <p>移植自 Better Badlands 1.16.5 的 {@code com.teamaurora.better_badlands.common.block.IKindling}。
 * 这是一套人工控制的「阴燃—蔓延—烧尽」系统，与主世界火焰机制并行：
 * <ul>
 *   <li>{@link #BURN_DISTANCE} 0 表示未点燃；被点燃后从 1 开始逐格 +1 向外蔓延，上限 21。</li>
 *   <li>蔓延到 21 后不再传递，改为把自身标记 {@link #IS_BURNED}，等待
 *       {@link #getEquation(int)} 计算的延迟后彻底烧毁并点燃周围方块。</li>
 *   <li>贴着火/岩浆放置会被立即点燃；被着火的弹射物击中也会被点燃。</li>
 *   <li>打火石或火焰弹右键直接点燃。</li>
 * </ul>
 *
 * <p><b>移植注记</b>：1.16.5 的 {@code ServerWorld.getPendingBlockTicks().schedule(...)} 在 1.20.1
 * 是 {@link ServerLevel#scheduleTick}；{@code BlockState.get(prop)} 现在叫 {@code getValue(prop)}，
 * 且对缺失属性会抛异常，因此这里统一用 {@link BlockState#hasProperty} 做守卫。
 */
public interface IKindling {

    IntegerProperty BURN_DISTANCE = IntegerProperty.create("burn_distance", 0, 21);
    BooleanProperty IS_BURNED = BooleanProperty.create("burned");

    /** 每次状态推进之间的调度间隔（tick）。 */
    int TO_SCHEDULE = 30;

    /**
     * 被着火的弹射物击中：直接把命中方块点燃（距离 1）。
     * 对应 1.16.5 {@code BlockState.onProjectileHit} 的扩展逻辑。
     */
    default void onProjectileCollisionKindling(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire()) {
            BlockPos pos = hit.getBlockPos();
            level.setBlockAndUpdate(pos, state.setValue(this.getDistProperty(state), 1));
        }
    }

    /**
     * 方块被放置时：贴着岩浆直接烧掉，否则排一次 tick。
     */
    default void onBlockAddedKindling(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        boolean touchingLava = false;
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(pos.relative(dir)).getBlock() == Blocks.LAVA) {
                touchingLava = true;
            }
        }
        if (touchingLava) {
            level.destroyBlock(pos, false);
        } else {
            level.scheduleTick(pos, state.getBlock(), TO_SCHEDULE);
        }
    }

    /**
     * 邻居变化：贴岩浆销毁；贴火则点燃。
     */
    default void neighborChangedKindling(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos fromPos, boolean isMoving) {
        boolean touchingLava = false;
        boolean touchingFire = false;
        for (Direction dir : Direction.values()) {
            Block neighbour = level.getBlockState(pos.relative(dir)).getBlock();
            if (neighbour == Blocks.LAVA) {
                touchingLava = true;
            } else if (neighbour == Blocks.FIRE) {
                touchingFire = true;
            }
        }
        if (touchingLava) {
            level.destroyBlock(pos, false);
        } else if (touchingFire) {
            level.setBlockAndUpdate(pos, state.setValue(this.getDistProperty(state), 1));
        }
    }

    /**
     * 读取燃烧距离。该属性只存在于引火物家族方块上，因此需要守卫。
     */
    default int getDistFromBlockstate(BlockState state) {
        return state.hasProperty(BURN_DISTANCE) ? state.getValue(BURN_DISTANCE) : 0;
    }

    /**
     * 子类可覆写以换用别的距离属性（原版接口预留）。
     */
    default IntegerProperty getDistProperty(BlockState state) {
        return BURN_DISTANCE;
    }

    /**
     * 客户端每帧：阴燃时冒火星与烟，并偶尔播放火焰环境音。
     */
    default void animateTickKindling(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (this.getDistFromBlockstate(state) > 0) {
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

    /**
     * 烧尽延迟曲线（原式照搬）：{@code x * (x / 5) / (25 * 8)}。
     * <p>x=21 时结果为 2。原版在结果为 0 时会以 0 延迟重复调度自身，这里抬到最小 1 tick
     * 以避免无意义的同 tick 调度风暴；烧尽路径本身仍与原始行为一致。
     */
    static int getEquation(int x) {
        int i = 8;
        return Math.max(1, x * (x / 5) / (25 * i));
    }

    /**
     * 读取「已烧尽」标记。
     */
    default boolean getBurntFromBlockstate(BlockState state) {
        return state.hasProperty(IS_BURNED) && state.getValue(IS_BURNED);
    }

    /**
     * 服务端 tick：整套阴燃/蔓延/烧尽状态机。
     */
    default void tickKindling(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        int dist = this.getDistFromBlockstate(state);

        // ---- 已烧尽：播粒子/音效，变空气并点燃四周 ----
        if (this.getBurntFromBlockstate(state)) {
            VoxelShape shape = state.getShape(level, pos);
            AABB box = shape.bounds();
            double x = pos.getX() + Math.min(box.maxX, rand.nextDouble()) * 0.1F;
            double y = pos.getY() + Math.min(box.maxY, rand.nextDouble());
            double z = pos.getZ() + Math.min(box.maxZ, rand.nextDouble());
            level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
            level.sendParticles(ParticleTypes.LAVA, x, y, z, rand.nextInt(5) + 1,
                    rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.0);
            level.sendParticles(BayouBluesParticles.TWIG.get(), x, y, z, rand.nextInt(10) + 5,
                    rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.0);

            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.playSound(null, pos, SoundEvents.GENERIC_BURN, SoundSource.PLAYERS,
                    1.0F, level.random.nextFloat() * 0.4F + 0.8F);

            for (Direction direction : Direction.values()) {
                BlockPos offset = pos.relative(direction);
                BlockState neighbour = level.getBlockState(offset);
                if (neighbour.getBlock() instanceof TntBlock) {
                    level.removeBlock(offset, false);
                } else if (neighbour.isFlammable(level, offset, direction)) {
                    level.setBlockAndUpdate(offset, Blocks.FIRE.defaultBlockState());
                }
            }
            return;
        }

        // ---- 正在燃烧：向邻居传递距离 ----
        if (dist > 0) {
            level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS,
                    1.0F, level.random.nextFloat() * 0.4F + 0.8F);
            if (dist < 21) {
                for (Direction dir : Direction.values()) {
                    BlockPos offset = pos.relative(dir);
                    BlockState neighbour = level.getBlockState(offset);
                    if (neighbour.getBlock() instanceof IKindling) {
                        if (this.getDistFromBlockstate(neighbour) != 0) {
                            continue;
                        }
                        level.setBlockAndUpdate(offset, neighbour.setValue(this.getDistProperty(neighbour), dist + 1));
                        level.scheduleTick(offset, neighbour.getBlock(), TO_SCHEDULE);
                    } else if (neighbour.getBlock() instanceof TntBlock) {
                        level.removeBlock(offset, false);
                    }
                }
            }
            level.setBlockAndUpdate(pos, state.setValue(IS_BURNED, true));
            level.scheduleTick(pos, state.getBlock(), getEquation(dist));
            return;
        }

        // ---- 未点燃：检查是否被旁边的火引燃 ----
        boolean touchingFire = false;
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(pos.relative(dir)).getBlock() == Blocks.FIRE) {
                touchingFire = true;
            }
        }
        if (touchingFire) {
            level.setBlockAndUpdate(pos, state.setValue(this.getDistProperty(state), 1));
        }
    }

    /**
     * 打火石 / 火焰弹右键点燃。
     */
    default InteractionResult useKindling(BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        Item item = player.getItemInHand(hand).getItem();
        if (item == Items.FLINT_AND_STEEL || item == Items.FIRE_CHARGE) {
            level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS,
                    1.0F, level.random.nextFloat() * 0.4F + 0.8F);
            level.setBlockAndUpdate(pos, state.setValue(this.getDistProperty(state), 1));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    /**
     * 子类在 {@code createBlockStateDefinition} 中合入本家族的两个属性。
     */
    default StateDefinition.Builder<Block, BlockState> addKindlingProperties(StateDefinition.Builder<Block, BlockState> builder) {
        return builder.add(BURN_DISTANCE, IS_BURNED);
    }

    /**
     * 供子类读取任意属性值时的通用守卫（保留以贴近原始接口语义）。
     */
    @SuppressWarnings("unchecked")
    static <T extends Comparable<T>> T safeGet(BlockState state, Property<T> property, T fallback) {
        return state.hasProperty(property) ? state.getValue(property) : fallback;
    }

    /** 供子类读取方块实体判定（原接口预留）。 */
    static boolean isKindling(Block block) {
        return block instanceof IKindling;
    }

    /** 供 {@code BlockGetter} 场景下读取碰撞箱（原接口预留）。 */
    static VoxelShape shapeOf(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getShape(level, pos);
    }

    /** 供其它方块（如 {@link TerracottaLampBlock}）判断实体来源。 */
    static Entity projectileOwner(Projectile projectile) {
        return projectile.getOwner();
    }
}
