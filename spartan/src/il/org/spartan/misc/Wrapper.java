package il.org.spartan.misc;

import org.eclipse.jdt.annotation.*;

/**
 * A generic wrapper classes which can store and retrieve values of any type.
 * Main use is in
 *
 * @author Yossi Gil
 * @since 2015-08-02
 * @param <T> JD
 */
public class Wrapper<T> {
  @Override public String toString() {
    return "Wrapper of " + t;
  }

  private @Nullable T t = null;

  /**
   * Instantiates this class
   *
   * @param t JD
   */
  public Wrapper(final T t) {
    this.t = t;
  }
  /**
   * Instantiates this class
   */
  public Wrapper() {
    //
  }
  /**
   * Set the value wrapped in this object.
   *
   * @param t JD
   */
  public void set(final T t) {
    this.t = t;
  }
  /**
   * @return the value wrapped in this object.
   */
  public T get() {
    return t;
  }
}
