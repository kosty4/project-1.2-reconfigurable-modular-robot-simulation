package com.group1.octobots.utility;

import java.util.Iterator;

/**
 * Utility class for cycling through a range of numbers indefinitely.
 * Calling the {@link CyclicIntIterator#next()} method when the iterator
 * has reached its limit will reset the internal counter to 0.
 * <br><br>
 * The limit passed to the constructor is an <u><b>inclusive</b></u> upper bound.
 * <br><br>
 * The class was designed for facilitating cyclic iteration through an array with the use of an index.
 *
 * @author antonwnk
 */
public class CyclicIntIterator {

    private int i = 0;

    private int limit;

    /**
     * Creates a new CyclicIntIterator object.
     * @param limit The <u><b>inclusive</b></u> upper bound of <code>this</code> iterator.
     */
    public CyclicIntIterator(int limit) {
        if (limit < 0) throw new IllegalArgumentException("Limit must be positive cyka..");
        this.limit = limit;
    }

    /**
     * Advances the {@link CyclicIntIterator} one step in the iteration process.
     * @see Iterator#next()
     * @return The next value in the iteration sequence.
     */
    public int next() {
        if (i == limit)
            i = -1;
        return ++i;
    }

    /**
     * Moves the {@link CyclicIntIterator} one step backwards in the iteration process.
     * @return The previous value in the iteration sequence.
     */
    public int previous() {
        if (i == 0)
            i = limit + 1;
        return --i;
    }

    /**
     * Peeks the previous value of the iterator. <br>
     * Does not make any modifications to the current position in the iteration.
     *
     * @return The value returned by the previous call of this
     *         {@link CyclicIntIterator}'s
     *         {@link CyclicIntIterator#next()} method.
     */
    public int peekPrevious() {
        if (i == 0)
            return limit;
        else
            return i - 1;
    }

    /**
     * @return The value of the internal counter of this {@link CyclicIntIterator}.
     */
    public int current() {
        return i;
    }

    /**
     * Peeks the next value of the iterator.
     * Does not make any modifications to the current position in the iteration.
     *
     * @return The value returned by the next call of this
     *         {@link CyclicIntIterator}'s
     *         {@link CyclicIntIterator#next()} method.
     */
    public int peekNext() {
        if (i == limit)
            return 0;
        else
            return i + 1;
    }
}
