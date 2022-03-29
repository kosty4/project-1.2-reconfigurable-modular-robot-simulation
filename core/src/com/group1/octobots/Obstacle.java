package com.group1.octobots;

import com.group1.octobots.physics.AABB;
import com.group1.octobots.physics.V3;
import com.group1.octobots.physics.WorldObject;

/**
 * Created on 4/20/2017.
 */
public class Obstacle implements WorldObject {

    private final String id;
    private final V3 position;
    private AABB aabb;

    public Obstacle(String id, float x, float y, float z) {
        this.id = id;
        this.position = new V3(x, y, z);
        this.aabb = new AABB(
                position.cpy(),
                position.cpy().add(V3.One));
    }

    @Override
    public AABB aabb() {
        return aabb;
    }

    public String ID() {
        return id;
    }

    /**
     * Returns the position of the {@link WorldObject}.
     */
    @Override
    public V3 pos() {
        return position;
    }
}
