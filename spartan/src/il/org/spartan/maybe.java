package il.org.spartan;
import org.eclipse.jdt.annotation.*;

/**
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day}
 */
public class maybe<@Nullable T> {
  private @Nullable T inner;

  public static <@Nullable T> maybe<T> no() {
    return new maybe<>();
  }
  public static <@Nullable T> maybe<T> yes(final T t) {
    return new maybe<>(t);
  }
  private maybe() {
    inner = null;
  }
  public maybe(final T inner) {
    this.inner = inner;
  }
  public boolean present() {
    return inner != null;
  }
  public boolean missing() {
    return inner == null;
  }
  public @Nullable T get() {
    return inner;
  }
  public maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }
  public maybe<@Nullable T> set(final T inner) {
    this.inner = inner;
    return this;
  }
}
