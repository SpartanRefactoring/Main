package il.org.spartan.spartanizer.ast.navigate;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface containing {
  static CompilationUnit compilationUnit(final ASTNode ¢) {
    return az.compilationUnit(yieldAncestors.untilNodeType(COMPILATION_UNIT).from(¢));
  }

  static String package¢(final CastExpression ¢) {
    return yieldAncestors.untilContainingCompilationUnit().from(¢).getPackage().getName() + "";
  }

  /** @param ¢ JD
   * @return ASTNode of the type if one of ¢'s parent ancestors is a container
   *         type and null otherwise */
  static ASTNode typeDeclaration(final ASTNode ¢) {
    return az.stream(hop.ancestors(parent(¢)))
        .filter(λ -> iz.nodeTypeIn(λ, ANONYMOUS_CLASS_DECLARATION //
            , ANNOTATION_TYPE_DECLARATION //
            , ENUM_DECLARATION //
            , TYPE_DECLARATION //
            , ENUM_CONSTANT_DECLARATION //
        )).findFirst().orElse(null);
  }

  static BodyDeclaration bodyDeclaration(final ASTNode ¢) {
    return yieldAncestors.untilClass(BodyDeclaration.class).from(¢);
  }

  /** Extract the {@link MethodDeclaration} that contains a given node.
   * @param pattern JD
   * @return inner most {@link MethodDeclaration} in which the parameter is
   *         nested, or <code><b>null</b></code>, if no such statement
   *         exists. */
  static MethodDeclaration methodDeclaration(final ASTNode ¢) {
    for (ASTNode $ = ¢; $ != null; $ = parent($))
      if (iz.methodDeclaration($))
        return az.methodDeclaration($);
    return null;
  }
}
