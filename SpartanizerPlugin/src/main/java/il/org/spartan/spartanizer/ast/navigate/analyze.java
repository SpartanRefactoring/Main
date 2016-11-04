package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** A class to find all sort all things about a node, generally some small
 * analyses.
 * @author Ori Marcovitch
 * @since 2016 */
public enum analyze {
  ;
  public static Set<Name> dependencies(final ASTNode n) {
    final Set<Name> $ = new HashSet<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleName node) {
        if (!izMethodName(node))
          $.add(node);
        return true;
      }

      boolean izMethodName(final SimpleName ¢) {
        return iz.methodInvocation(step.parent(¢)) && step.name(az.methodInvocation(step.parent(¢))).equals(¢);
      }

      @Override public boolean visit(final QualifiedName node) {
        $.add(node);
        return true;
      }
    });
    return $;
  }

  public static String type(Name n) {
    MethodDeclaration m = searchAncestors.forContainingMethod().from(n);
    String s = findDeclarationInMethod(n, m);
    return s != null ? s : findDeclarationInType(n, searchAncestors.forContainingType().from(n));
  }

  private static String findDeclarationInType(Name n, TypeDeclaration t) {
    for (FieldDeclaration d : step.fieldDeclarations(t))
      for (VariableDeclarationFragment ¢ : step.fragments(d))
        if ((step.name(¢) + "").equals((n + "")))
          return step.type(d) + "";
    return null;
  }

  private static String findDeclarationInMethod(Name n, MethodDeclaration d) {
    Str str = new Str();
    d.accept(new ASTVisitor() {
      @Override public boolean visit(SingleVariableDeclaration ¢) {
        if (str.inner != null || !(step.name(¢) + "").equals((n + "")))
          return true;
        str.inner = step.type(¢) + "";
        return false;
      }

      @Override public boolean visit(VariableDeclarationFragment ¢) {
        if (str.inner != null || !(step.name(¢) + "").equals((n + "")))
          return true;
        str.inner = step.type(¢) + "";
        return false;
      }
    });
    return str.inner;
  }

  public static Set<VariableDeclaration> enviromentVariables(@SuppressWarnings("unused") final ASTNode __) {
    // TODO: Marco search for all known variables
    // MethodDeclaration m = searchAncestors.forContainingMethod().from(n);
    return null;
  }
}
