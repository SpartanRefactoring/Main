package il.org.spartan.spartanizer.dispatch;

import org.eclipse.jdt.core.dom.*;

/** Black box implementation of data storing in {@link ASTNode}s using Objects
 * map.
 * @author Ori Roth */
public class NodeData {
  /** Get property from node.
   * @param n an {@link ASTNode}
   * @param key property name
   * @return key property of node, null if it does not have this property. */
  public static Object get(final ASTNode n, final String key) {
    return n == null ? null : n.getProperty(key);
  }

  /** Checks node has a property.
   * @param n an {@link ASTNode}
   * @param key property name
   * @return true iff node contains the key property */
  public static boolean has(final ASTNode n, final String key) {
    return n != null && n.properties().keySet().contains(key);
  }

  /** Sets a binary flag true.
   * @param n an {@link ASTNode}
   * @param key property name */
  public static void set(final ASTNode n, final String key) {
    set(n, key, Boolean.TRUE);
  }

  /** Sets a value under key for this node.
   * @param n an {@link ASTNode}
   * @param key property name
   * @param value property value */
  public static Object set(final ASTNode n, final String key, final Object value) {
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
