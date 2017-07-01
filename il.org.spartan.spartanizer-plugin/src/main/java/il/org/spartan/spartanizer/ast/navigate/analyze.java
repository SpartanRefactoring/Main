package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** A class to find all sort all things about a node, generally some small
 * analyses.
 * @author Ori Marcovitch
 * @author Ward Mattar
 * @author Vivian Shehadeh
 * @since 2016 */
public enum analyze {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static Collection<String> dependencies(final ASTNode n) {
    final Collection<String> ret = new HashSet<>();
    // noinspection SameReturnValue,SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SimpleName node) {
        if (notMethodName(node))
          ret.add(identifier(node));
        return true;
      }
      boolean notMethodName(final Name ¢) {
        return (!iz.methodInvocation(parent(¢)) || !identifier(az.methodInvocation(parent(¢))).equals(¢ + ""))
            && (!iz.methodDeclaration(parent(¢)) || !identifier(az.methodDeclaration(parent(¢))).equals(¢ + ""));
      }
      @Override public boolean visit(final QualifiedName node) {
        if (notMethodName(node))
          ret.add(identifier(node));
        return true;
      }
    });
    return ret;
  }
  public static Collection<String> dependencies(final Iterable<Expression> arguments) {
    final Set<String> ret = new HashSet<>();
    for (final Expression ¢ : arguments) {
      ret.addAll(analyze.dependencies(¢));
      if (iz.name(¢))
        ret.add(az.name(¢) + "");
    }
    return ret;
  }
  public static String type(final Name n) {
    final MethodDeclaration m = yieldAncestors.untilContainingMethod().from(n);
    final String ret = m == null ? null : findDeclarationInMethod(n, m);
    return ret != null ? ret : findDeclarationInType(n, yieldAncestors.untilContainingType().from(n));
  }
  private static String findDeclarationInType(final Name n, final AbstractTypeDeclaration d) {
    if (!iz.typeDeclaration(d)) // TODO Marco support all types of types
      return null;
    for (final FieldDeclaration ret : fieldDeclarations(az.typeDeclaration(d)))
      for (final VariableDeclarationFragment ¢ : fragments(ret))
        if (identifier(¢).equals(n + ""))
          return step.type(ret) + "";
    return null;
  }
  private static String findDeclarationInMethod(final Name n, final MethodDeclaration d) {
    final Str ret = new Str();
    d.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SingleVariableDeclaration ¢) {
        if (ret.notEmpty() || !identifier(¢).equals(n + ""))
          return true;
        ret.set(step.type(¢));
        return false;
      }
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        if (ret.notEmpty() || !(step.name(¢) + "").equals(n + ""))
          return true;
        ret.set(step.type(¢));
        return false;
      }
    });
    return ret.inner();
  }
}
