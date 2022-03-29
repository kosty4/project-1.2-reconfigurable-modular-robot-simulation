package com.group1.octobots.AI;

import com.group1.octobots.physics.V3;

/**
 * Created on 5/23/2017.
 */
public enum MoveType {
    Climb(V3.Z),
    Translate(V3.Zero),
    Descend(V3.NEG_Z);
    public final V3 vector;

    MoveType(V3 vector) {
        this.vector = vector;
    }

    @Override
    public String toString() {
        return "" + name().charAt(0);
    }
}
