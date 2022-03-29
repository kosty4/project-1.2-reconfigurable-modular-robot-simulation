package com.group1.octobots.physics;

import com.badlogic.gdx.Gdx;
import com.group1.octobots.AI.ComputationalEngine;
import com.group1.octobots.Module;
import com.group1.octobots.World;
import java.util.ArrayList;
import java.util.List;

import static com.group1.octobots.utility.Constants.Physics.MAP_SIZE;

/**
 * Courtesy Kastya!!!!
 */
public class PhysicsEngine {


    public static int UPDATES_PER_STEP = 100;
    public static boolean SIMULATION_PAUSED = true;

    private List<ModuleBody> bodies = new ArrayList<>(World.modCount);

    private final ComputationalEngine computationalEngine;
    private int updates = UPDATES_PER_STEP;

    public PhysicsEngine(ComputationalEngine computationalEngine) {
        for (Module module : World.modules) {
            bodies.add(module.body());
        }
        this.computationalEngine = computationalEngine;
    }

    public void update(float delta) {
        // Update the physics
        for (ModuleBody body : bodies) {
            body.update(delta, updates);
        }

        if (SIMULATION_PAUSED) return;

        updates++;
        if (bodies.stream().allMatch(ModuleBody::isConverged)) {
            updates = 0;
            Gdx.app.postRunnable(computationalEngine::step);
        }
    }

    public static boolean isInsideBounds(float x, float y, float z) {
        return x >= 0 && x <= MAP_SIZE.x &&
                y >= 0 && y <= MAP_SIZE.y &&
                 z >= 0 && z <= MAP_SIZE.z;
    }

    public static boolean isInsideBounds(V3 position) {
        return isInsideBounds(position.x, position.y, position.z);
    }
}


//    public void WorkingDRAFTS for PUT method, use for stress testing, see which is fastest(Module module) {
//        int tag = module.tag();
//        float x = module.pos().x,
//                y = module.pos().y,
//                z = module.pos().z;
//
//        put(tag, module.pos()); // put the module at its truncated position;
//
//        boolean xIsWhole = isZero(x - (int) x);
//        boolean yIsWhole = isZero(y - (int) y);
//        boolean zIsWhole = isZero(z - (int) z);
//
//        V3 xPlus1 = module.pos().cpy().add(V3.X);
//        V3 yPlus1 = module.pos().cpy().add(V3.Y);
//        V3 zPlus1 = module.pos().cpy().add(V3.Z);
//        V3 xyPlus1 = module.pos().cpy().add(V3.X).add(V3.Y);
//        V3 xzPlus1 = module.pos().cpy().add(V3.X).add(V3.Z);
//        V3 yzPlus1 = module.pos().cpy().add(V3.Y).add(V3.Z);
//        V3 posPlus1 = module.pos().cpy().add(V3.One);
//
//        if (!xIsWhole) {
//            put(tag, xPlus1);
//            if (!yIsWhole) {
//                put(tag, yPlus1);
//                put(tag, xyPlus1);
//                if (!zIsWhole) {
//                    put(tag, zPlus1);
//                    put(tag, xzPlus1);
//                    put(tag, yzPlus1);
//                    put(tag, posPlus1);
//                    return;
//                }
//            } else {
//                if (!zIsWhole) {
//                    put(tag, zPlus1);
//                    put(tag, xzPlus1);
//                }
//            }
//        }
//        if (!yIsWhole) {
//            put(tag, yPlus1);
//            if (!zIsWhole) {
//                put(tag, zPlus1);
//                put(tag, yzPlus1);
//            }
//        }
//        if (!zIsWhole) put(tag, zPlus1);
//
//
//
//        if (!xIsWhole) put(tag, (int)(x+1), (int)y, (int)z);
//        if (!yIsWhole) put(tag, (int)x, (int)(y+1), (int)z);
//        if (!zIsWhole) put(tag, (int)x, (int)y, (int)(z+1));
//        if (!xIsWhole && !yIsWhole) put(tag, (int)(x+1), (int)(y+1), (int)z);
//        if (!xIsWhole && !zIsWhole) put(tag, (int)(x+1), (int)y, (int)(z+1));
//        if (!yIsWhole && !zIsWhole) put(tag, (int)x, (int)(y+1), (int)(z+1));
//        if (!xIsWhole && !yIsWhole && !zIsWhole) put(tag, (int)(x+1), (int)(y+1), (int)(z+1));
//    }