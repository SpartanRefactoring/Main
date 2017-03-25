package il.org.spartan.spartanizer.issues;

import il.org.spartan.spartanizer.ast.safety.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-25 */
public enum not {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  @SafeVarargs public static <T> boolean in(final T t, final T... ts) {
    return !iz.in(t, ts);
  }
}
