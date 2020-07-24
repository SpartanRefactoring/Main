package fluent.ly;

import java.util.function.Supplier;

/** A class for lazy, memoizing evaluation of objects of arbitrary type. The
 * evaluation must never return <code><b>null</b></code>. Main purpose is for
 * lazy initialization as in {@code
    static final lazy<Collection<Thing>> things = lazy.get(() -> as.list(//
      new Thing("one"), //
      new Thing("two") //
  ));} use {@code things.get()} to obtain value; it would be computed only on
 * first call, and cached hence after
 * <p>
 * This class is not expected to be instantiated by clients; use as demonstrated
 * above
 * @param <T> JD
 * @author Yossi Gil
 * @since 2017-03-10 */
public interface lazy<T> extends Supplier<T> {
  static <T> lazy<T> get(final Supplier<T> ¢) {
    return new lazy<>() {
      /** Cached value; invalid cache if {@code null} */
      T $;

      /** No need to be {@code synchronized} to make it thread safe. Instance is
       * always unique.
       * @Return value of the supplier */
      @Override public T get() {
        return $ != null ? $ : ¢.get();
      }
    };
  }
}