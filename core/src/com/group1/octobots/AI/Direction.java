package com.group1.octobots.AI;

import com.group1.octobots.physics.V3;

public enum Direction {
    Front(V3.X),
    Right(V3.Y),
    Left(V3.NEG_Y),
    Back(V3.NEG_X);
    public final V3 vector;

    Direction(V3 vector){
        this.vector = vector;
    }

    public Direction opposite() {
        switch (this) {
            case Front:
            case Back:
                return Direction.Right;
            case Left:
            case Right:
                return Direction.Front;
        }
        return null;
    }

    @Override
    public String toString() {
        return "" + name().charAt(0);
    }
}