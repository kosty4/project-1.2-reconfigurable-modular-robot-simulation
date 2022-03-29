package com.group1.octobots.physics;

import com.badlogic.gdx.utils.Queue;
import com.group1.octobots.AI.Move;
import com.group1.octobots.Module;
import com.group1.octobots.Target;
import com.group1.octobots.World;
import com.group1.octobots.physics.AABB.IntersectData;
import com.group1.octobots.rendering.ModuleGfxComp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.badlogic.gdx.math.MathUtils.*;
import static com.group1.octobots.utility.Constants.Physics.G;

/**
 * Update the position of this {@link WorldObject} over a period of time, according
 * to the forces it is subject to.
 **/

public class ModuleBody implements WorldObject {

    private static float normalSpeed = 0.5f;

    public int ti;
    public Map<Integer, V3> positionAt_ti = new HashMap<>();

    private final Module mod;
    private final ModuleGfxComp gfxComp;
    private final AABB aabb;

    private int currentFrame;
    private final Queue<V3> moveQueue;

    private V3 targetPosition = null;
    private V3 terminalMove;
    private float currentSpeed;

    private final V3 continuousPosition;
    private V3 velocity;

    private boolean falling = true;

    public ModuleBody(Module mod) {
        this.mod = mod;
        this.continuousPosition = new V3(mod.pos());
        this.velocity = new V3();
        this.aabb = new AABB(this);

        gfxComp = mod.gfxComponent();
        moveQueue = new Queue<>();

        positionAt_ti.put(ti, continuousPosition);
    }

    /**
     * Returns the position of the object.
     */
    @Override
    public V3 pos() {
        return continuousPosition;
    }

    @Override
    public AABB aabb() {
        return aabb;
    }

    public boolean isConverged() {
        return targetPosition == null
                && moveQueue.size == 0;
    }

    void update(float delta, int frame) {
        currentFrame = frame;

        if (falling) {
            velocity.z += G * delta;
            continuousPosition.z += velocity.z;
            aabb.update();

            if (continuousPosition.z <= 0) {
                continuousPosition.z = 0;
                velocity.z = 0;
                aabb.update();
                falling = false;
            } else {
                WorldObject objectUnder = getCollidedStableObjectUnder();
                if (objectUnder != null) {
                    continuousPosition.z = objectUnder.pos().z + 1;
                    velocity.z = 0;
                    aabb.update();
                    falling = false;
                }
            }
            mod.updateFromBody();
            gfxComp.updateModel(continuousPosition);
            return;
        }

        if (continuousPosition.equals(targetPosition, 0.1f)) {
            continuousPosition.set(targetPosition);
            aabb.update();
            gfxComp.updateModel(continuousPosition);
            if (targetPosition == terminalMove) {
                falling = checkFalling();
            }
            targetPosition = null;
        }

        if (targetPosition == null) {
            if (moveQueue.size != 0) {
                V3 newVector = moveQueue.removeFirst();
                targetPosition = newVector.add(continuousPosition);
            }
            return;
        }

        continuousPosition.lerp(targetPosition, delta * currentSpeed);
        aabb.update();
        gfxComp.updateModel(continuousPosition);
    }

    public void integrate(Move move) {
        currentSpeed = normalSpeed;

        V3 direction = move.getDirection().vector.cpy(),
                type = move.getType().vector.cpy();
        float magnitude = move.getMaxMagnitude();
        switch (move.getType()) {
            case Climb:
                terminalMove = direction.scale(magnitude);
                moveQueue.addLast(type);
                moveQueue.addLast(terminalMove);
                currentSpeed *= 2;
                break;
            case Descend:
                terminalMove = type.scale(magnitude);
                moveQueue.addLast(direction);
                moveQueue.addLast(terminalMove);
                currentSpeed *= 2;
                break;
            case Translate:
                terminalMove = direction.scale(magnitude);
                moveQueue.addLast(terminalMove);
        }
    }

    public void COOLOID_METH() {        System.out.println(moveQueue.size);
    }

    private boolean checkFalling() {
        return continuousPosition.z > 0 &&
                getCollidedStableObjectUnder() == null;
    }

    private WorldObject getCollidedStableObjectUnder() {
        List<WorldObject> objectsUnder = continuousPosition.z < 1f
                ? World.state.getObjectsAt(continuousPosition)
                : World.state.getObjectsAt(
                continuousPosition.x,
                continuousPosition.y,
                continuousPosition.z - 1);

        for (WorldObject wo : objectsUnder) {
            if (!(wo instanceof Target)) {
                if (!wo.equals(this.mod)) {
                    boolean isModule = wo instanceof Module;
                    AABB objectBox = isModule? ((Module) wo).body().aabb() : wo.aabb();
                    IntersectData id = this.aabb.collide(objectBox);
                    if (id.alignedXY() > 0 && id.thisMinMinusThatMax.z <= 0) {
                        if (!isModule || !((Module)wo).isFalling())
                            return wo;
                    }
                }
            }
        }
        return null;
    }

    public boolean assertLegalState() {
        boolean onGround = isZero(continuousPosition.z, 0.1f);
        boolean nothingUnderneath = getCollidedStableObjectUnder() == null;
        float collided = (float) World.state.getObjectsAt(continuousPosition).stream()
                .filter(wo -> !(wo instanceof Target) && !(wo == mod))
                .map(wo -> (wo instanceof Module) ? ((Module) wo).body().aabb() : wo.aabb())
                .mapToDouble(box -> box.collide(this.aabb).volumeOfOverlap())
                .sum();

        if (collided > 0) {
            System.out.println(mod + " Illegal position " + continuousPosition +
                                    " intersects others by " + collided);
        }
        if (!falling && (nothingUnderneath && !onGround)) {
            System.out.println(mod + " Illegal position " + continuousPosition +
                                    " is suspened and not falling.");

        }
        if (falling && (!nothingUnderneath || onGround)) {
            System.out.println(mod + " Illegal position " + continuousPosition +
                                    " is falling through objects.");
        }
        if (collided > 0 || (!falling && (nothingUnderneath && !onGround)) || (falling && (!nothingUnderneath || onGround))) {
            new Thread(() -> {
                gfxComp.toggleMoved();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gfxComp.toggleMoved();
            }).start();
            return false;
        }
        return true;
    }

    public boolean isFalling() {
        return falling;
    }
}
