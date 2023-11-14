package com.miketies.create_ice_age.item;

import net.minecraft.world.item.Item;

public class FreezerItem extends Item {
    private int freezeTime = 0;
    public FreezerItem(Properties pProperties) {
        super(pProperties);
    }

    public int getFreezeTime() {
        return this.freezeTime;
    }

    public void setFreezeTime(int freezeTime) {
        this.freezeTime = freezeTime;
    }
}
