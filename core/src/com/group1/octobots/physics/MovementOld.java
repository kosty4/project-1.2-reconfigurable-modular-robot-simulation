/*
package com.group1.octobots.physics;

import com.badlogic.gdx.math.MathUtils;
import com.group1.octobots.AI.Direction;
import com.group1.octobots.AI.Move;
import com.group1.octobots.AI.MoveType;
import com.group1.octobots.AI.State;
import com.group1.octobots.Module;
import java.util.ArrayList;
import java.util.List;

import static com.group1.octobots.utility.Constants.Physics.MAP_SIZE;
import static com.group1.octobots.utility.Constants.Physics.OBJECT_SIZE;
import static com.group1.octobots.utility.Constants.Physics.TIPPING_PERCENTAGE;
import static java.lang.Math.*;

*/
/**
 * Created by Jackz on 04.05.2026.
 *//*

@SuppressWarnings("Duplicates")
public class Movement {

    private static final Movement instance = new Movement();

    public static Movement get() {
        return instance;
    }

    //Change in Move : setMaxMagnitude
    //Add in Constant , Physics Percentage

    //TODO Put a movement object into every module so we just have to call module.movement.getAvailableMoves() ??
    //public Movement(Module module){}
    private Movement(){
    }


    public List<Move> getAvailableMoves(Module module){
        float MAXIMUM_MAGNITUDE = 1f ;
        List<Move> availableMoves = new ArrayList<>();

        boolean aboveGround = module.pos().z >= 1;

        // DICKRECTION FRONT
        if (module.pos().x <= MAP_SIZE.x - 1) {
            Move Translate_Front = new Move(module, Direction.Front, MoveType.Translate,MAXIMUM_MAGNITUDE);
            if (move(module,Translate_Front)){
                availableMoves.add(Translate_Front);
            }
            Move Climb_Front = new Move(module,Direction.Front,MoveType.Climb,MAXIMUM_MAGNITUDE);
            if (move(module,Climb_Front)){
                availableMoves.add(Climb_Front);
            }
            if (aboveGround) {
                Move Descend_Front = new Move(module,Direction.Front,MoveType.Descend,MAXIMUM_MAGNITUDE);
                if (move(module,Descend_Front)){
                    availableMoves.add(Descend_Front);
                }
            }
        }

        // DICKRECTION BACK
        if (module.pos().x >= 1) {
            Move Translate_Back = new Move(module,Direction.Back,MoveType.Translate,MAXIMUM_MAGNITUDE);
            if (move(module,Translate_Back)){
                availableMoves.add(Translate_Back);
            }
            Move Climb_Back = new Move(module,Direction.Back,MoveType.Climb,MAXIMUM_MAGNITUDE);
            if (move(module,Climb_Back)){
                availableMoves.add(Climb_Back);
            }
            if (aboveGround) {
                Move Descend_Back = new Move(module,Direction.Back,MoveType.Descend,MAXIMUM_MAGNITUDE);
                if (move(module,Descend_Back)){
                    availableMoves.add(Descend_Back);
                }
            }
        }

        // DICKRECTION RIGHT
        if (module.pos().y <= MAP_SIZE.y - 1) {
            Move Translate_Right = new Move(module,Direction.Right,MoveType.Translate,MAXIMUM_MAGNITUDE);
            if (move(module,Translate_Right)){
                availableMoves.add(Translate_Right);
            }
            Move Climb_Right = new Move(module,Direction.Right,MoveType.Climb,MAXIMUM_MAGNITUDE);
            if (move(module,Climb_Right)){
                availableMoves.add(Climb_Right);
            }
            if (aboveGround) {
                Move Descend_Right = new Move(module,Direction.Right,MoveType.Descend,MAXIMUM_MAGNITUDE);
                if (move(module,Descend_Right)){
                    availableMoves.add(Descend_Right);
                }
            }
        }

        // DICKRECTION LEFT
        if (module.pos().y >= 1) {
            Move Translate_Left = new Move(module,Direction.Left,MoveType.Translate,MAXIMUM_MAGNITUDE);
            if (move(module,Translate_Left)){
                availableMoves.add(Translate_Left);
            }
            Move Climb_Left = new Move(module,Direction.Left,MoveType.Climb,MAXIMUM_MAGNITUDE);
            if (move(module,Climb_Left)){
                availableMoves.add(Climb_Left);
            }
            if (aboveGround) {
                Move Descend_Left = new Move(module,Direction.Left,MoveType.Descend,MAXIMUM_MAGNITUDE);
                if (move(module,Descend_Left)){
                    availableMoves.add(Descend_Left);
                }
            }
        }

        return availableMoves;
    }


    private boolean move(Module module, Move move) {
        float magnitude;
        if (!isSomething(module,V3.Y) &&
                !isSomething(module,V3.NEG_Y) &&
                !isSomething(module,V3.X) &&
                !isSomething(module,V3.NEG_X) &&
                MathUtils.isZero(module.pos().z)) {//No Modules around it && on ground floor
//            System.out.println("Nothing around us!");
            return false;
        } else {
            switch (move.getType()) {
                case Climb:
                    if (!isSomething(module, V3.Y) && !isSomething(module, V3.NEG_Y) && !isSomething(module, V3.X) && !isSomething(module, V3.NEG_X)) {
                        return false;    //Nothing to climb on
                    } else {
                        if (isSomething(module,V3.Z)){   //Can never climb if there is something sitting on us
                            return false;
                        } else {
                            switch (move.getDirection()) {
                                case Front:
                                    if (module.pos().x == MAP_SIZE.x) {
                                        return false;
                                    } else {
//                                    if ((isSomething(module, V3.X) && (isSomething(module, V3.NEG_Z))) || (MathUtils.isZero(module.pos().z) && isSomething(module,V3.X))) {
//                                        if (isSomething(module, V3.X) && (isSomething(module, V3.NEG_Z) || MathUtils.isZero(module.pos().z))) {   //if there is something to climb on and we are not falling  Fixme isSomething returns true if we are falling and there is are modules below us in the range of OBJECT_SIZE
                                        if (isSomething(module,V3.X)){
                                            magnitude = calculateMagnitude(module, move);
                                            if (!MathUtils.isZero(magnitude)) {
                                                move.setMaxMagnitude(magnitude);
                                                return true;
                                            } else
                                                return false;
                                        } else
                                            return false;
                                    }
                                case Back:
                                    if (MathUtils.isZero(module.pos().x)) {
                                        return false;
                                    } else {
                                        if (isSomething(module, V3.NEG_X) && (isSomething(module, V3.NEG_Z) || MathUtils.isZero(module.pos().z))) {
                                            magnitude = calculateMagnitude(module, move);
                                            if (!MathUtils.isZero(magnitude)) {
                                                move.setMaxMagnitude(magnitude);
                                                return true;
                                            } else
                                                return false;
                                        } else
                                            return false;
                                    }
                                case Right:
                                    if (module.pos().y == MAP_SIZE.y) {
                                        return false;
                                    } else {
                                        if (isSomething(module, V3.Y) && (isSomething(module, V3.NEG_Z) || MathUtils.isZero(module.pos().z))) {
                                            magnitude = calculateMagnitude(module, move);
                                            if (!MathUtils.isZero(magnitude)) {
                                                move.setMaxMagnitude(magnitude);
                                                return true;
                                            } else
                                                return false;
                                        } else
                                            return false;
                                    }
                                case Left:
                                    if (MathUtils.isZero(module.pos().y)) {
                                        return false;
                                    } else {
                                        if (isSomething(module, V3.NEG_Y) && (isSomething(module, V3.NEG_Z) || MathUtils.isZero(module.pos().z))) {
                                            magnitude = calculateMagnitude(module, move);
                                            if (!MathUtils.isZero(magnitude)) {
                                                move.setMaxMagnitude(magnitude);
                                                return true;
                                            } else
                                                return false;
                                        } else
                                            return false;
                                    }
                            }
                        }
                    }
                case Translate:
                    if (MathUtils.isZero(module.pos().z)){
                        return false;
                    } else {
                        switch (move.getDirection()) {
                            case Front:
                                if (module.pos().x == MAP_SIZE.x) {   //For out of aabb error
                                    return false;
                                } else {
                                    if (isSomething(module, V3.NEG_Z) && isSomething(module, new V3(V3.X).add(V3.NEG_Z)) && !getPressedDown(module)){  //Fixme check if our minPos.z == maxPos.z of the module below us but may be unnecessary
//                                    if (module.aabb().sitsOn() > TIPPING_PERCENTAGE && !getPressedDown(module)) {
//                                        if (!getPressedDown(module)) {  //We will never get to a position where we won't be sitting on a module
                                        magnitude = calculateMagnitude(module, move);
                                        if (!MathUtils.isZero(magnitude)) {
                                            move.setMaxMagnitude(magnitude);
                                            return true;
                                        } else
                                            return false;
                                    } else
                                        return false;
//                                    } else
//                                        return false;
                                }
                            case Back:
                                if (MathUtils.isZero(module.pos().x)) {
                                    return false;
                                } else {
                                    if ((isSomething(module, V3.NEG_Z) && isSomething(module, new V3(V3.NEG_X).add(V3.NEG_Z))) && !getPressedDown(module)) {
//                                        if (module.aabb().sitsOn() > TIPPING_PERCENTAGE && !getPressedDown(module)) {
                                        magnitude = calculateMagnitude(module, move);
                                        if (!MathUtils.isZero(magnitude)) {
                                            move.setMaxMagnitude(magnitude);
                                            return true;
                                        } else
                                            return false;
                                    } else
                                        return false;
//                                    } else
//                                        return false;
                                }
                            case Right:
                                if (module.pos().y == MAP_SIZE.y) {
                                    return false;
                                } else {
                                    if ((isSomething(module, V3.NEG_Z) && isSomething(module, new V3(V3.Y).add(V3.NEG_Z))) && !getPressedDown(module)) {
//                                        if (module.aabb().sitsOn() > TIPPING_PERCENTAGE && !getPressedDown(module)) {
                                        magnitude = calculateMagnitude(module, move);
                                        if (!MathUtils.isZero(magnitude)) {
                                            move.setMaxMagnitude(magnitude);
                                            return true;
                                        } else
                                            return false;
                                    } else
                                        return false;
//                                    } else
//                                        return false;
                                }
                            case Left:
                                if (MathUtils.isZero(module.pos().y)) {
                                    return false;
                                } else {
                                    if ((isSomething(module, V3.NEG_Z) && isSomething(module, new V3(V3.NEG_Y).add(V3.NEG_Z))) && !getPressedDown(module)) {
//                                        if (module.aabb().sitsOn() > TIPPING_PERCENTAGE && !getPressedDown(module)) {
                                        magnitude = calculateMagnitude(module, move);
                                        if (!MathUtils.isZero(magnitude)) {
                                            move.setMaxMagnitude(magnitude);
                                            return true;
                                        } else
                                            return false;
                                    } else
                                        return false;
//                                    } else
//                                        return false;
                                }
                        }
                    }
                case Descend:           //Same Conditions for each direction, except differences in magnitude

                    if (MathUtils.isZero(module.pos().z)) {
                        return false;    //Can't descend on ground floor
                    } else {
//                        if (isSomething(module, V3.NEG_Z) && module.aabb().sitsOn() >= TIPPING_PERCENTAGE && !getPressedDown(module)) { //sitOn() for additional checking, might be unecessary
                        if (getPressedDown(module)) {
                            return false;
                        } else {
                            switch (move.getDirection()) {
                                case Front:
                                    magnitude = calculateMagnitude(module, move);
                                    if (!MathUtils.isZero(magnitude)) {
                                        move.setMaxMagnitude(magnitude);
                                        return true;
                                    } else
                                        return false;
                                case Back:
                                    magnitude = calculateMagnitude(module, move);
                                    if (!MathUtils.isZero(magnitude)) {
                                        move.setMaxMagnitude(magnitude);
                                        return true;
                                    } else
                                        return false;
                                case Left:
                                    magnitude = calculateMagnitude(module, move);
                                    if (!MathUtils.isZero(magnitude)) {
                                        move.setMaxMagnitude(magnitude);
                                        return true;
                                    } else
                                        return false;
                                case Right:
                                    magnitude = calculateMagnitude(module, move);
                                    if (!MathUtils.isZero(magnitude)) {
                                        move.setMaxMagnitude(magnitude);
                                        return true;
                                    } else
                                        return false;
                            }
                        }
                    }
            }
        }
        System.out.println("Unknown case!!!!!┌∩┐(⋟﹏⋞)┌∩┐");
        return false;
    }

    private boolean isTouching(List<Module> mod1, List<Module> mod2, V3 direction){
        if (direction == V3.X || direction == V3.NEG_X){
            for (Module module1 : mod1 ) {
                for ( Module module2: mod2 ) {
                    if (MathUtils.isZero(module1.aabb().collide(module2.body().aabb()).xDist())){
                        return true;
                    }
                }
            }
        }

        if (direction == V3.Y || direction == V3.NEG_Y){
            for (Module module1 : mod1 ) {
                for ( Module module2: mod2 ) {
                    if (MathUtils.isZero(module1.aabb().collide(module2.body().aabb()).yDist())){
                        return true;
                    }
                }
            }

        }
        return false;
    }


    //Checks if there is something at modules position + V3 vector
    //If the position is out of aabb, returns false  => take into consideration the aabb when using!
    private boolean isSomething(Module module, V3 v) {   //Fixme redundant to check for z-axis?
        return !(module.pos().x + v.x < 0f || module.pos().x + v.x > MAP_SIZE.x ||
                module.pos().y + v.y < 0f || module.pos().y + v.y > MAP_SIZE.y ||
                module.pos().z + v.z < 0f || module.pos().z + v.z > MAP_SIZE.z) &&
                State.get().getModsAt(module.pos().cpy().add(v)).size() != 0;
*/
/* System.out.println("new V3(module.pos()).add(v).x = " + new V3(module.pos()).add(v).x);
            System.out.println("new V3(module.pos()).add(v).y = " + new V3(module.pos()).add(v).y);
            System.out.println("new V3(module.pos()).add(v).z = " + new V3(module.pos()).add(v).z);*//*

//            System.out.println("___________________________________________");
    }

    */
