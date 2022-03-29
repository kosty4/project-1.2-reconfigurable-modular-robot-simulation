package com.group1.octobots.search;

/**
 * Created on 3/26/2017.
 */
@FunctionalInterface
public interface Expansion<Context, Position> {
    Iterable<Position> expand(Context c, Position p);
}
