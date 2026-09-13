package com.teamaurora.bayou_blues.core.terrablender;

import com.teamaurora.bayou_blues.core.registry.BayouBluesBiomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class BayouBluesSurfaceRuleData {

    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);

    private static final SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isBayouBiome = SurfaceRules.isBiome(BayouBluesBiomes.BAYOU);
        
        SurfaceRules.ConditionSource isAboveY62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource isAboveY63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        
        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(
            SurfaceRules.ifTrue(isAtOrAboveWaterLevel, GRASS_BLOCK),
            DIRT
        );

        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                isBayouBiome,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(
                                isAboveY62,
                                SurfaceRules.ifTrue(
                                    SurfaceRules.not(isAboveY63),
                                    SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D),
                                        WATER
                                    )
                                )
                            ),
                            grassSurface
                        )
                    ),
                    SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT)
                )
            )
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(net.minecraft.world.level.block.Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}