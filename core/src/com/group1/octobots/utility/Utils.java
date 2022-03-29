package com.group1.octobots.utility;

import static com.badlogic.gdx.math.MathUtils.*;

/**
 * Class for providing wrappers around some aggregates of {@link com.badlogic.gdx.math.MathUtils} functions.
 */
public class Utils {

    /**
     * Returns true if the given float is effectively a whole number. Works correctly only for positive numbers.
     */
    public static boolean isWhole(float f) {
        return isZero(f - roundPositive(f));
    }
}
