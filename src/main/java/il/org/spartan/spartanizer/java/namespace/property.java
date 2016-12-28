package il.org.spartan.spartanizer.java.namespace;

import org.eclipse.jdt.core.dom.*;

/** 
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-27 
 * [[SuppressWarningsSpartan]]
 */
public enum property {
  ;
  interface Attached {
    void to(ASTNode n);
  }

  interface Obtainer<N> {
    N from(ASTNode n);
  }

  static <N> String key(Class<N> ¢) {
    return ¢.getCanonicalName();
  }

  public static Attached attach(final Object o) {
    return ¢ -> ¢.setProperty(key(o.getClass()), o);
  }

  static <N> Obtainer<N> obtain(Class<N> c) {
    return new Obtainer<N>() {
      @Override @SuppressWarnings("unchecked") public N from(ASTNode n) {
        return (N) n.getProperty(key(c));
      }
    };
  }
}
