package com.group1.octobots.AI;

import com.group1.octobots.Module;
import com.group1.octobots.Obstacle;
import com.group1.octobots.Target;
import com.group1.octobots.World;
import com.group1.octobots.physics.WorldObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.group1.octobots.AI.State.callOnFootprint;
import static com.group1.octobots.utility.Constants.Physics.MAP_SIZE;

/**
 * Created on 6/17/2017.
 */
@SuppressWarnings("Duplicates")
public final class ArrayState implements State {

    private WorldObject[][][][] map;

    @Override
    public void put(WorldObject worldObject) {
        callOnFootprint(worldObject.pos(),
                ((x, y, z) -> {
                    put(worldObject, x, y, z);
                }));
    }

    @Override
    public void remove(Module module) {
        callOnFootprint(module.pos(),
                ((x, y, z) -> remove(module, x, y, z)));

    }

    private void put(WorldObject worldObject, int x, int y, int z) {
        WorldObject[] worldObjects = map[x][y][z];
        for (int i = 0; i < worldObjects.length; i++) {
            if (worldObjects[i] == null) {
                worldObjects[i] = worldObject;
                return;
            }
        }
        throw new IndexOutOfBoundsException("Tried to place obj: "
                            + worldObject + " at "
                            + x +"," + y + "," + z);
    }

    private void remove(WorldObject worldObject, int x, int y, int z) {
        WorldObject[] worldObjects = map[x][y][z];
        for (int i = 0; i < worldObjects.length; i++) {
            if (worldObjects[i] == worldObject) {
                worldObjects[i] = null;
                return;
            }
        }
        throw new IndexOutOfBoundsException("Tried to remove obj: "
                            + worldObject + " from "
                            + x + "," + y + "," + z);
    }

    @Override
    public List<WorldObject> getObjectsAt(float x, float y, float z) {
        List<WorldObject> objectsAt = new ArrayList<>(1);

        callOnFootprint(x, y, z, (x1, y1, z1) ->
                Arrays.stream(map[x1][y1][z1])
                    .filter(Objects::nonNull)
                    .forEach(objectsAt::add)
        );
        return objectsAt;
    }

    @Override
    public List<Module> getModsAt(float x, float y, float z) {
        List<Module> modsAt = new ArrayList<>(1); //todo optimization magic numberrr, has much impact i thinkss

        callOnFootprint(x, y, z, (x1, y1, z1) ->
                Arrays.stream(map[x1][y1][z1])
                        .filter(Module.class::isInstance)
                        .map(Module.class::cast)
                        .forEach(modsAt::add));
        return modsAt;
    }

    @Override
    public List<Obstacle> getObstsAt(float x, float y, float z) {
        List<Obstacle> obstsAt = new ArrayList<>(1);

        callOnFootprint(x, y, z, (x1, y1, z1) ->
                Arrays.stream(map[x1][y1][z1])
                        .filter(Obstacle.class::isInstance)
                        .map(Obstacle.class::cast)
                        .forEach(obstsAt::add));
        return obstsAt;
    }

    @Override
    public List<Target> getTargsAt(float x, float y, float z) {
        List<Target> targsAt = new ArrayList<>(1);

        callOnFootprint(x, y, z, (x1, y1, z1) ->
                Arrays.stream(map[x1][y1][z1])
                        .filter(Target.class::isInstance)
                        .map(Target.class::cast)
                        .forEach(targsAt::add));
        return targsAt;
    }

    @Override
    public void assertValidity() {
        for (Module module : World.modules) {
            callOnFootprint(module.pos(), (x, y, z) -> {
                if (!Arrays.asList(map[x][y][z]).contains(module))
                    System.out.println(module + " missing from " + x + "," + y + "," + z);
            });
            module.body().assertLegalState();
//            if (!module.pos().equals(module.body().pos())) System.out.println(
//                    module.pos().cpy().subtract(module.body().pos()).sumComponents() + " sum distances"
//            );
        }
        for (Obstacle obstacle : World.obstacles) {
            callOnFootprint(obstacle.pos(), (x, y, z) -> {
                if (!Arrays.asList(map[x][y][z]).contains(obstacle))
                    System.out.println(obstacle + " missing from " + x + "," + y + "," + z);
            });
        }
        for (Target target : World.targets) {
            callOnFootprint(target.pos(), (x, y, z) -> {
                if (!Arrays.asList(map[x][y][z]).contains(target))
                    System.out.println(target + " missing from " + x + "," + y + "," + z);
            });
        }
//        System.out.println("State.assertValidity completed");
    }

    @SuppressWarnings("unchecked")
    protected ArrayState(List<Module> modules, List<Obstacle> obstacles, List<Target> targets) {
        map = new WorldObject[MAP_SIZE.x][MAP_SIZE.y][MAP_SIZE.z][8];
        modules.forEach(this::put);
        obstacles.forEach(this::put);
        targets.forEach(this::put);
    }
}
