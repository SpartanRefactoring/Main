package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.fieldDeclarations;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.utils.Str;

/** A class to find all sort all things about a node, generally some small
 * analyses.
 * @author Ori Marcovitch
 * @author Ward Mattar
 * @author Vivian Shehadeh
 * @since 2016 */
public enum analyze {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static Collection<String> dependencies(final ASTNode n) {
    final Collection<String> $ = new HashSet<>();
    // noinspection SameReturnValue,SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SimpleName node) {
        if (notMethodName(node))
          $.add(identifier(node));
        return true;
      }
      boolean notMethodName(final Name ¢) {
        return (!iz.methodInvocation(parent(¢)) || !identifier(az.methodInvocation(parent(¢))).equals(¢ + ""))
            && (!iz.methodDeclaration(parent(¢)) || !identifier(az.methodDeclaration(parent(¢))).equals(¢ + ""));
      }
      @Override public boolean visit(final QualifiedName node) {
        if (notMethodName(node))
          $.add(identifier(node));
        return true;
      }
    });
    return $;
  }
  public static Collection<String> dependencies(final Iterable<Expression> arguments) {
    final Set<String> $ = new HashSet<>();
    for (final Expression ¢ : arguments) {
      $.addAll(analyze.dependencies(¢));
      if (iz.name(¢))
        $.add(az.name(¢) + "");
    }
    return $;
  }
  public static String type(final Name n) {
    final MethodDeclaration m = yieldAncestors.untilContainingMethod().from(n);
    final String $ = m == null ? null : findDeclarationInMethod(n, m);
    return $ != null ? $ : findDeclarationInType(n, yieldAncestors.untilContainingType().from(n));
  }
  private static String findDeclarationInType(final Name n, final AbstractTypeDeclaration d) {
    if (!iz.typeDeclaration(d)) // TODO Marco support all types of types
      return null;
    for (final FieldDeclaration $ : fieldDeclarations(az.typeDeclaration(d)))
      for (final VariableDeclarationFragment ¢ : fragments($))
        if (identifier(¢).equals(n + ""))
          return step.type($) + "";
    return null;
  }
  private static String findDeclarationInMethod(final Name n, final MethodDeclaration d) {
    final Str $ = new Str();
    d.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SingleVariableDeclaration ¢) {
        if ($.notEmpty() || !identifier(¢).equals(n + ""))
          return true;
        $.set(step.type(¢));
        return false;
      }
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        if ($.notEmpty() || !(step.name(¢) + "").equals(n + ""))
          return true;
        $.set(step.type(¢));
        return false;
      }
    });
    return $.inner();
  }
}
