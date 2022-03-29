package com.group1.octobots;

import com.group1.octobots.physics.AABB;
import com.group1.octobots.physics.V3;
import com.group1.octobots.physics.WorldObject;

/**
 * Created on 4/20/2017.
 */
public class Target implements WorldObject {

    private final String id;
    private final V3 pos;
    private final AABB aabb;

    public Target(String id, float x, float y, float z) {
        this.id = id;
        pos = new V3(x, y, z);
        aabb = new AABB(this);
    }

    /**
     * Returns the ID of the {@link WorldObject}.
     */
    public String ID() {
        return id;
    }

    /**
     * Returns the pos of the {@link WorldObject}.
     */
    @Override
    public V3 pos() {
        return pos;
    }

    /**
     * Returns the {@link AABB} of the {@link WorldObject}.
     */
    @Override
    public AABB aabb() {
        return aabb;
    }
}
