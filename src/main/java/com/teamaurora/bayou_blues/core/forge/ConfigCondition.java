package com.teamaurora.bayou_blues.core.forge;

import com.google.gson.JsonObject;
import com.teamaurora.bayou_blues.core.BayouBlues;
import com.teamaurora.bayou_blues.core.BayouBluesConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

/**
 * Allows datapack JSONs (recipes, biome modifiers) to test boolean common config values.
 *
 * JSON format:
 * <pre>
 * { "type": "bayou_blues:config", "path": "do_lilies_spawn" }
 * </pre>
 */
public final class ConfigCondition implements ICondition {
    public static final String NAME = "config";

    private final String path;

    public ConfigCondition(String path) {
        this.path = path;
    }

    @Override
    public ResourceLocation getID() {
        return BayouBlues.location(NAME);
    }

    @Override
    public boolean test(IContext context) {
        return BayouBluesConfig.getBooleanConfig(this.path);
    }

    @Override
    public String toString() {
        return "config(\"" + this.path + "\")";
    }

    public static class Serializer implements IConditionSerializer<ConfigCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, ConfigCondition value) {
            json.addProperty("path", value.path);
        }

        @Override
        public ConfigCondition read(JsonObject json) {
            return new ConfigCondition(json.get("path").getAsString());
        }

        @Override
        public ResourceLocation getID() {
            return BayouBlues.location(NAME);
        }
    }
}
