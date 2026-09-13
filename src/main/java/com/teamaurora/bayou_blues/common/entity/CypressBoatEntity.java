package com.teamaurora.bayou_blues.common.entity;

import com.teamaurora.bayou_blues.core.registry.BayouBluesEntities;
import com.teamaurora.bayou_blues.core.registry.BayouBluesItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * Cypress boat entity.
 * Behaves like a vanilla boat but drops the cypress boat item.
 */
public class CypressBoatEntity extends Boat {

    public CypressBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public CypressBoatEntity(Level level, double x, double y, double z) {
        this(BayouBluesEntities.CYPRESS_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public Item getDropItem() {
        return BayouBluesItems.CYPRESS_BOAT.get();
    }
}
