package il.org.spartan.spartanizer.engine;

import static il.org.spartan.idiomatic.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.corext.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** Some useful utility functions used for binding manipulations.
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-24 */
@SuppressWarnings("restriction") public final class BindingUtils {
  /** @param n an {@link ASTNode}
   * @return type in which n is placed, or null if there is none */
  public static ITypeBinding container(final ASTNode n) {
    final ASTNode $ = hop.containerType(n);
    return eval(() -> ((TypeDeclaration) $).resolveBinding()).when($ != null && $ instanceof TypeDeclaration);
  }

  /** @param compilationUnit current compilation unit
   * @return current package */
  public static IPackageBinding getPackage(final CompilationUnit ¢) {
    return ¢.getPackage().resolveBinding();
  }

  /** Finds visible method in hierarchy.
   * @param b base type
   * @param methodName method name
   * @param bs method parameters
   * @param n original {@link ASTNode} containing the method invocation. Used in
   *        order to determine the context in which the method is being used
   * @param u current {@link CompilationUnit}
   * @return method's binding if it is visible from context, else null */
  public static IMethodBinding getVisibleMethod(final ITypeBinding b, final String methodName, final ITypeBinding[] bs, final ASTNode n,
      final CompilationUnit u) {
    if (b == null)
      return null;
    final IMethodBinding $ = Bindings.findMethodInHierarchy(b, methodName, bs);
    return take($).when(isVisible($, n, u));
  }

  /** Determines whether an invocation of a method is legal in a specific
   * context.
   * @param b a method
   * @param n the context in which the method is invoked
   * @param u current {@link CompilationUnit}
   * @return true iff method is visible from its context */
  public static boolean isVisible(final IMethodBinding b, final ASTNode n, final CompilationUnit u) {
    final int ms = b.getModifiers();
    if (Modifier.isPublic(ms))
      return true;
    final ITypeBinding mc = b.getDeclaringClass();
    if (Modifier.isProtected(ms) && mc.getPackage().equals(getPackage(u)))
      return true;
    final ITypeBinding nc = container(n);
    return nc != null && nc.equals(mc);
  }
}
