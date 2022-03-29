package com.group1.octobots.AI;

import com.group1.octobots.World;
import com.group1.octobots.physics.V3;

/**
 * Created on 6/25/2017.
 */
public class SavedState {
    public V3[] discretePositions = new V3[World.modCount];
    public V3[] continuousPositions = new V3[World.modCount];
}
