package il.org.spartan.spartanizer.ast.safety;

import org.eclipse.jdt.core.dom.*;

/** TODO:  Yossi Gil <tt>yossi.gil@gmail.com</tt>
 please add a description 
 @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-27 
 */

public enum property {
  ;
  public interface Attached {
    void to(ASTNode n);
  }

  public interface Obtainer<N> {
    N from(ASTNode n);
  }

  public static Attached attach(final Object o) {
    return ¢ -> ¢.setProperty(key(o.getClass()), o);
  }

  /** Get property from node.
   * @param n JD
   * @param key property name
   * @return key property of node, null if it does not have this property. */
  @SuppressWarnings("unchecked") //
  public static <T> T get(final ASTNode n, final String key) {
    return n == null ? null : (T) n.getProperty(key);
  }

  /** Checks node has a property.
   * @param n JD
   * @param key property name
   * @return <code><b>true</b></code> <em>iff</em> node contains the key
   *         property */
  public static boolean has(final ASTNode n, final String key) {
    return n != null && n.properties().keySet().contains(key);
  }

  static <N> String key(final Class<N> ¢) {
    return ¢.getCanonicalName();
  }

  @SuppressWarnings("unchecked") public static <N> Obtainer<N> obtain(final Class<N> c) {
    return ¢ -> (N) ¢.getProperty(key(c));
  }

  /** Sets a binary flag true.
   * @param n JD
   * @param key property name */
  public static void set(final ASTNode n, final String key) {
    set(n, key, Boolean.TRUE);
  }

  /** Sets a value under key for this node.
   * @param n JD
   * @param key property name
   * @param value property value */
  public static <T> T set(final ASTNode n, final String key, final T value) {
    if (n == null)
      return null;
    n.setProperty(key, value);
    return value;
  }

  /** Unsets a key property for this node.
   * @param n an {@link ASTNode}
   * @param key property name */
  public static void unset(final ASTNode n, final String key) {
    if (n != null)
      n.setProperty(key, null);
  }
}

