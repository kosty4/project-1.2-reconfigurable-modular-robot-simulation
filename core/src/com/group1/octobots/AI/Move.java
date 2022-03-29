package com.group1.octobots.AI;

import com.badlogic.gdx.math.MathUtils;
import com.group1.octobots.Module;
import com.group1.octobots.World;
import com.group1.octobots.physics.V3;

/**
 * Class used to represent individual {@link com.group1.octobots.Module} movement.
 */
public class Move {

    public Module getar() {
        return target;
    }

    private final Module target;
    private final Direction direction;
    private final MoveType type;
    private final float maxMagnitude;

    private final int cTS;

    public boolean causesFall;

    public Move(Module target, Direction direction, MoveType type, float maxMagnitude) {
        this.target = target;
        this.direction = direction;
        this.type = type;
        this.maxMagnitude = maxMagnitude;
        cTS = World.simulationTimeSteps;
        if (maxMagnitude != 0f && maxMagnitude != 1f) System.out.println(this);
    }

    public void apply(State s) {
        s.remove(target);
        target.applyMove(this);
        s.put(target);
    }

    public Direction getDirection() {
        return direction;
    }

    public MoveType getType() {
        return type;
    }

    public float getMaxMagnitude() {
        return maxMagnitude;
    }

    public V3 getResultingVector() {
        return new V3().add(direction.vector)
                       .scale(maxMagnitude)
                       .add(type.vector);
    }

    public int getTimeStep() {
        return cTS;
    }

    @Override
    public String toString() {
        if (MathUtils.isZero(maxMagnitude)) return "[]";
        return "[" + type.name().charAt(0) +
                direction.name().charAt(0) + "]";
    }

    public String toStringVerbose() {
        if (MathUtils.isZero(maxMagnitude)) return "[]";
        return "[" + type.name().charAt(0) +
                direction.name().charAt(0) +
                " mag: " + maxMagnitude + "," +
                ((causesFall) ? " causes fall]" : " does not cause fall]");
    }

    public static Move emptyMove(Module module) {
        return new Move(module, Direction.Front, MoveType.Translate, 0f);
    }
}
