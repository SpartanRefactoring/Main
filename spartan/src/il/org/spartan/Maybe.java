package il.org.spartan;
import org.eclipse.jdt.annotation.*;

/**
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day}
 */
public class Maybe<@Nullable T> {
  private @Nullable T inner;

  public static <@Nullable T> Maybe<T> no() {
    return new Maybe<>();
  }
  public static <@Nullable T> Maybe<T> yes(final T t) {
    return new Maybe<>(t);
  }
  private Maybe() {
    inner = null;
  }
  public Maybe(final T inner) {
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
  public Maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }
  public Maybe<@Nullable T> set(final T inner) {
    this.inner = inner;
    return this;
  }
}
