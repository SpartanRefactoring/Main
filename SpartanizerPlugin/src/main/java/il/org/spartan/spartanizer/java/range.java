package il.org.spartan.spartanizer.java;

import java.util.NoSuchElementException;
import java.util.Iterator;

/** A fluent api implementation for range
 * @author Dor Ma'ayan
 * @since 26-11-2016 */
public class range implements Iterable<Integer> {
  int from;
  int to;
  boolean inclusive;
  int step = 1;

  void initialize() {
    from = 0;
    inclusive = false;
    step = 1;
  }

  public AfterTo to(int ¢) {
    to = ¢;
    return new AfterTo();
  }

  public BeforeTo from(int ¢) {
    from = ¢;
    return new BeforeTo();
  }

  @Override public Iterator<Integer> iterator() {
    return new RangeIterator();
  }

  class BeforeTo {
    public AfterTo to(int ¢) {
      to = ¢;
      return new AfterTo();
    }

    public AfterTo step(int ¢) {
      step = ¢;
      return new AfterTo();
    }
  }

  class RangeIterator implements Iterator<Integer>, Iterable<Integer> {
    private int cursor;

    public RangeIterator() {
      this.cursor = from;
    }

    public RangeIterator inclusive() {
      inclusive = true;
      return this;
    }

    public RangeIterator notInclusive() {
      inclusive = false;
      return this;
    }

    @Override public boolean hasNext() {
      boolean $ = inclusive ? this.cursor <= to : this.cursor < to;
      if (!$)
        initialize();
      return $;
    }

    @SuppressWarnings("boxing") @Override public Integer next() {
      if (!this.hasNext())
        throw new NoSuchElementException();
      int $ = cursor;
      cursor += step;
      return $;
    }

    @Override public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override public Iterator<Integer> iterator() {
      return this;
    }
  }

  class AfterTo implements Iterator<Integer>, Iterable<Integer> {
    private int cursor;

    public AfterTo() {
      this.cursor = from;
    }

    public RangeIterator step(int ¢) {
      step = ¢;
      return new RangeIterator();
    }

    public RangeIterator inclusive() {
      inclusive = true;
      return new RangeIterator();
    }

    public RangeIterator notInclusive() {
      inclusive = false;
      return new RangeIterator();
    }

    @Override public boolean hasNext() {
      boolean $ = inclusive ? this.cursor <= to : this.cursor < to;
      if (!$)
        initialize();
      return $;
    }

    @SuppressWarnings("boxing") @Override public Integer next() {
      if (!this.hasNext())
        throw new NoSuchElementException();
      int $ = cursor;
      ++cursor;
      return $;
    }

    @Override public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override public Iterator<Integer> iterator() {
      return this;
    }
  }
}
