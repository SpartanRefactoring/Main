package il.org.spartan.spartanizer.java.namespace;

import org.eclipse.jdt.core.dom.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-27 */
public enum property {
  ;
  interface Attached {
    void to(ASTNode n);
  }

  public static Attached attach(final Object o) {
    return ¢ -> ¢.setProperty(o.getClass().getCanonicalName(), o);
  }
}
