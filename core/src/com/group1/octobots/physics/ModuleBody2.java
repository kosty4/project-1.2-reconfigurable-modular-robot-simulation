/*
package com.group1.octobots.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Queue;
import com.group1.octobots.AI.Move;
import com.group1.octobots.AI.Direction;
import com.group1.octobots.AI.MoveType;
import com.group1.octobots.AI.State;
import com.group1.octobots.Module;
import com.group1.octobots.rendering.ModuleGfxComp;
import com.group1.octobots.utility.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.group1.octobots.physics.PhysicsEngine.isInsideBounds;
import static com.group1.octobots.utility.Constants.Physics.MOD_SIZE;

*/
/**
 * Created on 5/23/2017.
 *//*

public class ModuleBody2 {

    private final Module module;
    private final ModuleGfxComp gfx;

    private final V3 continuousPosition;
    private final V3 velocity;
    private final V3 acceleration;

    private final AABB aabb;

    private final Queue<Move> moves;
    private final int timeFrame;

    private V3 targetPosition;

    public ModuleBody2(Module module) {
        this.module = module;

        gfx = module.gfxComponent();
        continuousPosition = module.pos().cpy();
        velocity = V3.Zero.cpy();
        acceleration = V3.Zero.cpy();

        aabb = new AABB(continuousPosition.cpy(),
                continuousPosition.cpy().add(OBJECT_SIZE));
        moves = new Queue<>(1);
        timeFrame = 0;
    }

    public void update(float delta) {

    }

    public void integrate(Move move) {

    }

    public List<Move> getAvailableMoves() {
        //if falling can't move
        if (checkFalling()) return Collections.emptyList();
        //else
        List<Move> moves = new ArrayList<>(8);
        State s = State.get();
        V3 placeholderPosition = continuousPosition.cpy();
        //if on the ground then
        if (MathUtils.isZero(continuousPosition.z)) {
            for (Direction d : Direction.values()) {

                s.getModsAt(placeholderPosition.add(d.vector))
                        .stream()
                        .map(m -> m.body().aabb)
                        .filter(aabb.)
            }
        }

        State.get().getModsAt(
                continuousPosition.x,
                continuousPosition.y,
                continuousPosition.z - 1)
                .stream()
                .
    }

    private boolean checkFalling() {
        return !Utils.isWhole(continuousPosition.z);
    }

    public void updateBounds() {
        aabb.getMin().set(continuousPosition);
        aabb.getMax().set(continuousPosition).add(OBJECT_SIZE);
    }

    @SuppressWarnings("Duplicates")
    private List<Module> nearby() {
        List<Module> nearby = new ArrayList<>();
        V3 adjacentPosition = new V3(continuousPosition);

        for (Direction d : Direction.values()) {
            adjacentPosition.add(d.vector);
            if (isInsideBounds(adjacentPosition)) nearby.addAll(State.get().getModsAt(adjacentPosition));
            adjacentPosition.set(continuousPosition);
        }
        for (MoveType t : MoveType.values()) {
            adjacentPosition.add(t.vector);
            if (isInsideBounds(adjacentPosition)) nearby.addAll(State.get().getModsAt(adjacentPosition));
            adjacentPosition.set(continuousPosition);
        }
        return nearby;
    }

    public V3 pos() {
        return continuousPosition;
    }

    public AABB aabb() {
        return aabb;
    }
}
*/
