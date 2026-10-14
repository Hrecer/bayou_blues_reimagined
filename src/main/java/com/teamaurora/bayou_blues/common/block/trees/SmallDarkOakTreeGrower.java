package com.teamaurora.bayou_blues.common.block.trees;

import com.teamaurora.bayou_blues.core.registry.BayouBluesFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.DarkOakTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

/**
 * 单个深色橡木树苗长成"小深色橡木"的现代适配。
 *
 * <p>Better Badlands 1.16.5 用 {@code SaplingGrowTreeEvent} 实现：单个（非 2x2）
 * 深色橡木树苗生长时拒绝原版、原地生成小深色橡木。1.20.1 的事件改在
 * {@code AbstractTreeGrower.growTree} 里触发，而单个树苗在
 * {@code DarkOakTreeGrower.getConfiguredFeature} 就返回 null 提前退出——事件根本不会触发。
 * 因此改为在启动时把原版深色橡木树苗的 grower 替换为本类（见
 * {@code BayouBluesCompat.replaceDarkOakGrower}）：
 * <ul>
 *   <li>单个树苗（随机刻/骨粉）→ 返回 {@code bayou_blues:small_dark_oak_tree}，
 *       随后原版机制照常触发 SaplingGrowTreeEvent（携带本特征）、放树、失败自动还原树苗；</li>
 *   <li>2x2 → 走原版 mega 通道（大深色橡木），与本类无关。</li>
 * </ul>
 * 行为与 BBB 一致（不分群系、全局生效）。
 */
public class SmallDarkOakTreeGrower extends DarkOakTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean flowers) {
        return BayouBluesFeatures.Configured.SMALL_DARK_OAK;
    }
}
