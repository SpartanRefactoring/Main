package il.org.spartan.utils;

import java.util.function.*;

/** neat class for lazy initialization
 * @param <T>
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-10 */
public interface lazy<T> extends Supplier<T> {
  static <T> lazy<T> get(final Supplier<T> ¢) {
    return new lazy<T>() {
      T $;

      @Override public T get() {
        return $ = $ != null ? $ : ¢.get();
      }
    };
  }
}
