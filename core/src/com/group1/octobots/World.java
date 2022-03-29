package com.group1.octobots;

import com.group1.octobots.AI.ComputationalEngine;
import com.group1.octobots.AI.State;
import com.group1.octobots.physics.PhysicsEngine;
import com.group1.octobots.utility.FileHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Big bad Access class.
 */
public class World {

    public static OctoBots application;

    public static int modCount;

    public static final List<Module> modules     = new ArrayList<>(16);
    public static final List<Obstacle> obstacles = new ArrayList<>(16);
    public static final List<Target> targets     = new ArrayList<>(16);

    public static PhysicsEngine physicsEngine;
    public static ComputationalEngine computationalEngine;

    public static State state;
    public static int simulationTimeSteps;

    public static void init() {
        FileHandler.readInputFiles();
        // Shrink the lists to match actual number of elements.
        state = State.init(modules, obstacles, targets);
//        makeDataStructures();

        computationalEngine = new ComputationalEngine(state);
        physicsEngine = new PhysicsEngine(computationalEngine);
    }
}
/*
private static void makeDataStructures() {
        // Link modules together.
        for (Module mod : modules) {
            V3 modPosition = mod.pos();
            V3 modOffsets = modPosition.decimals();

            V3 placeholderPosition = new V3();


            State.get().getModsAt(placeholderPosition.set(modPosition).add(V3.X))
                    .select(m -> m.body().aabb().intersects(mod.body().aabb()))
                    .forEach(mod::attach);

            State.get().getModsAt(placeholderPosition.set(modPosition).add(V3.Y))
                    .select(m -> m.body().aabb().intersects(mod.body().aabb()))
                    .forEach(mod::attach);

            State.get().getModsAt(placeholderPosition.set(modPosition).add(V3.Z))
                    .select(m -> m.body().aabb().intersects(mod.body().aabb()))
                    .forEach(mod::attach);

            State.get().getModsAt(placeholderPosition.set(modPosition).add(V3.NEG_X))
                    .select(m -> m.body().aabb().intersects(mod.body().aabb()))
                    .forEach(mod::attach);

            State.get().getModsAt(placeholderPosition.set(modPosition).add(V3.NEG_Y))
                    .select(m -> m.body().aabb().intersects(mod.body().aabb()))
                    .forEach(mod::attach);

            State.get().getModsAt(placeholderPosition.set(modPosition).add(V3.NEG_Z))
                    .select(m -> m.body().aabb().intersects(mod.body().aabb()))
                    .forEach(mod::attach);
            // TODO: 4/30/2017 It's nice and all but either go with AABBtree or encapsulate this.
        }

    }
 */