package com.teamaurora.bayou_blues.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BayouBluesConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> lilyBonemealBehavior;
        public final ForgeConfigSpec.ConfigValue<Boolean> doLiliesSpawn;
        public final ForgeConfigSpec.ConfigValue<Integer> bayouWeight;
        public final ForgeConfigSpec.ConfigValue<Boolean> doSmallDarkOaksSpawn;
        
        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common configurations for Bayou Blues")
                .push("common");
            
            builder.comment("Lily flower configuration").push("lily_flowers");
            
            this.lilyBonemealBehavior = builder.comment(
                "Lily bonemeal behavior. 0=none. 1=bonemealing lily pad grows lily flowers. 2=bonemealing lily flower gives you more of it.")
                .define("lily_bonemeal_behavior", 1);
            this.doLiliesSpawn = builder.define("lily_flower_spawns", true);
            
            builder.pop();

            // ── Better Badlands 移植内容 ──────────────────────────────────────────
            builder.comment("Better Badlands (ported) configuration").push("better_badlands");

            this.doSmallDarkOaksSpawn = builder.comment(
                "Wooded Badlands grows bent small dark oak trees instead of vanilla oak trees.")
                .define("small_dark_oaks_spawn", true);

            builder.pop();
            
            builder.comment("Values for biome frequencies; lower = more rare. (Requires restart)")
                .push("biome_weights");
            
            // TerraBlender 以 Region 为单位划分世界（vanilla_overworld_region_weight 默认 10）。
            // 本 Region 只把红树林沼泽槽位替换为 bayou，因此该权重的实际含义是：
            // 被划入本 Region 的世界面积占比 = weight / (weight + 10)。
            // 10 = 与原版对半分，bayou 出现率与原版红树林沼泽相当。
            this.bayouWeight = builder.define("bayou_weight", 10);
            
            builder.pop();
            builder.pop();
        }
    }
    
    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    /**
     * Looks up a boolean common config value by its JSON key for use in datapack conditions.
     * Key matches the config option name (e.g. "lily_flower_spawns").
     */
    public static boolean getBooleanConfig(String path) {
        return switch (path) {
            case "lily_flower_spawns" -> COMMON.doLiliesSpawn.get();
            case "small_dark_oaks_spawn" -> COMMON.doSmallDarkOaksSpawn.get();
            default -> true;
        };
    }
}
