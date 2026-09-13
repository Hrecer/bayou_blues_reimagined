package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.entity.CypressBoatEntity;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BayouBluesEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BayouBlues.MOD_ID);

    public static final RegistryObject<EntityType<CypressBoatEntity>> CYPRESS_BOAT = ENTITIES.register("cypress_boat",
            () -> EntityType.Builder.<CypressBoatEntity>of(CypressBoatEntity::new, MobCategory.MISC)
                    .sized(1.375F, 0.5625F).clientTrackingRange(10).build("cypress_boat"));
}
