package com.teamaurora.bayou_blues.core.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Biome modifier that adds an <b>independent copy</b> of each referenced placed
 * feature to the target biomes (same configured feature + same placement
 * modifiers, but a fresh PlacedFeature instance per injection site).
 *
 * <h2>为什么需要它（Feature order cycle 复盘，2026-09-15 崩溃）</h2>
 * vanilla {@code FeatureSorter} 在世界加载时对【所有群系】某个 decoration step 的
 * 特性列表做全局拓扑排序：每个群系列表里相邻两项会产生 A→B 的先后约束。当同一个
 * placed feature（同一实例）出现在多个群系、且相对顺序不一致时，拓扑排序成环，
 * 生成区块即崩：{@code IllegalStateException: Feature order cycle found}。
 *
 * Zeta（Quark 前置）的 biome modifier 会给【每个群系】的每个 step 末尾追加一个
 * 无注册键的占位特性，且它与其他 mod 的 biome modifier 的应用顺序每次启动随机
 * （注册表并行加载）。于是：
 * <pre>
 *   environmental:marsh 的基础 JSON 含   G = environmental:patch_giant_tall_grass_marsh（G 在占位符之前，恒成立）
 *   我们用 forge:add_features 给 bayou 注入同一个 G（G 在占位符之后还是之前，随机）
 * </pre>
 * 当随机结果不利时：bayou 里 占位符→G，marsh 里 G→占位符 ⇒ 成环（崩溃源恰为
 * bayou + marsh 两个群系）。forge:add_features 的官方 javadoc 也警告过这一点：
 * "Be wary of using this to add vanilla PlacedFeatures to biomes, as doing so
 * may cause a feature cycle violation."
 *
 * 本 modifier 通过为注入目标构造全新实例来切断跨群系共享：副本只会出现在
 * bayou 一个群系里，与占位符、与其他群系都不再有任何顺序约束，从机制上根除
 * 这一类崩溃——与 build.gradle 里记录的 patch_lily_*_bayou 独立副本是同一条铁律
 * 的代码化实现（那里是数据副本，这里因 Environmental 是可选依赖、无法用 JSON
 * 副本引用其 configured feature，故在应用期代码复制）。
 *
 * <p>注意 record equals 的坑：PlacedFeature 是 record，FeatureSorter 以 equals 建
 * 图节点。仅"新实例 + 相同 placement"的副本与原实例 equals 相等，副本会被合并回
 * 同一节点、完全失效（1.2.3 第一次修复即栽在这里）。因此副本必须追加一个额外的
 * 无副作用 BiomeFilter 打破值相等——副本由此成为独立图节点、与其他群系零共享，
 * 成环在机制上不再可能。详见 modify() 内注释。
 *
 * <p>JSON 格式（与 forge:add_features 完全一致，通常外面再包一层
 * bayou_blues:conditional 以维持可选依赖语义）：
 * <pre>
 * {
 *   "type": "bayou_blues:copy_features",
 *   "biomes": ["bayou_blues:bayou"],
 *   "features": ["environmental:cattails", ...],
 *   "step": "vegetal_decoration"
 * }
 * </pre>
 */
public record CopyFeaturesBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, GenerationStep.Decoration step) implements BiomeModifier {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, BayouBlues.MOD_ID);

    public static final RegistryObject<Codec<CopyFeaturesBiomeModifier>> COPY_FEATURES =
            SERIALIZERS.register("copy_features", () ->
                    RecordCodecBuilder.create(builder -> builder.group(
                            Biome.LIST_CODEC.fieldOf("biomes").forGetter(CopyFeaturesBiomeModifier::biomes),
                            PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(CopyFeaturesBiomeModifier::features),
                            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(CopyFeaturesBiomeModifier::step)
                    ).apply(builder, CopyFeaturesBiomeModifier::new)));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome)) {
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            for (Holder<PlacedFeature> holder : this.features) {
                PlacedFeature original = holder.value();
                // 独立副本需要两道保险，缺一不可：
                // 1) 新实例 —— 若 FeatureSorter 按对象身份建图，新实例即独立节点；
                // 2) 追加一个额外的 BiomeFilter —— 关键！PlacedFeature 是 record，equals 按
                //    值比较：仅"新实例 + 相同 placement"的副本与原实例 equals 相等，而
                //    FeatureSorter 用 Object2IntOpenHashMap#computeIfAbsent（按 equals）给
                //    特性分配图节点 ⇒ 副本会与其他群系里的原实例合并为同一节点，副本完全
                //    失效（1.2.3 第一次修复被架空、依然崩 Feature order cycle 的原因）。
                //    每个 placed feature 的 placement 链本就以 BiomeFilter 结尾，它是纯谓词，
                //    重复追加不改变任何放置行为，但使副本与所有既有 PlacedFeature 都不等
                //    ⇒ 必为独立图节点，与其他群系零共享 ⇒ 成环在机制上不再可能，
                //    与 Zeta 占位符的随机应用顺序彻底解耦（不再有任何可随机的交点）。
                List<PlacementModifier> placement = new ArrayList<>(original.placement());
                placement.add(BiomeFilter.biome());
                PlacedFeature copy = new PlacedFeature(original.feature(), List.copyOf(placement));
                generationSettings.addFeature(this.step, Holder.direct(copy));
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return COPY_FEATURES.get();
    }
}
