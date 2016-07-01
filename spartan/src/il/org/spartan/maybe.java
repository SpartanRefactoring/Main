package il.org.spartan;
import org.eclipse.jdt.annotation.*;

/**
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day}
 */
public class maybe<@Nullable T> {
  private @Nullable T inner;

  /**
   * @param <T>
   * @return
   */
  public static <@Nullable T> maybe<T> no() {
    return new maybe<>();
  }
  /**
   * @param <T>
   * @param t
   * @return
   */
  public static <@Nullable T> maybe<T> yes(final T t) {
    return new maybe<>(t);
  }
  private maybe() {
    inner = null;
  }
  /**
   * Instantiates this class.
   *
   * @param inner
   */
  public maybe(final @Nullable T inner) {
    this.inner = inner;
  }
  /**
   * @return
   */
  public boolean present() {
    return inner != null;
  }
  /**
   * @return
   */
  public boolean missing() {
    return inner == null;
  }
  /**
   * @return
   */
  public @Nullable T get() {
    return inner;
  }
  /**
   * @return
   */
  public maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }
  public maybe<@Nullable T> set(final T inner) {
    this.inner = inner;
    return this;
  }
}
