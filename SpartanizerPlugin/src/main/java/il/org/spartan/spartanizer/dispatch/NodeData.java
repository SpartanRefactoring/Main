package il.org.spartan.spartanizer.dispatch;

import org.eclipse.jdt.core.dom.*;

/** Black box implementation of data storing in {@link ASTNode}s using Objects
 * map.
 * @author Ori Roth */
public class NodeData {
  // TODO Roth: attachment.of(n).get(x)
  // TODO: Yossi Gil, erase the above as per #1
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
