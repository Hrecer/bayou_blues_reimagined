package com.teamaurora.bayou_blues.core.terrablender;

import com.teamaurora.bayou_blues.core.BayouBlues;
import com.teamaurora.bayou_blues.core.BayouBluesConfig;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class BayouBluesTerraBlender {
    
    public static void register() {
        int bayouWeight = BayouBluesConfig.COMMON.bayouWeight.get();

        if (bayouWeight > 0) {
            Regions.register(new BayouBluesRegion("bayou_blues", bayouWeight));
        }

        // ── Better Badlands 移植 ────────────────────────────────────────────────────────────
        // 这里【故意不注册】BayouBluesBadlandsRegion。原因见该类顶部注释，摘要：
        //   1. 原版 Better Badlands 不新增群系，只改 wooded_badlands 内部的特性，
        //      1.20.1 的正确对应物是 forge:biome_modifier（badlands_*.json），不是 Region；
        //   2. 注册额外 Region 会瓜分 TerraBlender 的 uniqueness 值域，把 bayou 的世界
        //      占比从 50% 压到 33%，而恒等映射的恶地 Region 功能收益为零。
        // 将来若要新增真正的恶地子群系，解除下面两行注释，并把 bayou_weight 相应调小：
        //   int badlandsWeight = BayouBluesConfig.COMMON.badlandsWeight.get();
        //   if (badlandsWeight > 0) Regions.register(new BayouBluesBadlandsRegion("badlands", badlandsWeight));
        
        SurfaceRuleManager.addSurfaceRules(
            SurfaceRuleManager.RuleCategory.OVERWORLD,
            BayouBlues.MOD_ID,
            BayouBluesSurfaceRuleData.makeRules()
        );
    }
}