/**
     * Helper method for the movement method. Finds the smallest distance between 2 collections of modules.
     *//*

    private float spaceBetween(List<Module> a1, List<Module> a2, V3 direction){
        float tmp;
        float d1 = MAP_SIZE.x;   //Initiallized to biggest distance possible
        float d2 = MAP_SIZE.y;
        if (direction == V3.X || direction == V3.NEG_X) {
            for (Module mod : a1) {
                for (Module modu : a2) {
                    tmp = mod.aabb().collide(modu.body().aabb()).xDist();
                    if ( tmp < d1 )
                        d1 = tmp;
                }
            }
        }
        if (direction == V3.Y || direction == V3.NEG_Y) {
            for (Module mod : a1) {
                for (Module modu : a2) {
                    tmp = mod.aabb().collide(modu.body().aabb()).yDist();
                    if ( tmp < d2 )
                        d2 = tmp;
                }
            }
        }
        return max(d1, d2);
    }

    //FIXME change V3 to Enum X , Y ??
    public float spaceBetween(Module mod , List<Module> a1 , V3 direction){
        float tmp;
        float d1 = MAP_SIZE.x;   //Initiallized to biggest distance possible
        float d2 = MAP_SIZE.y;
        if (direction == V3.X || direction == V3.NEG_X) {
            for (Module modu : a1) {
                tmp = mod.aabb().collide(modu.body().aabb()).xDist();   //TODO Relocate y- and xDistance(AABB , AABB ) method
                if ( tmp < d1 )
                    d1 = tmp;
            }
        }
        if (direction == V3.Y || direction == V3.NEG_Y) {
            for (Module modu : a1) {
                tmp = mod.aabb().collide(modu.body().aabb()).yDist();
                if ( tmp < d2 )
                    d2 = tmp;
            }
        }
        return max(d1, d2);
    }

    //Careful : If a1 is empty, we return the biggest distance possible : Map_Size !!
    private float spaceBetween(AABB mod, List<Module> a1, V3 direction){
        float tmp;
        float d1 = MAP_SIZE.x;   //Initiallized to biggest distance possible
        float d2 = MAP_SIZE.y;
        if (direction == V3.X || direction == V3.NEG_X) {
            for (Module modu : a1) {
                tmp = mod.collide(modu.body().aabb()).xDist();   //TODO Relocate y- and xDistance(AABB , AABB ) method
                if ( tmp < d1 )
                    d1 = tmp;
            }
        }
        if (direction == V3.Y || direction == V3.NEG_Y) {
            for (Module modu : a1) {
                tmp = mod.collide(modu.body().aabb()).yDist();
                if ( tmp < d2 )
                    d2 = tmp;
            }
        }
        return max(d1, d2);
    }

    private boolean getPressedDown(Module module){
        if ( isSomething(module,V3.Z)){
            float area = 0f;
            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.Z))) {
                area += mod.aabb().sitsOn(module.body().aabb());
            }
            //Fixme >= ?
            return area > TIPPING_PERCENTAGE;
        } else
            return false;
    }

    private float calculateMagnitude(Module module, Move move){
        float reach;
        float magnitude = 0f;
        V3 vec;
        AABB futurePos;
        switch (move.getType()) {
            case Translate:
                switch (move.getDirection()) {
                    case Front:
                        if ( isTouching(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)),State.get().getModsAt(new V3(module.pos()).add(V3.X).add(V3.NEG_Z)) , V3.X)) {   //Fixme Phase 3 : if 2 modules are touching, but not aligned
                            magnitude = 1f; //Maximum possible magnitude that we can move now
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.X))) {
//                            reach = module.aabb().xIntersection(mod.body().aabb()) - TIPPING_PERCENTAGE / 100;
                                reach = module.aabb().collide(mod.body().aabb()).xDist();
                                vec = new V3(reach, 0, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {   //Fixme Phase 3 : If we don't sit on it, check until reach = 0 if it sits on a module  || to see if there is a space where we will be sitting on it
                                    magnitude = min(magnitude, reach);  //Fixme Phase 3 : max or min?   Min : no way of colliding with another module // Max : Possibility of colliding with another module
                                }
                            }
                        } else {  //Can only translate as much relative to the modules below us
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {
                                reach = OBJECT_SIZE - module.aabb().xIntersection(mod.body().aabb());
                                vec = new V3(reach, 0, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {
                                    magnitude = max(magnitude,reach);
                                }
                            }
                        }
                        return magnitude;
                    case Back:
                        if ( isTouching(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)),State.get().getModsAt(new V3(module.pos()).add(V3.NEG_X).add(V3.NEG_Z)) , V3.NEG_X)) {
                            magnitude = 1f;
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_X))) {
                                reach = module.aabb().collide(mod.body().aabb()).xDist();
                                vec = new V3(-reach, 0, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {
                                    magnitude = min(magnitude, reach);
                                }
                            }
                        } else {
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {
                                reach = OBJECT_SIZE - module.aabb().xIntersection(mod.body().aabb());
                                vec = new V3(-reach, 0, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {
                                    magnitude = max(magnitude,reach);
                                }
                            }
                        }
                        return magnitude;
                    case Left:
                        if ( isTouching(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)),State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Y).add(V3.NEG_Z)) , V3.NEG_Y)) {
                            magnitude = 1f;
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Y))) {
                                reach = module.aabb().collide(mod.body().aabb()).yDist();
                                vec = new V3(0, -reach, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {
                                    magnitude = min(magnitude, reach);
                                }
                            }
                        } else {
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {
                                reach = OBJECT_SIZE - module.aabb().yIntersection(mod.body().aabb());
                                vec = new V3(0, -reach, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {
                                    magnitude = max(magnitude,reach);
                                }
                            }
                        }
                        return magnitude;
                    case Right:
                        if ( isTouching(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)),State.get().getModsAt(new V3(module.pos()).add(V3.Y).add(V3.NEG_Z)) , V3.Y)) {
                            magnitude = 1f;
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.Y))) {
                                reach = module.aabb().collide(mod.body().aabb()).yDist();
                                vec = new V3(0, reach, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {
                                    magnitude = min(magnitude, reach);
                                }
                            }
                        } else {
                            for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {
                                reach = OBJECT_SIZE - module.aabb().yIntersection(mod.body().aabb());
                                vec = new V3(0, reach, 0);
                                futurePos = new AABB(module.aabb().getMin().add(vec), module.aabb().getMax().add(vec));
                                if (futurePos.sitsOn() > TIPPING_PERCENTAGE) {
                                    magnitude = max(magnitude,reach);
                                }
                            }
                        }
                        return magnitude;
                }
            case Climb:  //todo fix aabbEdit
                AABB up = new AABB(module.aabb().getMin().add(V3.Z), module.aabb().getMax().add(V3.Z));  //position of our module + 1 in the z-axis
                switch (move.getDirection()) {
                    case Front:
                        float spaceUpFront = spaceBetween(up, State.get().getModsAt(new V3(module.pos()).add(V3.X).add(V3.Z)), V3.X);
                        if (spaceUpFront >= TIPPING_PERCENTAGE / 100) { //Enough space to sit on
                            return min(spaceUpFront, OBJECT_SIZE);
                        } else {
                            return 0f;
                        }
                    case Back:
                        float spaceUpBack = spaceBetween(up, State.get().getModsAt(new V3(module.pos()).add(V3.NEG_X).add(V3.Z)), V3.NEG_X);
                        if (spaceUpBack >= TIPPING_PERCENTAGE / 100) { //Enough space to sit on
                            return min(spaceUpBack, OBJECT_SIZE);
                        } else {
                            return 0f;
                        }
                    case Left:
                        float spaceUpLeft = spaceBetween(up, State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Y).add(V3.Z)), V3.NEG_Y);
                        if (spaceUpLeft >= TIPPING_PERCENTAGE / 100) { //Enough space to sit on
                            return min(spaceUpLeft, OBJECT_SIZE);
                        } else {
                            return 0f;
                        }
                    case Right:
                        float spaceUpRight = spaceBetween(up, State.get().getModsAt(new V3(module.pos()).add(V3.Y).add(V3.Z)), V3.Y);
                        if (spaceUpRight >= TIPPING_PERCENTAGE / 100) { //Enough space to sit on
                            return min(spaceUpRight, OBJECT_SIZE);
                        } else {
                            return 0f;
                        }
                }
            case Descend:  //FixMe Calculate maximum magnitude with minimum magnitude
                if (MathUtils.isZero(module.pos().z)) {  //Can't descend if on the ground.
                    return 0f;
                } else {
                    switch (move.getDirection()) {
                        case Front:
                           */
