package il.org.spartan.spartanizer.dispatch;

import org.eclipse.jdt.core.dom.*;

/**
 * Black box implementation of data storing in {@link ASTNode}s using Objects map.
 * 
 * @author Ori Roth
 */
public class NodeData {
  /**
   * Sets a value under key for this node.
   * @param n an {@link ASTNode}
   * @param key property name
   * @param value property value
   */
  static void set(ASTNode n, String key, Object value) {
    if (n != null)
      n.setProperty(key, value);
  }
  /**
   * Sets a binary flag true.
   * @param n an {@link ASTNode}
   * @param key property name
   */
  static void set(ASTNode n, String key) {
    set(n, key, Boolean.TRUE);
  }
  /**
   * Unsets a key property for this node.
   * @param n an {@link ASTNode}
   * @param key property name
   */
  static void unset(ASTNode n, String key) {
    if (n != null)
      n.setProperty(key, null);
  }
  /**
   * Checks node has a property.
   * @param n an {@link ASTNode}
   * @param key property name
   * @return true iff node contains the key property
   */
  static boolean has(ASTNode n, String key) {
    return n != null && n.properties().keySet().contains(key);
  }
  /**
   * Get property from node.
   * @param n an {@link ASTNode}
   * @param key property name
   * @return key property of node, null if it does not have this property.
   */
  static Object get(ASTNode n, String key) {
    return n == null ? null : n.getProperty(key);
  }
}
