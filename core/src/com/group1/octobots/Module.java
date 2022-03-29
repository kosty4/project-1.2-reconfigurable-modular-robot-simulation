package com.group1.octobots;

import com.group1.octobots.AI.Move;
import com.group1.octobots.physics.AABB;
import com.group1.octobots.physics.ModuleBody;
import com.group1.octobots.physics.V3;
import com.group1.octobots.physics.WorldObject;
import com.group1.octobots.rendering.GraphicsComponent;
import com.group1.octobots.rendering.ModuleGfxComp;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a Robotic Module.
 */
public class Module implements WorldObject {

    public int ti;
    public Map<Integer, V3> positionAt_ti = new HashMap<>();

    private final AABB aabb;
    private final ModuleBody body;
    private final ModuleGfxComp gfxComponent;
    private final String id;
    private final int tag;

    private final V3 discretePosition = new V3();

    private boolean willFall;
    /*
        private final List<Module> connections = new ArrayList<>(8);
    */

    public Module(String id, float x, float y, float z, int tag) {
        this.id = id;
        this.tag = tag;
        discretePosition.set(x, y, z);
        aabb = new AABB(this);
        gfxComponent = new ModuleGfxComp(discretePosition);
        body = new ModuleBody(this);

        positionAt_ti.put(ti, discretePosition);
    }

    public void applyMove(Move m) {
        //toDo stuff
        discretePosition.add(m.getResultingVector());
        aabb.update();
        body.integrate(m);
        positionAt_ti.put(ti++, discretePosition);
    }

    public boolean isMoving() {
        return !body.isConverged();
    }

    /**
     * Returns the position of the {@link WorldObject}.
     */
    @Override
    public V3 pos() {
        return discretePosition;
    }

    public AABB aabb() {
        return aabb;
    }

    public boolean isFalling() {
        return body().isFalling();
    }

    public ModuleBody body() {
        return body;
    }

    public void updateFromBody() {
        World.state.remove(this);
        discretePosition.set(body.pos());
        aabb.update();
        World.state.put(this);
    }

    /**
     * Returns the {@link GraphicsComponent} of the {@link Module}.
     */
    public ModuleGfxComp gfxComponent() {
        return gfxComponent;
    }

    /**
     * Returns the {@link #tag} of the {@link Module}.
     */
    public int tag() {
        return tag;
    }

    /**
     * Returns the ID of the {@link WorldObject}.
     */
    public String ID() {
        return id;
    }

    @Override
    public String toString() {
        return "Module " + id + "(" + tag + "): " + discretePosition.toString()
                + ((isFalling()) ? " falling" : "");
    }
}