/* if (module.pos().x > MAP_SIZE) {
                                return 0f;
                            } else {*//*

                            float spaceDownFront = spaceBetween(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)), State.get().getModsAt(new V3(module.pos()).add(V3.X).add(V3.NEG_Z)), V3.X);
                            float maxXIntersection = 0f;
                            if (spaceDownFront >= OBJECT_SIZE) {
                                for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {
                                    maxXIntersection = max(maxXIntersection, -module.aabb().collide(mod.body().aabb()).xDist());  //FIXME shouldnt we take min?
                                }
//                            return min(OBJECT_SIZE, spaceDownFront - (OBJECT_SIZE - maxXIntersection)); //FixMe Same thing?
                                return min(OBJECT_SIZE, maxXIntersection);
                            } else {
                                return 0f;
                            }
//                            }
                        case Back:
                            float spaceDownBack = spaceBetween(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)), State.get().getModsAt(new V3(module.pos()).add(V3.NEG_X).add(V3.NEG_Z)), V3.X);
                            float maxNEG_XIntersection = 0f;
                            if (spaceDownBack >= OBJECT_SIZE) {
                                for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {        //FixME  Optimze : Same xIntersection as in case Front
                                    maxNEG_XIntersection = max(maxNEG_XIntersection, -module.aabb().collide(mod.body().aabb()).xDist());
                                }
                                return min(OBJECT_SIZE, maxNEG_XIntersection);
                            } else {
                                return 0f;
                            }
                        case Left:
                            float spaceDownLeft;
                            if (State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)).size() == 0 || State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Y).add(V3.NEG_Z)).size() == 0){
                                spaceDownLeft = OBJECT_SIZE;
                            } else {
                                spaceDownLeft = spaceBetween(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)), State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Y).add(V3.NEG_Z)), V3.Y);
                            }
                            float maxYIntersection = 0f;
                            if (spaceDownLeft >= OBJECT_SIZE) {
                                for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {
                                    maxYIntersection = max(maxYIntersection, -module.aabb().collide(mod.body().aabb()).yDist());
                                }
                                return min(OBJECT_SIZE, maxYIntersection);
                            } else {
                                return 0f;
                            }
                        case Right:
                            float spaceDownRight;
                            if (State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)).size() == 0 || State.get().getModsAt(new V3(module.pos()).add(V3.Y).add(V3.NEG_Z)).size() == 0){
                                spaceDownRight = OBJECT_SIZE;
                            } else {
                                spaceDownRight = spaceBetween(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)), State.get().getModsAt(new V3(module.pos()).add(V3.Y).add(V3.NEG_Z)), V3.Y);
                            }
                            float maxNegYIntersection = 0f;
                            if (spaceDownRight >= OBJECT_SIZE) {
                                for (Module mod : State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z))) {
                                    maxNegYIntersection = max(maxNegYIntersection, -module.aabb().collide(mod.body().aabb()).yDist());
                                }
                                return min(OBJECT_SIZE, maxNegYIntersection);
                            } else {
                                return 0f;
                            }
                    }
                }
        }
        System.out.print("Magnitude Error!! (╯°□°）╯︵┻━┻ ");
        return magnitude;
    }


    //TODO for Phase 3 : Check 4 different lists according to direction , take the smallest xDist and yDist. If x- yDist >= OBJECT_SIZE return true  (If enough space/area for us to descned)
   */
/* public boolean canDescend(Module module,V3 direction){
        float spaceDownDirection = spaceBetween(State.get().getModsAt(new V3(module.pos()).add(V3.NEG_Z)), State.get().getModsAt(new V3(module.pos()).add(V3.Y).add(V3.NEG_Z)), V3.Y);
    }*//*

}
*/
