package il.org.spartan;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day} */
public class maybe<@Nullable T> {
  @SuppressWarnings({ "javadoc", "static-method" }) public static class TEST {
    /** TODO Javadoc(2016): automatically generated for method
     * <code>usecase0</code> void TODO Javadoc(2016) automatically generated for
     * returned value of method <code>usecase0</code> */
    @Test public void usecase0() {
      final maybe<@Nullable Object> o = maybe.no();
      azzert.isNull(o.get());
    }

    @Test public void usecase1() {
      @Nullable final Object o = null;
      final maybe<@Nullable Object> m = maybe.yes(o);
      azzert.isNull(m.get());
    }

    @Test public void usecase2() {
      @Nullable final Object o = new Object();
      final maybe<@Nullable Object> m = maybe.yes(o);
      azzert.notNull(m.get());
    }
  }

  /** @param <T> JD
   * @return TODO document return type */
  public static <@Nullable T> maybe<T> no() {
    return new maybe<>();
  }

  /** @param <T> JD
   * @param t JD
   * @return TODO document return type */
  public static <@Nullable T> maybe<T> yes(final T t) {
    return new maybe<>(t);
  }

  private @Nullable T inner;

  private maybe() {
    inner = null;
  }

  /** Instantiates this class.
   * @param inner JD */
  public maybe(final @Nullable T inner) {
    this.inner = inner;
  }

  /** @return TODO document return type */
  public maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }

  /** @return TODO document return type */
  public @Nullable T get() {
    return inner;
  }

  /** @return TODO document return type */
  public boolean missing() {
    return inner == null;
  }

  /** @return TODO document return type */
  public boolean present() {
    return inner != null;
  }

  /** @param inner TODO document this parameter
   * @return TODO document return type */
  public maybe<@Nullable T> set(final T inner) {
    this.inner = inner;
    return this;
  }
}
