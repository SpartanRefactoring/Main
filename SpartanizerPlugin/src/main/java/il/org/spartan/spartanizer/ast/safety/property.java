package il.org.spartan.spartanizer.ast.safety;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Yossi Gil <tt>yossi.gil@gmail.com</tt> please add a description
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-27 */
public enum property {
  ;
  @FunctionalInterface
  public interface Attached {
    void to(ASTNode n);
  }

  @FunctionalInterface
  public interface Obtainer<N> {
    @NotNull N from(ASTNode n);
  }

  public static Attached attach(@NotNull final Object o) {
    return λ -> λ.setProperty(key(o.getClass()), o);
  }

  /** Get property from node.
   * @param n JD
   * @param key property name
   * @return key property of node, null if it does not have this property. */
  @SuppressWarnings("unchecked") //
  @Nullable public static <T> T get(@Nullable final ASTNode n, @NotNull final String key) {
    return n == null ? null : (T) n.getProperty(key);
  }

  /** Checks node has a property.
   * @param n JD
   * @param key property name
   * @return <code><b>true</b></code> <em>iff</em> node contains the key
   *         property */
  public static boolean has(@Nullable final ASTNode n, final String key) {
    return n != null && n.properties().keySet().contains(key);
  }

  static <N> String key(@NotNull final Class<N> ¢) {
    return ¢.getCanonicalName();
  }

  @SuppressWarnings("unchecked") public static <N> Obtainer<N> obtain(@NotNull final Class<N> c) {
    return λ -> (N) λ.getProperty(key(c));
  }

  /** Sets a binary flag true.
   * @param n JD
   * @param key property name */
  public static void set(final ASTNode n, @NotNull final String key) {
    set(n, key, Boolean.TRUE);
  }

  /** Sets a value under key for this node.
   * @param n JD
   * @param key property name
   * @param value property value */
  @Nullable public static <T> T set(@Nullable final ASTNode n, @NotNull final String key, final T value) {
    if (n == null)
      return null;
    n.setProperty(key, value);
    return value;
  }

  /** Unsets a key property for this node.
   * @param n an {@link ASTNode}
   * @param key property name */
  public static void unset(@Nullable final ASTNode n, @NotNull final String key) {
    if (n != null)
      n.setProperty(key, null);
  }
}
