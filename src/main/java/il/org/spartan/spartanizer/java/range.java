package il.org.spartan.spartanizer.java;

import java.util.NoSuchElementException;

import java.util.Iterator;

/** A fluent API implementation for range
 * @author Dor Ma'ayan
 * @since 26-11-2016 */
public class range {
  public static BeforeTo from(final int ¢) {
    return makeFrom(¢).new BeforeTo();
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

  int from;
  int to = -1;
  boolean inclusive;
  int step = 1;

  public class AfterTo extends Inner<AfterTo> {
    public AfterTo from(final int ¢) {
      to = ¢;
      return this;
    }

    public AfterTo step(final int ¢) {
      step = ¢;
      return this;
    }

    @Override AfterTo self() {
      return this;
    }
  }

  public class BeforeTo extends Inner<BeforeTo> {
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

  abstract class Inner<Self extends Inner<Self>> implements Iterable<Integer> {
    public final Self exclusive() {
      inclusive = false;
      return self();
    }

    abstract Self self();

    public final Self inclusive() {
      inclusive = true;
      return self();
    }

    @Override public Iterator<Integer> iterator() {
      return new Iterator<Integer>() {
        int next = from;

        @Override public boolean hasNext() {
          return inclusive ? next <= to : next < to;
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
  }
}
