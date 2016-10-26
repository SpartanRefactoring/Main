package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** A class to find all sort all things about a node, typically some small
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

  public static Set<VariableDeclaration> enviromentVariables(@SuppressWarnings("unused") final ASTNode __) {
    // TODO: Marco search for all known variables
    // MethodDeclaration m = searchAncestors.forContainingMethod().from(n);
    return null;
  }
}
