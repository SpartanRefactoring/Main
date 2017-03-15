package il.org.spartan.utils;

import java.util.function.*;

/** lazy initialization as in {@code
    static final lazy<Collection<Thing>> things = lazy.get(() -> as.list(//
      new Thing("one"); //
      new Thing("two"); //
  ));} use {@code things.get()} to obtain value; it would be computed only on
 * first call, and cached hence after
 * <p>
 * This class is not expected to be instantiated by clients; use as demonstrated
 * above
 * @param <T>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-10 */
public interface lazy<@¢ T> extends Supplier<T> {
  static <T> lazy<T> get(@¢ final Supplier<T> ¢) {
    return new lazy<T>() {
      T $;

      /** No need to be {@code synchronized} to make it thread safe. Instance is
       * always unique.
       * @Return value of the supplier */
      @Override public T get() {
        return $ = $ != null ? $ : ¢.get();
      }
    };
  }
}
