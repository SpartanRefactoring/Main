package fluent.ly;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** A fluent API implementation for range
 * @author Dor Ma'ayan
 * @since 26-11-2016 */
public class range {
  public class AfterTo extends RangeIterator<AfterTo> {
    public AfterTo from(final int ¢) {
      to = ¢;
      return this;
    }

    public Iterable<Integer> infinite() {
      return range.infiniteFrom(from, step);
    }

    public AfterTo step(final int ¢) {
      step = ¢;
      return this;
    }

    public Stream<Integer> stream() {
      return StreamSupport.stream(spliterator(), false);
    }

    @Override AfterTo self() {
      return this;
    }
  }

  public class BeforeTo extends RangeIterator<BeforeTo> {
    public Infinite infinite() {
      return range.infiniteFrom(from, step);
    }

    public AfterTo step(final int ¢) {
      step = ¢;
      return new AfterTo();
    }

    public AfterTo to(final int ¢) {
      to = ¢;
      return new AfterTo();
    }

    @Override BeforeTo self() {
      return this;
    }
  }

  public class Infinite extends RangeIterator<Infinite> {
    public Infinite from(final int ¢) {
      from = ¢;
      step = 1;
      return this;
    }

    public Iterable<Integer> step(final int ¢) {
      step = ¢;
      return this;
    }

    @Override Infinite self() {
      return this;
    }
  }

  abstract class RangeIterator<Self extends RangeIterator<Self>> implements Iterable<Integer> {
    public final Self exclusive() {
      inclusive = false;
      return self();
    }

    public final Self inclusive() {
      inclusive = true;
      return self();
    }

    public final Self infiniteRange() {
      infinite = true;
      return self();
    }

    @Override public Iterator<Integer> iterator() {
      return new Iterator<Integer>() {
        int next = from;

        @Override public boolean hasNext() {
          return infinite || (inclusive ? next <= to : next < to);
        }

        @Override public Integer next() {
          if (!hasNext())
            throw new NoSuchElementException();
          final int $ = next;
          next += step;
          return Integer.valueOf($);
        }
      };
    }

    abstract Self self();
  }

  public static BeforeTo from(final int ¢) {
    return makeFrom(¢).new BeforeTo();
  }

  public static Infinite infinite() {
    return infiniteFrom(0, 1);
  }

  public static Iterable<Integer> infinite(final int ¢) {
    return from(¢).to(¢).step(0).inclusive();
  }

  public static RangeIterator<?> naturals() {
    return from(0).to(-1).step(1);
  }

  public static RangeIterator<?> numerals() {
    return from(1).to(-1).step(1);
  }

  public static RangeIterator<?> odds() {
    return from(1).to(-1).step(2);
  }

  public static <T> RangeIterator<?> of(final T[] ¢) {
    return from(0).to(¢.length);
  }

  public static AfterTo to(final int to) {
    return makeTo(to).new AfterTo();
  }

  private static range makeFrom(final int ¢) {
    return new range() {
      {
        from = ¢;
      }
    };
  }

  private static range makeTo(final int ¢) {
    return new range() {
      {
        to = ¢;
      }
    };
  }

  static Infinite infiniteFrom(final int ¢, final int ¢2) {
    final Infinite $ = makeFrom(¢).new Infinite().infiniteRange();
    $.step(¢2);
    return $;
  }

  int from;
  boolean inclusive;
  boolean infinite;
  int step = 1;
  int to = -1;
}
