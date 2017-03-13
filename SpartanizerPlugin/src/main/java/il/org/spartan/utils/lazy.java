package il.org.spartan.utils;

import java.util.function.*;

/** lazy initialization as in {@code
    static final lazy<Collection<Thing>> things = lazy.get(() -> as.list(//
      new Thing("one"); //
      new Thing("two"); //
  ));} use {@code things.get()} to obtain value; it would be computed only on
 * first call, and cached hence after
 * @param <T>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-10 */
public interface lazy<@¢ T> extends Supplier<T> {
  static <T> lazy<T> get(@¢ final Supplier<T> ¢) {
    return new lazy<T>() {
      T $;

      @Override public T get() {
        return $ = $ != null ? $ : ¢.get();
      }
    };
  }
}
