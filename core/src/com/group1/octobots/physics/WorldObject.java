package com.group1.octobots.physics;

/**
 * Interface for identifying the objects/structures in the Simulator.
 */
public interface WorldObject {

     /**
     * Returns the position of the {@link WorldObject}.
     */
    V3 pos();

    /**
     * Returns the {@link AABB} of the {@link WorldObject}.
     */
    AABB aabb();
}
