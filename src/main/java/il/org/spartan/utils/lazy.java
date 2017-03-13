package il.org.spartan.utils;

import java.util.function.*;

/** lazy initialization as in {@code
    static final lazy<Collection<Thing>> things = lazy.get(() -> as.list(//
      new Thing("one"); //
      new Thing("two"); //
  ));} use {@code things.get()} to obtain value; it would be computed only on
 * first call, and cached hence after
 * @param <T>
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-10 */
public interface lazy<@JD T> extends Supplier<T> {
  static <T> lazy<T> get(@JD final Supplier<T> ¢) {
    return new lazy<T>() {
      T $;

      @Override public T get() {
        return $ = $ != null ? $ : ¢.get();
      }
    };
  }
}
