package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.world.gen.feature.*;
import com.teamaurora.bayou_blues.common.world.gen.treedecorator.*;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BayouBluesFeatures {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, BayouBlues.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, BayouBlues.MOD_ID);

    // Tree Decorators
    public static final RegistryObject<TreeDecoratorType<BeardMossTreeDecorator>> BEARD_MOSS = TREE_DECORATORS.register("beard_moss", () -> new TreeDecoratorType<>(BeardMossTreeDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<CypressBranchTreeDecorator>> CYPRESS_BRANCH = TREE_DECORATORS.register("cypress_branch", () -> new TreeDecoratorType<>(CypressBranchTreeDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<HangingCypressLeavesTreeDecorator>> HANGING_CYPRESS_LEAVES = TREE_DECORATORS.register("hanging_cypress_leaves", () -> new TreeDecoratorType<>(HangingCypressLeavesTreeDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<CypressKneesTreeDecorator>> CYPRESS_KNEES = TREE_DECORATORS.register("cypress_knees", () -> new TreeDecoratorType<>(CypressKneesTreeDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<SparseLeaveVineTreeDecorator>> SPARSE_LEAVE_VINE = TREE_DECORATORS.register("sparse_leave_vine", () -> new TreeDecoratorType<>(SparseLeaveVineTreeDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<SparseCypressKneesTreeDecorator>> SPARSE_CYPRESS_KNEES = TREE_DECORATORS.register("sparse_cypress_knees", () -> new TreeDecoratorType<>(SparseCypressKneesTreeDecorator.CODEC));

    // Features
    public static final RegistryObject<Feature<TreeConfiguration>> CYPRESS_TREE = FEATURES.register("cypress_tree", () -> new CypressFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<TreeConfiguration>> MEGA_CYPRESS_TREE = FEATURES.register("mega_cypress_tree", () -> new MegaCypressFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<TreeConfiguration>> WATER_CYPRESS_TREE = FEATURES.register("water_cypress_tree", () -> new WaterCypressFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<TreeConfiguration>> WATER_MEGA_CYPRESS_TREE = FEATURES.register("water_mega_cypress_tree", () -> new WaterMegaCypressFeature(TreeConfiguration.CODEC));
    
    // Patch Features
    public static final RegistryObject<Feature<BlockStateConfiguration>> LARGE_PATCH = FEATURES.register("large_patch", () -> new LargePatchFeature(BlockStateConfiguration.CODEC));
    public static final RegistryObject<Feature<BlockStateConfiguration>> LARGE_LAND_PATCH = FEATURES.register("large_land_patch", () -> new LargeLandPatchFeature(BlockStateConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PODZOL_PATCH = FEATURES.register("podzol_patch", () -> new PodzolPatchFeature(NoneFeatureConfiguration.CODEC));

    /**
     * 小深色橡木（恶地专属）。移植自 Better Badlands 的
     * {@code BetterBadlandsFeatures.SMALL_DARK_OAK_TREE}。
     * 配置与放置由 datapack JSON 提供：
     * {@code data/bayou_blues/worldgen/configured_feature/small_dark_oak_tree.json} +
     * {@code placed_feature/small_dark_oak_tree.json}。
     */
    public static final RegistryObject<Feature<TreeConfiguration>> SMALL_DARK_OAK_TREE = FEATURES.register("small_dark_oak_tree", () -> new SmallDarkOakFeature(TreeConfiguration.CODEC));

    /**
     * 萨瓜罗仙人掌（恶地专属）。移植自 Better Badlands 的
     * {@code BetterBadlandsFeatures.SAGUARO_CACTUS}，配置与放置由 datapack JSON 提供。
     */
    public static final RegistryObject<Feature<TreeConfiguration>> SAGUARO_CACTUS = FEATURES.register("saguaro_cactus", () -> new SaguaroCactusFeature(TreeConfiguration.CODEC));

    // Resource Keys for ConfiguredFeatures (for sapling tree growing)
    public static final ResourceKey<ConfiguredFeature<?, ?>> CYPRESS_GROWN = createConfiguredKey("cypress_grown");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_CYPRESS_GROWN = createConfiguredKey("mega_cypress_grown");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SAGUARO_CACTUS_KEY = createConfiguredKey("saguaro_cactus");

    // Configured inner class for easy access from CypressTree
    public static final class Configured {
        public static final ResourceKey<ConfiguredFeature<?, ?>> CYPRESS_GROWN = BayouBluesFeatures.CYPRESS_GROWN;
        public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_CYPRESS_GROWN = BayouBluesFeatures.MEGA_CYPRESS_GROWN;
        public static final ResourceKey<ConfiguredFeature<?, ?>> SAGUARO_CACTUS = BayouBluesFeatures.SAGUARO_CACTUS_KEY;
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createConfiguredKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, BayouBlues.location(name));
    }
}
