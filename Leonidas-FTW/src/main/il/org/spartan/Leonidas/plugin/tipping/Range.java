package il.org.spartan.Leonidas.plugin.tipping;

import org.junit.Assert;

/**
 * A very simplistic Range
 *
 * @author Roey Maor
 * @since 09-12-2016
 */
public class Range {
    private int start;
    private int stop;

    public Range(int start, int stop) {
        assert start <= stop;
        this.start = start;
        this.stop = stop;
    }

    public Range(Range r) {
        this.start = r.start;
        this.stop = r.stop;
    }

    public Range merge(Range ¢) {
        return this.start > ¢.start || ¢.stop < this.stop ? null : new Range(this.start, ¢.stop);
    }
}