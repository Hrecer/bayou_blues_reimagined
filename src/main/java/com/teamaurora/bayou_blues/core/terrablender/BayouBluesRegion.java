package com.teamaurora.bayou_blues.core.terrablender;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.bayou_blues.core.BayouBlues;
import com.teamaurora.bayou_blues.core.registry.BayouBluesBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ModifiedVanillaOverworldBuilder;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class BayouBluesRegion extends Region {

    public BayouBluesRegion(String name, int weight) {
        super(BayouBlues.location(name), RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // ⚠ 不能用独立 ParameterPointListBuilder 参数框！TerraBlender 的 findValuePositional
        // 先用 uniqueness 噪声把世界划成大区域分给各 Region，再只在被选中 Region 的点里找
        // 最近点（无视距离）。独立参数框必然被零散铺到各种不合适的地形上（恶地旁的旱地森林）。
        // 正确做法：重写原版参数表，让 bayou 接管原版红树林沼泽（MANGROVE_SWAMP）的气候槽位
        // ——该槽位地形必然是浅水沼泽，正是 bayou 需要的生态位（浅水、温暖湿润）。
        //
        // 延伸到浅水：原版"海岸大陆度(coast continentalness) + 高侵蚀段 erosions[6]"是海滨滩涂
        // 槽位（BEACH），地形在海平面附近、被 bayou 地表规则（Y62/63 放水）淹没后即成浅水沼泽岸。
        // 只替换温和~炎热气候（BEACH 键本身就排除了 snow(beach)/desert），雪滩与沙漠海滩不受影响。
        // 岸边/沼泽旁仍可能遇到原版森林（erosions[5] 及内陆段），符合"森林只占小部分"的预期。
        ModifiedVanillaOverworldBuilder builder = new ModifiedVanillaOverworldBuilder();
        builder.replaceBiome(Biomes.MANGROVE_SWAMP, BayouBluesBiomes.BAYOU);

        for (Pair<Climate.ParameterPoint, ResourceKey<Biome>> pair : builder.build()) {
            Climate.ParameterPoint point = pair.getFirst();
            ResourceKey<Biome> biome = pair.getSecond();
            if (biome == Biomes.BEACH && isCoastalShallowWater(point)) {
                biome = BayouBluesBiomes.BAYOU;
            }
            mapper.accept(Pair.of(point, biome));
        }
    }

    /**
     * 海岸 + 高侵蚀（erosions[6] = 0.55~1.0）：原版在此槽位生成沙滩（温和~炎热），
     * 地形为海平面附近的平坦滩涂，是 bayou 浅水岸的延伸方向。
     */
    private static boolean isCoastalShallowWater(Climate.ParameterPoint point) {
        return point.continentalness().min() == -0.19F && point.continentalness().max() == -0.11F
                && point.erosion().min() == 0.55F && point.erosion().max() == 1.0F;
    }
}
