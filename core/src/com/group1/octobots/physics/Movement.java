package com.group1.octobots.physics;

import com.group1.octobots.AI.Direction;
import com.group1.octobots.AI.Move;
import com.group1.octobots.AI.MoveType;
import com.group1.octobots.AI.State;
import com.group1.octobots.Module;
import com.group1.octobots.World;
import org.jetbrains.annotations.NotNull;
//import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.badlogic.gdx.math.MathUtils.*;
import static com.group1.octobots.physics.AABB.*;
import static com.group1.octobots.physics.PhysicsEngine.isInsideBounds;


@SuppressWarnings("Duplicates")
public class Movement {

    private static final int NUM_NEIGHBOURING_POSITIONS = 2 + (3 * 4);

    private State st;

    public Movement(State state){
        st = state;
    }

    @NotNull
    public List<Move> getAvailableMoves(Module module){
        V3 pos = module.pos();
        AABB thisAABB = module.aabb();

        List<Move> availableMoves = new ArrayList<>(5);
        availableMoves.add(Move.emptyMove(module));

        if (module.isFalling()) return availableMoves;

        Map<String, List<WorldObject>> neighbours = new HashMap<>(NUM_NEIGHBOURING_POSITIONS);

        neighbours.put("" + MoveType.Climb,                    // "C"
                st.getObjectsAt(pos.x, pos.y, pos.z + 1));

        float percentTopCovered = (float) neighbours.get("C").stream()                         // whether we have more than 50% top surface covered
                .filter(Module.class::isInstance)
//                .map(worldObject -> ((Module)worldObject).body().aabb())
                .map(WorldObject::aabb)
                .filter(aabb -> isZero(aabb.collide(thisAABB).zDist()))
                .mapToDouble(aabb -> aabb.collide(thisAABB).alignedXY())
                .sum();

        if (percentTopCovered > 50) return availableMoves;

        boolean belowExists = pos.z >= 1;

        if (belowExists)
            neighbours.put("" + MoveType.Descend, st.getObjectsAt(pos.x, pos.y, pos.z - 1));

        for (Direction direction : Direction.values()) {
            V3 newPos = pos.cpy().add(direction.vector);
            if (!isInsideBounds(newPos)) continue;

            Move move;

            // "F", "L", "R", "B"
            neighbours.put("" + direction, st.getObjectsAt(newPos));
            // "CF", "CB", ......
            neighbours.put("" + MoveType.Climb + direction, st.getObjectsAt(newPos.x, newPos.y, newPos.z + 1));


            if (belowExists) {
                // "DF", "DB", ....
                neighbours.put("" + MoveType.Descend + direction, st.getObjectsAt(newPos.x, newPos.y, newPos.z - 1));

                move = checkMove(module, neighbours, direction, MoveType.Translate);
                if (move != null) {
                    availableMoves.add(move);
                    continue;
                }

                move = checkMove(module, neighbours, direction, MoveType.Descend);
                if (move != null) {
                    availableMoves.add(move);
                    continue;
                }
            }

            Optional.ofNullable(
                    checkMove(module, neighbours, direction, MoveType.Climb)
            ).ifPresent(availableMoves::add);
        }

//        new Thread(() -> {
//            for (List<WorldObject> modules : neighbours.values()) {
//                modules.stream().filter(Module.class::isInstance)
//                        .map(Module.class::cast)
//                        .forEach(m -> m.gfxComponent().toggleMoved());
//            }
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            for (List<WorldObject> modules : neighbours.values()) {
//                modules.stream().filter(Module.class::isInstance)
//                        .map(Module.class::cast)
//                        .forEach(m -> m.gfxComponent().toggleMoved());
//            }
//        }).start();
        return availableMoves;
    }

    private Move checkMove(Module thisMod, Map<String, List<WorldObject>> neighbours, Direction dir, MoveType type) {
        Direction opDir = dir.opposite();
        V3 thisPos = thisMod.pos();
        AABB thisAABB = thisMod.aabb();
        AABB hypotheticalLocation;

        switch (type) {
            case Climb:
                if (neighbours.get(dir.toString()).stream()                // if the objects in front of us
                        .filter(Module.class::isInstance)                   // are modules
                        .filter(mod -> isEqual(mod.pos().z, thisPos.z))    // and are on the same height
                        .map(WorldObject::aabb)
                        .filter(aabb -> isZero(aabb.collide(thisAABB).distOn(dir))) // and are touching us in the direction
                        .mapToDouble(aabb -> aabb.collide(thisAABB).alignedOn(opDir))
                        .sum() < 50)                                            // are touching us for less that 50%
                    return null;                                            // THEN WE CAN'T Climb


                if (neighbours.get("C").stream().map(WorldObject::aabb)    // if any object ABOVE us is closer than 1f
                        .filter(aabb -> aabb.collide(thisAABB)                          
                                            .alignedXY() > 0)
                        .anyMatch(aabb -> aabb.collide(thisAABB)
                                              .zDist() < 1f))
                    return null;                                             // THEN WE CAN'T Climb

                float maxMagnitude = (float) neighbours.get("C" + dir).stream()
                        .map(WorldObject::aabb)
                        .filter(aabb -> aabb.collide(thisAABB).alignedOn(opDir) > 0)        // if there is any other module at said location
                        .mapToDouble(aabb -> aabb.collide(thisAABB).distOn(dir))            // get the distances to them/it
                        .min()                                                              // get the closest
                        .orElse(1);                                                         // if nothing found return 1 for max magnitude
                if (maxMagnitude > 1f) maxMagnitude = 1f;

                if (maxMagnitude < 0.5f)
                    return null;

                return new Move(thisMod, dir, type, maxMagnitude);

            case Translate:
                List<WorldObject> modsUnder = neighbours.get("D").stream()           // the mods beneath
                        .filter(Module.class::isInstance)
                        .filter(mod -> mod.aabb().collide(thisAABB).alignedXY() > 0) // , DIRECTLY beneath
                        .filter(mod -> isZero(mod.aabb().collide(thisAABB).zDist())) // touching our rear    todo insert tolerance if iffy
                        .collect(Collectors.toList());

                boolean notEnoughBottomCovfefe = modsUnder.stream()                         // whether we have less than 50% footing
                        .mapToDouble(mod -> mod.aabb().collide(thisAABB).alignedXY())
                        .sum() < 50;

                if (notEnoughBottomCovfefe)
                    return null;

                // TODO INSERT STATIC FRICTION HERE!!!!!!!!!!!!!!!!!!!

                float maxMag = (float) neighbours.get(dir.toString()).stream()              // get the distance to the first thing in front of us
                        .map(WorldObject::aabb)
                        .filter(aabb -> aabb.collide(thisAABB).alignedOnZand(opDir) > 0)
                        .mapToDouble(aabb -> aabb.collide(thisAABB).distOn(dir))
                        .min()
                        .orElse(1);
                if (maxMag > 1f) maxMag = 1f;

                if (isZero(maxMag))
                    return null;

                hypotheticalLocation = new AABB(thisAABB, dir.vector.cpy().scale(maxMag));  // a box to where we would be after moving
/*
                boolean blockedAbovePostMove = neighbours.get("C" + dir.toString()).stream()// whether we have more than 50% top surface covered
                        .filter(Module.class::isInstance)                                       // during the move
                        .map(WorldObject::aabb)
                        .filter(aabb -> isZero(aabb.collide(hypotheticalLocation).zDist()))
                        .mapToDouble(aabb -> aabb.collide(hypotheticalLocation).alignedXY())
                        .sum() > 50;

                if (blockedAbovePostMove)
                    return null;
*/

                float bottomCoveredPostMove = (float) neighbours.get("D"+ dir).stream()     // the coverage % of the it's bottom surface
//                        .filter(wo -> wo instanceof Obstacle || !((Module)wo).checkFalling())
                        .map(WorldObject::aabb)
                        .filter(aabb -> isZero(aabb.collide(hypotheticalLocation).zDist()))
                        .mapToDouble(aabb -> aabb.collide(hypotheticalLocation).alignedXY())
                        .sum();
                if (!(bottomCoveredPostMove > 50))
                    return null;

/*
                boolean theFloorIsFalling = neighbours.get("D" + dir).stream()
                        .filter(Module.class::isInstance)
                        .map(Module.class::cast)
                        .filter(mod -> {
                            AABB.IntersectData id = mod.aabb().collide(hypotheticalLocation);
                            return id.alignedXY() > 0 && isZero(id.zDist());
                        })
                        .noneMatch(Module::checkFalling);
*/

                Move td = new Move(thisMod, dir, type, maxMag);

//                td.causesFall = isZero(bottomCoveredPostMove) || theFloorIsFalling;

                return td;

//                boolean hasBridge = modsUnder.stream()
//                        .anyMatch(mod -> modsUnderDirection.contains(mod)
//                                        || modsUnderDirection.stream().anyMatch(
//                                                m -> isZero(m.aabb().collide(mod.aabb())
//                                                             .distOn(d))));
//
//                return hasBridge ? new Move(thisMod, d, t, maxMag) : null;

            case Descend:
                float bottomSurfaceCovered = 0f;
                for (WorldObject wo : neighbours.get("D")) {
                    IntersectData id = thisAABB.collide(wo.aabb());
                    if (isZero(id.zDist())) {
                        bottomSurfaceCovered +=
                                id.alignedXY();
                    }
                }
                if (!isEqual(bottomSurfaceCovered, 100, 1))
                    return null;

                for (WorldObject wo : neighbours.get("" + dir)) {       // if there's any object in front
                    IntersectData id = thisAABB.collide(wo.aabb());     // alligned and closer than 1f, CAN'T DESCEND
                    if (id.alignedOnZand(opDir) > 0)
                        if (id.distOn(dir) < 1)
                            return null;
                }

                hypotheticalLocation = new AABB(thisAABB, dir.vector.cpy().add(V3.NEG_Z));
                for (WorldObject wo : neighbours.get("D" + dir)) {
                    if (hypotheticalLocation.intersectsStrict(wo.aabb())) {
                        return null;
                    }
                }
                return new Move(thisMod, dir, type, 1f);
        }
        return null;
    }
    /*
    * .filter(mod -> {
                            IntersectData id = mod.aabb().collide(thisMod.aabb());
                            boolean touching = isZero(id.distOn(d));
                            boolean aligned = id.alignedOn(opDir) >= 50;
                            return touching && aligned;
                        })
    * */
}