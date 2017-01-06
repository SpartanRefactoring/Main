package il.org.spartan.spartanizer.ast.safety;

import org.eclipse.jdt.core.dom.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-27 [[SuppressWarningsSpartan]] */
public enum property {
  ;
  public interface Attached {
    void to(ASTNode n);
  }

  public interface Obtainer<N> {
    N from(ASTNode n);
  }

  static <N> String key(final Class<N> ¢) {
    return ¢.getCanonicalName();
  }

  public static Attached attach(final Object o) {
    return ¢ -> ¢.setProperty(key(o.getClass()), o);
  }

  public static <N> Obtainer<N> obtain(final Class<N> c) {
    return new Obtainer<N>() {
      @Override @SuppressWarnings("unchecked") public N from(final ASTNode n) {
        return (N) n.getProperty(key(c));
      }
    };
  }
}
