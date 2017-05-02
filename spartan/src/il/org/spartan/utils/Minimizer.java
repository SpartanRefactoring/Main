package il.org.spartan.utils;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since Mar 6, 2012
 * @param <T> an arbitrary type */
public class Minimizer<T> {
  private double min = Double.NaN;
  private T value;
  private int index;
  private int maxIndex = -1;

  public int index() {
    return maxIndex;
  }

  public double min() {
    return min;
  }

  @NotNull public Minimizer<T> next(final T t, final double next) {
    ___.nonnull(t);
    if (Double.isNaN(min) || next < min) {
      min = next;
      value = t;
      maxIndex = index;
    }
    ++index;
    return this;
  }

  public T value() {
    ___.nonnull(value);
    return value;
  }
}
