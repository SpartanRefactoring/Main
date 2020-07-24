package fluent.ly;

import org.junit.*;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day} */
public class maybe<T> {
  public static <T> maybe<T> no() {
    return new maybe<>();
  }
  public static <T> maybe<T> yes(final T ¢) {
    return new maybe<>(¢);
  }

  private T inner;

  /** Instantiates this class.
   * @param inner JD */
  public maybe(final T inner) {
    this.inner = inner;
  }
  private maybe() {
    inner = null;
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
  /** @param inner TODO document this parameter */
  public maybe<T> set(final T inner) {
    this.inner = inner;
    return this;
  }
}
