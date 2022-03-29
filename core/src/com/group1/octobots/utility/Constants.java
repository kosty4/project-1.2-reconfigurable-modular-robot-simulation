package com.group1.octobots.utility;

import com.group1.octobots.AI.ArrayState;
import com.group1.octobots.Obstacle;
import com.group1.octobots.Target;

import static com.group1.octobots.utility.Constants.Physics.MAP_SIZE;

/**
 * Global class for the definition of constant values.
 * Provides a point of global access and is used for ease
 * of modification to the constant parameters.
 */
public class Constants {

    public static final boolean DEBUG = true;

    public static final int MODULE_SOFTCAP = 253;

    /**
     * Container class for {@link ArrayState} constants.
     */
    public static class State {

        /**
         * Unique byte identifier for an {@link Obstacle}.
         */
        public static final int OBS = 254;
        /**
         * Unique byte identifier for a {@link Target}.
         */
        public static final int TAR = 255;
        /**
         * Long with all bytes being {@code 0x00}.
         */
        public static final long ZEROS = 0x00L;
        /**
         * Long with all bytes being {@code 0xff}.
         */
        public static final long ONES = 0xff_ff_ff_ff_ff_ff_ff_ffL;
        /**
         * Perform a {@code bitwise-and} operation with this argument
         * to obtain the first byte of a given {@code long}.
         */
        public static final long GET_BYTE = 0xffL;

    }

    /**
     * Container class for Rendering constants.
     */
    public static class Rendering {

        /**
         * Offset values for drawing WorldObjects on the screen.
         */
        public static final float X_OFFSET =
                (MAP_SIZE.x % 2 == 0)
                        ? MAP_SIZE.x / 2 - 0.5f
                        : MAP_SIZE.x / 2;
        public static final float Y_OFFSET =
                (MAP_SIZE.y % 2 == 0)
                        ? MAP_SIZE.y / 2 - 0.5f
                        : MAP_SIZE.y / 2;

    }

    /**
     * Container class for Physics constants.
     */
    public static class Physics {

        /**
         * Gravitational acceleration.
         */
        public static final float G = -1f;

        /**
         * The mass of a module
         */
        public static final float MASS = 1f;

        /**
         * Friction constant.
         */
        public static final float FRICTION = Float.NEGATIVE_INFINITY;

        /**
         * The size of a {@link com.group1.octobots.Module}.
         */
        public static final float OBJECT_SIZE = 1f;

        public static final float MOD_SIDE_AREA = 1f;

        public static final float TIPPING_PERCENTAGE = 50f;
        /**
         * The side-length of the 3-d Simulation world.
         */
        public static final Dimension MAP_SIZE = new Dimension(35, 35, 35);

    }

    /**
     * Private Constructor to prevent instantiation.
     */
    private Constants(){}

}
