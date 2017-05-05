package fluent.ly;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jdt.annotation.*;
import org.junit.*;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day} */
public class maybe< T> {
   public static < T> maybe<T> no() {
    return new maybe<>();
  }
   public static < T> maybe<T> yes(final T ¢) {
    return new maybe<>(¢);
  }

   private T inner;

  /** Instantiates this class.
   * @param inner JD */
  public maybe(final  T inner) {
    this.inner = inner;
  }
  private maybe() {
    inner = null;
  }
   public maybe< T> clear() {
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
   public maybe< T> set(final T inner) {
    this.inner = inner;
    return this;
  }

  @SuppressWarnings("static-method")
  public static class TEST {
    @Test public void usecase0() {
      azzert.isNull(maybe.no().get());
    }
    @Test public void usecase1() {
      azzert.isNull(maybe.yes(null).get());
    }
    @Test public void usecase2() {
      assert maybe.yes(new Object()).get() != null;
    }
  }
}
