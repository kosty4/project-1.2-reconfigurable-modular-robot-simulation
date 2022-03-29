package com.group1.octobots.AI;

import com.badlogic.gdx.utils.ObjectMap;
import com.group1.octobots.Module;
import com.group1.octobots.Obstacle;
import com.group1.octobots.Target;
import com.group1.octobots.World;
import com.group1.octobots.physics.AABB;
import com.group1.octobots.physics.V3;
import com.group1.octobots.physics.WorldObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.group1.octobots.AI.State.callOnFootprint;
import static com.group1.octobots.utility.Constants.DEBUG;
import static com.group1.octobots.utility.Constants.Physics.MAP_SIZE;
import static com.group1.octobots.utility.Constants.State.*;

/**
 * Created on 4/24/2017.
 */
public final class BitmaskState {

    private final long[][][] map;
    private final ObjectMap<Integer, Module> mods;

//    @Override
    public void put(WorldObject wo) {
        int tag;
        if (wo instanceof Module) tag = ((Module) wo).tag();
        else tag = (wo instanceof Obstacle) ? OBS : TAR;
        callOnFootprint(wo.pos(),
                (x, y, z) -> put(tag, x, y, z));
    }

//    @Override
    public void remove(Module module) {
        callOnFootprint(module.pos(),
                ((x, y, z) -> remove(module.tag(), x, y, z)));
    }


    private List<Integer> getTagsAt(int x, int y, int z) {
        long mask = map[x][y][z];
        if (mask == ZEROS) {
            return new ArrayList<>(0);
        }
        List<Integer> tags = new ArrayList<>(8);
        int tag;
        for (int i = 0; i < 64; i+=8) {
            tag = (int) ((mask >>> i) & GET_BYTE);   //error-prone?
            if (tag != ZEROS)
                tags.add(tag);
        }
        return tags;
    }

    public List<Module> getModsAt(float x, float y, float z) {
        List<Module> modsAt = new ArrayList<>(8);
        callOnFootprint(x, y, z, (a, b, c) ->
                getTagsAt(a, b, c).stream()
                                .filter(tag -> tag != OBS && tag != TAR)
                                .map(mods::get)
                                .forEach(modsAt::add));
        return modsAt;
    }

//    @Override
    public List<AABB> getObstsAt(float x, float y, float z) {
        List<AABB> obstsAt = new ArrayList<>(1);
        callOnFootprint(x, y, z, (a, b, c) -> {
            if (getTagsAt(a, b, c).stream()
                    .filter(tag -> tag == OBS)
                    .count() > 0) obstsAt.add(new AABB(new V3(a,b,c),new V3(a+1,b+1,c+1)));
        });
        return obstsAt;
    }

//    @Override
    public List<Target> getTargsAt(float x, float y, float z) {
        List<Target> targsAt = new ArrayList<>(1);
        callOnFootprint(x, y, z, (a, b, c) -> {
            if (getTagsAt(a, b, c).stream()
                    .filter(tag -> tag == TAR)
                    .count() > 0) targsAt.addAll(World.targets.stream().filter(t -> t.pos().equals(new V3(a, b, c))).collect(Collectors.toList()));
        });
        return targsAt;
    }

    //todo Can use positional placement of the bytes. Map the 8 bytes of the long to 8 different regions of a space.
    private void put(int modID, int x, int y, int z) {
        int emptySlot = getEmptySlot(map[x][y][z]);
        if (DEBUG) {
            assert (modID >>> 8) == 0;
            assert (emptySlot != -1);
        }
        long offsetID = ((long) modID) << emptySlot;
        map[x][y][z] |= offsetID;
    }

    private void remove(int modID, int x, int y, int z) {
        if (DEBUG)
            assert (modID >>> 8) == 0;
        long mask = map[x][y][z];
        for (int i = 0; i < 64; i += 8) {
            if (((mask >>> i) & GET_BYTE) == modID)
                map[x][y][z] &= ~(GET_BYTE << i);
        }
    }

    private int getEmptySlot(long mask) {
        for (int i = 0; i < 64; i += 8) {
            if (((mask >>> i) & GET_BYTE) == ZEROS)
                return i;
        }
        return -1;
    }

    public void assertValidity() {
        for (Module module : World.modules) {
            callOnFootprint(module.pos(), (x, y, z) -> {
                if (!getModsAt(x, y, z).contains(module)) {
                    System.out.println(module.ID() + " not in (" + x + ", " + y + ", " + z + ")");
                }
            });
        }
        System.out.println("State.assertValidity COMPLETED");
    }

    protected BitmaskState(List<Module> modules, List<Obstacle> obstacles, List<Target> targets) {
        map = new long[MAP_SIZE.x][MAP_SIZE.y][MAP_SIZE.z];
        mods = new ObjectMap<>(modules.size());
        modules.forEach(module -> {
            mods.put(module.tag(), module);
            put(module);
        });
        obstacles.forEach(this::put);
        targets.forEach(this::put);
        assertValidity();
    }
}
