package il.org.spartan.spartanizer.java;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.*;

/** A fluent API implementation for range
 * @author Dor Ma'ayan
 * @since 26-11-2016 */
public class range {
  @NotNull
  public static <T> RangeIterator<?> of(@NotNull final T[] ¢) {
    return from(0).to(¢.length);
  }

  @NotNull
  public static BeforeTo from(final int ¢) {
    return makeFrom(¢).new BeforeTo();
  }

  @NotNull
  public static RangeIterator<?> infinite(final int ¢) {
    return from(¢).to(¢).step(0).inclusive();
  }

  @NotNull
  public static Infinite infinite() {
    return infiniteFrom(0, 1);
  }

  @NotNull
  static Infinite infiniteFrom(final int ¢, final int ¢2) {
    final Infinite $ = makeFrom(¢).new Infinite().infiniteRange();
    $.step(¢2);
    return $;
  }

  @NotNull
  public static RangeIterator<?> naturals() {
    return from(0).to(-1).step(1);
  }

  @NotNull
  public static RangeIterator<?> numerals() {
    return from(1).to(-1).step(1);
  }

  @NotNull
  public static RangeIterator<?> odds() {
    return from(1).to(-1).step(2);
  }

  @NotNull
  public static AfterTo to(final int to) {
    return makeTo(to).new AfterTo();
  }

  @NotNull
  private static range makeFrom(final int ¢) {
    return new range() {
      {
        from = ¢;
      }
    };
  }

  @NotNull
  private static range makeTo(final int ¢) {
    return new range() {
      {
        to = ¢;
      }
    };
  }

  int from;
  int to = -1;
  boolean inclusive;
  int step = 1;
  boolean infinite;

  public class AfterTo extends RangeIterator<AfterTo> {
    @NotNull
    public Stream<Integer> stream() {
      return StreamSupport.stream(spliterator(), false);
    }

    @NotNull
    public AfterTo from(final int ¢) {
      to = ¢;
      return this;
    }

    @NotNull
    public AfterTo step(final int ¢) {
      step = ¢;
      return this;
    }

    @NotNull
    public Infinite infinite() {
      return range.infiniteFrom(from, step);
    }

    @NotNull
    @Override AfterTo self() {
      return this;
    }
  }

  public class BeforeTo extends RangeIterator<BeforeTo> {
    @NotNull
    public AfterTo step(final int ¢) {
      step = ¢;
      return new AfterTo();
    }

    @NotNull
    public AfterTo to(final int ¢) {
      to = ¢;
      return new AfterTo();
    }

    @NotNull
    public Infinite infinite() {
      return range.infiniteFrom(from, step);
    }

    @NotNull
    @Override BeforeTo self() {
      return this;
    }
  }

  public class Infinite extends RangeIterator<Infinite> {
    @NotNull
    public Infinite step(final int ¢) {
      step = ¢;
      return this;
    }

    @NotNull
    public Infinite from(final int ¢) {
      from = ¢;
      step = 1;
      return this;
    }

    @NotNull
    @Override Infinite self() {
      return this;
    }
  }

  abstract class RangeIterator<Self extends RangeIterator<Self>> implements Iterable<Integer> {
    @NotNull
    public final Self exclusive() {
      inclusive = false;
      return self();
    }

    @NotNull
    public final Self inclusive() {
      inclusive = true;
      return self();
    }

    @NotNull
    public final Self infiniteRange() {
      infinite = true;
      return self();
    }

    @NotNull
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

    @NotNull
    abstract Self self();
  }
}
