package il.org.spartan;

import org.eclipse.jdt.annotation.*;

/** A generic wrapper classes which can store and retrieve values of any type.
 * Main use is in
 * @author Yossi Gil
 * @since 2015-08-02
 * @param <T> JD */
public class Wrapper<T> {
  /** Instantiates this class */
  public Wrapper() {
    //
  }
  /** Instantiates this class
   * @param t JD */
  public Wrapper(final T t) {
    this.t = t;
  }
  /** @return the value wrapped in this object. */
  public @Nullable T get() {
    return t;
  }
  /** Set the value wrapped in this object.
   * @param t JD */
  public void set(final T t) {
    this.t = t;
  }
  @Override public String toString() {
    return "Wrapper of " + t;
  }

  @Nullable protected T t = null;
}
