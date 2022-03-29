package com.group1.octobots.AI;

import com.group1.octobots.Module;
import com.group1.octobots.Obstacle;
import com.group1.octobots.Target;
import com.group1.octobots.physics.AABB;
import com.group1.octobots.physics.V3;
import com.group1.octobots.physics.WorldObject;
import com.group1.octobots.utility.Utils;
import com.group1.octobots.utility.VectorFootprintConsumer;
import java.util.List;

import static com.group1.octobots.utility.Constants.MODULE_SOFTCAP;

/**
 * Created on 6/22/2017.
 */
public interface State {
    void put(WorldObject worldObject);

    void remove(Module module);

    default List<WorldObject> getObjectsAt(V3 position) { return getObjectsAt(position.x, position.y, position.z); }
    List<WorldObject> getObjectsAt(float x, float y, float z);

    default List<Module> getModsAt(V3 position) { return getModsAt(position.x, position.y, position.z); }
    List<Module> getModsAt(float x, float y, float z);

    default List<Obstacle> getObstsAt(V3 position) { return getObstsAt(position.x, position.y, position.z); }
    List<Obstacle> getObstsAt(float x, float y, float z);

    default List<Target> getTargsAt(V3 position) { return getTargsAt(position.x, position.y, position.z); }
    List<Target> getTargsAt(float x, float y, float z);

    void assertValidity();

    /**
     * Initializes either an {@link ArrayState} or a {@link BitmaskState} according to a strategy.
     *
     * @see "https://en.wikipedia.org/wiki/Strategy_pattern"
     */
    static State init(List<Module> modules, List<Obstacle> obstacles, List<Target> targets) {
//        return (modules.size() > MODULE_SOFTCAP)
//                ? new ArrayState(modules, obstacles, targets)
//                : new BitmaskState(modules, obstacles, targets);
        return new ArrayState(modules, obstacles, targets);
    }

    static void callOnFootprint(V3 position, VectorFootprintConsumer f) {
        callOnFootprint(position.x, position.y, position.z, f);
    }

    static void callOnFootprint(float x, float y, float z, VectorFootprintConsumer f) {
        f.call((int) x, (int) y, (int) z);

        boolean xIsWhole = Utils.isWhole(x);   //todo insert tolerance if needed.
        boolean yIsWhole = Utils.isWhole(y);
        boolean zIsWhole = Utils.isWhole(z);

        if (!xIsWhole || !yIsWhole || !zIsWhole) {
            if (zIsWhole && yIsWhole) {
                f.call((int) (x + 1), (int) y, (int) z);
            } else if (zIsWhole && xIsWhole) {
                f.call((int) x, (int) (y + 1), (int) z);
            } else if (xIsWhole && yIsWhole) {
                f.call((int) x, (int) y, (int) (z + 1));
            } else if (zIsWhole) {
                f.call((int) (x + 1), (int) y, (int) z);
                f.call((int) x, (int) (y + 1), (int) z);
                f.call((int) (x + 1), (int) (y + 1), (int) z);
            } else if (yIsWhole) {
                f.call((int) (x + 1), (int) y, (int) z);
                f.call((int) x, (int) y, (int) (z + 1));
                f.call((int) (x + 1), (int) y, (int) (z + 1));
            } else if (xIsWhole) {
                f.call((int) x, (int) (y + 1), (int) z);
                f.call((int) x, (int) y, (int) (z + 1));
                f.call((int) x, (int) (y + 1), (int) (z + 1));
            } else {
                f.call((int) (x + 1), (int) y, (int) z);
                f.call((int) x, (int) (y + 1), (int) z);
                f.call((int) x, (int) y, (int) (z + 1));
                f.call((int) (x + 1), (int) (y + 1), (int) z);
                f.call((int) (x + 1), (int) y, (int) (z + 1));
                f.call((int) x, (int) (y + 1), (int) (z + 1));
                f.call((int) (x + 1), (int) (y + 1), (int) (z + 1));
            }
        }
    }
}
