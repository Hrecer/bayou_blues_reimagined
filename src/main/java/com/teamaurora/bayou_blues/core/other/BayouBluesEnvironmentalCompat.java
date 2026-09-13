package com.teamaurora.bayou_blues.core.other;

import com.teamabnormals.environmental.common.block.LargeLilyPadBlock;
import com.teamabnormals.environmental.core.registry.EnvironmentalBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Environmental 联动的运行时入口。
 * <p>
 * 独立成类以隔离对 Environmental 类的引用：本类只在
 * {@code ModList.isLoaded("environmental")} 为真时才被调用，
 * 方法体内解析的 Environmental 类引用不会在缺席时触发类加载。
 */
public final class BayouBluesEnvironmentalCompat {

    private BayouBluesEnvironmentalCompat() {
    }

    /**
     * 在原版睡莲位置种下 Environmental 的大睡莲。
     * <p>
     * Environmental 自身也有一个对原版睡莲的骨粉处理器（1/3 概率放置
     * 大/巨型睡莲且总是 ALLOW），这里的 checkPositions 守卫保证即使
     * 两个处理器先后触发也只在位置为空时放置，不会重复或互相覆盖。
     */
    public static void tryGrowLargeLilyPad(Level level, BlockPos pos) {
        BlockState state = EnvironmentalBlocks.LARGE_LILY_PAD.get().defaultBlockState();
        if (LargeLilyPadBlock.checkPositions(level, pos, state)) {
            LargeLilyPadBlock.placeAt(level, pos, state, 3);
        }
    }
}
