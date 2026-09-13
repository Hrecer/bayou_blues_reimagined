package com.teamaurora.bayou_blues.core.forge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.common.world.NoneBiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

/**
 * Conditional biome modifier: only decodes the wrapped modifier when the
 * condition passes (config gate / mod_loaded), keeping datapack references to
 * features of absent mods from being resolved (which would crash the DFU).
 * <p>
 * Forge 1.20.1 does not support the {@code condition} field in biome modifier JSONs
 * (that is a NeoForge feature). This serializer replicates it: when the condition
 * fails, the inner modifier is never decoded, so references to unloaded mods'
 * registry entries do not break datapack loading.
 * <p>
 * JSON format:
 * <pre>
 * {
 *   "type": "bayou_blues:conditional",
 *   "condition": { "type": "forge:mod_loaded", "modid": "environmental" },
 *   "modifier": { "type": "forge:add_features", ... }
 * }
 * </pre>
 * <p>
 * Implementation note: DFU's {@code Codec.dispatch} passes the whole element to
 * serializers backed by a {@link MapCodec} (fields laid flat beside "type"), but
 * plain {@link Codec}s receive {@code map.get("value")} (null for flat JSON).
 * This must therefore be a real MapCodec registered via {@code codec()}.
 */
public final class ConditionalBiomeModifier implements BiomeModifier {

    /**
     * The conditional logic as a MapCodec: the inner modifier is only decoded
     * when the condition passes.
     */
    private static final MapCodec<BiomeModifier> CODEC = new MapCodec<>() {
        @Override
        public <T> DataResult<BiomeModifier> decode(DynamicOps<T> ops, MapLike<T> input) {
            // Biome modifier files are always loaded through JsonOps (RegistryDataLoader),
            // so the map values are JsonElements.
            final T conditionValue = input.get("condition");
            if (conditionValue == null) {
                return DataResult.error(() -> "Missing condition in conditional biome modifier");
            }
            final JsonObject conditionJson;
            try {
                conditionJson = ((JsonElement) conditionValue).getAsJsonObject();
            } catch (ClassCastException | IllegalStateException e) {
                return DataResult.error(() -> "Condition is not a JSON object: " + e.getMessage());
            }
            final ICondition condition = CraftingHelper.getCondition(conditionJson);
            if (!condition.test(ICondition.IContext.EMPTY)) {
                return DataResult.success(NoneBiomeModifier.INSTANCE);
            }
            final T modifierValue = input.get("modifier");
            if (modifierValue == null) {
                return DataResult.error(() -> "Missing modifier in conditional biome modifier");
            }
            return BiomeModifier.DIRECT_CODEC.decode(ops, modifierValue).map(Pair::getFirst);
        }

        @Override
        public <T> RecordBuilder<T> encode(BiomeModifier input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            // Encoding is only needed for datagen, which this mod does not use.
            return prefix;
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.of(
                    ops.createString("type"),
                    ops.createString("condition"),
                    ops.createString("modifier"));
        }
    };

    public static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, BayouBlues.MOD_ID);

    public static final RegistryObject<Codec<? extends BiomeModifier>> CONDITIONAL =
            SERIALIZERS.register("conditional", () -> CODEC.codec());

    private final BiomeModifier modifier;

    private ConditionalBiomeModifier(BiomeModifier modifier) {
        this.modifier = modifier;
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        this.modifier.modify(biome, phase, builder);
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CONDITIONAL.get();
    }
}
