package il.org.spartan;

import java.util.Objects;

/**
 * A generic wrapper classes which can store and retrieve values of any type.
 *
 * @author Yossi Gil
 * @since 2015-08-02
 * @param <T> JD
 */
public class Wrapper<T> {
  protected T inner;

  /** Instantiates this class */
  @SuppressWarnings("null") public Wrapper() {
    this(null);
  }

  /**
   * Instantiates this class
   *
   * @param inner JD
   */
  public Wrapper(final T inner) {
    this.inner = inner;
  }

  @Override @SuppressWarnings("unchecked") public Wrapper<T> clone() throws CloneNotSupportedException {
    return (Wrapper<T>) Utils.cantBeNull(super.clone());
  }

  @Override public final boolean equals(final Object ¢) {
    return super.equals(¢) || ¢ != null && getClass() == ¢.getClass() && equals((Wrapper<?>) ¢);
  }

  /**
   * @param ¢ JD
   * @return <code><b>true</b></code> <i>iff</i> method <code>equals</code>
   *         returns <code><b>true</b></code> for the wrapped objects.
   */
  public boolean equals(final Wrapper<?> ¢) {
    return Objects.equals(inner, ¢.inner); 
  }

  /** @return value wrapped in this object. */
  public T get() {
    return inner;
  }

  @Override public int hashCode() {
    return Objects.hash(inner); 
  }

  /** set current value */
  public void set(final T inner) {
    this.inner = inner;
  }

  @Override public String toString() {
    return inner == null ? "null" : Utils.cantBeNull(inner + "");
  }
}
