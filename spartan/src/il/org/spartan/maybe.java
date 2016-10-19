package il.org.spartan;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day} */
public class maybe<@Nullable T> {
  public static <@Nullable T> maybe<T> no() {
    return new maybe<>();
  }

  public static <@Nullable T> maybe<T> yes(final T ¢) {
    return new maybe<>(¢);
  }

  private @Nullable T inner;

  /** Instantiates this class.
   * @param inner JD */
  public maybe(final @Nullable T inner) {
    this.inner = inner;
  }

  private maybe() {
    inner = null;
  }


  public maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }


  public @Nullable T get() {
    return inner;
  }


  public boolean missing() {
    return inner == null;
  }


  public boolean present() {
    return inner != null;
  }

  /** @param inner TODO document this parameter
   * */
  public maybe<@Nullable T> set(final T inner) {
    this.inner = inner;
    return this;
  }

  @SuppressWarnings({ "javadoc", "static-method" }) public static class TEST {
     * <code>usecase0</code> void TODO Javadoc(2016) automatically generated for
     * returned value of method <code>usecase0</code> */
    @Test public void usecase0() {
      azzert.isNull(maybe.no().get());
    }

    @Test public void usecase1() {
      @Nullable final Object o = null;
      azzert.isNull(maybe.yes(o).get());
    }

    @Test public void usecase2() {
      @Nullable final Object o = new Object();
      azzert.notNull(maybe.yes(o).get());
    }
  }
}
