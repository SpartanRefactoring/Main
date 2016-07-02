/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import java.util.function.*;

import org.eclipse.jdt.annotation.*;

/**
 * An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> utility functions of reasonably wide use.
 *
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>
 * @since 2013/07/01
 */
public enum idiomatic {
  ;
  /**
   * Quote a given {@link String}
   *
   * @param $ some {@link String} to be quoted
   * @return the parameter, quoted
   */
  public static String quote(final @Nullable String $) {
    return $ != null ? QUOTE + $ + QUOTE : "<null reference>";
  }

  /**
   *
   */
  public static final String QUOTE = "'";

  /**
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  public interface Hold {
    /**
     * @param <T> JD
     * @param t
     * @return TODO document return type
     */
    <@Nullable T> T eval(final Supplier<T> t);
    /**
     * @param <T> JD
     * @param t JD
     * @return TODO document return type
     */
    default <@Nullable T> T eval(final T t) {
      return eval(() -> t);
    }
  }

  private static final Hold ignore = new Hold() {
    @Override public <@Nullable T> T eval(final Supplier<T> __) {
      return null;
    }
  };
  private static final Hold eval = new Hold() {
    @Override public <@Nullable T> T eval(final Supplier<T> __) {
      return null;
    }
  };

  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static <T> @Nullable T unless(final boolean condition, final T t) {
    return incase(!condition, t);
  }
  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static <T> @Nullable T incase(final boolean condition, final T t) {
    return condition ? t : null;
  }
  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static Hold unless(final boolean condition) {
    return incase(!condition);
  }
  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static Hold incase(final boolean condition) {
    return condition ? eval : ignore;
  }
}