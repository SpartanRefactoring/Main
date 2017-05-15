package il.org.spartan.spartanizer.ast.navigate;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** An empty {@code interface} for fluent API. The name of this __ should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface containing {
  static BodyDeclaration bodyDeclaration(final ASTNode ¢) {
    return yieldAncestors.untilClass(BodyDeclaration.class).from(¢);
  }
  static CompilationUnit compilationUnit(final ASTNode ¢) {
    return az.compilationUnit(yieldAncestors.untilNodeType(COMPILATION_UNIT).from(¢));
  }
  /** Extract the {@link MethodDeclaration} that contains a given node.
   * @param pattern JD
   * @return inner most {@link MethodDeclaration} in which the parameter is
   *         nested, or {@code null, if no such statement
   *         exists. */
  static MethodDeclaration methodDeclaration(final ASTNode ¢) {
    for (ASTNode $ = ¢; $ != null; $ = parent($))
      if (iz.methodDeclaration($))
        return az.methodDeclaration($);
    return null;
  }
  static String package¢(final CastExpression ¢) {
    return yieldAncestors.untilContainingCompilationUnit().from(¢).getPackage().getName() + "";
  }
  /** extract the {@link Statement} that contains a given node.
   * @param pattern JD
   * @return inner most {@link Statement} in which the parameter is nested, or
   *         {@code null, if no such statement exists. */
  static Statement statement(final ASTNode ¢) {
    for (ASTNode $ = ¢; $ != null; $ = parent($))
      if (iz.statement($))
        return az.statement($);
    return null;
  }
  /** @param ¢ JD
   * @return ASTNode of the __ if one of ¢'s parent ancestors is a container __
   *         and null otherwise */
  static ASTNode typeDeclaration(final ASTNode ¢) {
    return az.stream(hop.ancestors(parent(¢)))
        .filter(λ -> iz.nodeTypeIn(λ, //
            ANONYMOUS_CLASS_DECLARATION //
            , ANNOTATION_TYPE_DECLARATION //
            , ENUM_DECLARATION //
            , TYPE_DECLARATION //
            , ENUM_CONSTANT_DECLARATION //
        )).findFirst().orElse(null);
  }
}
