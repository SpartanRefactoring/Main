package fluent.ly;

import org.eclipse.jdt.annotation.Nullable;

public class maybe<@Nullable T> {
  public static <T> maybe<@Nullable T> no() {
    return new maybe<>(null);
  }

  public static <T> maybe<@Nullable T> yes(final T ¢) {
    return new maybe<>(¢);
  }

  private T inner;

  /**
   * Instantiates this class.
   *
   * @param inner JD
   */
  public maybe(final T inner) {
    this.inner = inner;
  }

  public maybe<T> clear() {
    inner = null;
    return this;
  }

  public T get() {
    return inner;
  }

  public boolean missing() {
    return inner == null;
  }

  public boolean present() {
    return inner != null;
  }

  public maybe<T> set(final T inner) {
    this.inner = inner;
    return this;
  }
}
