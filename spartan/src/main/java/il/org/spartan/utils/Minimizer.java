package il.org.spartan.utils;

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

  public Minimizer<T> next(final T t, final double next) {
    assert t != null;
    if (Double.isNaN(min) || next < min) {
      min = next;
      value = t;
      maxIndex = index;
    }
    ++index;
    return this;
  }

  public T value() {
    assert value != null;
    return value;
  }
}
