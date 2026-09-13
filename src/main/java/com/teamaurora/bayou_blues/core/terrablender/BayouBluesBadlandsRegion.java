package com.teamaurora.bayou_blues.core.terrablender;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ModifiedVanillaOverworldBuilder;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

/**
 * Better Badlands 移植：恶地 TerraBlender Region。
 *
 * <h2>为什么这个 Region 默认不注册</h2>
 * 原版 Better Badlands <b>没有新增群系</b>。它在 1.16.5 里做的事是监听
 * {@code BiomeLoadingEvent}，在 <b>已有的</b> {@code minecraft:wooded_badlands} 里
 * 删掉原版橡树、换成自研的「小深色橡木」。1.20.1 里这件事的正确对应物是
 * {@code forge:remove_features} / {@code forge:add_features} biome modifier
 * （见 {@code data/bayou_blues/forge/biome_modifier/badlands_*.json}），
 * 而不是 TerraBlender Region —— Region 只能「提供参数点→群系」的映射，
 * 无法增删某个群系内部的特性。
 *
 * <p>更重要的是，<b>注册一个额外 Region 会破坏现有 bayou 的生成率</b>：
 * TerraBlender 的 uniqueness 噪声值域是 {@code [0, regionCount)}，
 * 每个 Region 按 weight 占比瓜分世界。当前
 * <pre>
 *   原版 overworld region  weight 10  →  50% 世界
 *   bayou_blues region     weight 10  →  50% 世界
 * </pre>
 * 一旦再注册本 Region（weight 10），三个 Region 各占 1/3，bayou 会从 50% 掉到 33%。
 * 而本 Region 能提供的东西（原版恶地参数→原版恶地群系，恒等映射）在功能上是零收益。
 *
 * <p>因此本类作为「脚手架」保留：若将来要新增真正的恶地子群系
 * （例如 {@code bayou_blues:badlands_flats}），把它的参数点加进
 * {@link #addBiomes}，并在 {@link BayouBluesTerraBlender#register()} 里解除注释即可，
 * 写法与 {@link BayouBluesRegion} 完全一致（复用 {@link ModifiedVanillaOverworldBuilder}）。
 */
public class BayouBluesBadlandsRegion extends Region {

    public BayouBluesBadlandsRegion(String name, int weight) {
        super(BayouBlues.location(name), RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // 目前是恒等映射：完整输出原版参数表（不替换任何群系）。
        // 这样若启用本 Region，被划入它的世界区域仍然生成正常的原版群系，
        // 只是把「恶地槽位」留在这里，方便后续挂接真正的恶地子群系。
        //
        // ⚠ 不要改成「只输出恶地那几个参数框」的独立 ParameterPointListBuilder：
        //   TerraBlender 的 findValuePositional 是「先按 uniqueness 噪声选中 Region，
        //   再只在该 Region 的参数点里找最近点（无视距离）」。参数框不完整时，
        //   整个世界都会被填成恶地。BayouBluesRegion 顶部注释记录了同一个坑。
        ModifiedVanillaOverworldBuilder builder = new ModifiedVanillaOverworldBuilder();
        // 预留：将来在这里加 builder.replaceBiome(Biomes.BADLANDS, BayouBluesBiomes.BADLANDS_FLATS);
        for (Pair<Climate.ParameterPoint, ResourceKey<Biome>> pair : builder.build()) {
            mapper.accept(pair);
        }
    }
}
