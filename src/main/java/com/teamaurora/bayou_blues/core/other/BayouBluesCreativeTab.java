package com.teamaurora.bayou_blues.core.other;

import com.teamaurora.bayou_blues.core.BayouBlues;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBlocks;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 本模组内容已全部收纳进自定义"长沼蓝调"标签页（见 {@link com.teamaurora.bayou_blues.core.registry.BayouBluesTabs}）。
 * 本类仅剩一个职责：把丝柏树叶加入原版"自然方块"页，作为 Zeta 自动插入丝柏树叶篱的
 * 定位锚点——Quark 的 HedgeBlock 构造时硬编码了"插到 cypress_leaves 旁边"，若树叶
 * 不在 NATURAL 页，Zeta 扫描匹配失败会重试到 90 次并刷 "Creative tab loop" 错误日志。
 * HIGHEST 优先级保证树叶先于 Zeta 的扫描进入标签页。
 */
@Mod.EventBusSubscriber(modid = BayouBlues.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BayouBluesCreativeTab {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(BayouBluesBlocks.CYPRESS_LEAVES.get());
        }
    }
}
