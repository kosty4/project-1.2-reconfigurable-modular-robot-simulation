package com.group1.octobots.search;

/**
 * Created on 3/27/2017.
 */
@FunctionalInterface
public interface Estimation<Context, Position> {
    float estimate(Context c, Position from, Position to);
}
