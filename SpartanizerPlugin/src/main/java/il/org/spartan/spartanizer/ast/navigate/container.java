package il.org.spartan.spartanizer.ast.navigate;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface container {
  static CompilationUnit compilationUnit(final ASTNode ¢) {
    return (CompilationUnit) yieldAncestors.untilNodeType(COMPILATION_UNIT).from(¢);
  }

  /** @param ¢ JD
   * @return ASTNode of the type if one of ¢'s parent ancestors is a container
   *         type and null otherwise */
  static ASTNode typeDeclaration(final ASTNode ¢) {
    for (final ASTNode $ : hop.ancestors(¢.getParent()))
      if (iz.nodeTypeIn($//
          , ANONYMOUS_CLASS_DECLARATION //
          , ANNOTATION_TYPE_DECLARATION //
          , ENUM_DECLARATION //
          , TYPE_DECLARATION //
          , ENUM_CONSTANT_DECLARATION //
      ))
        return $;
    return null;
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
    for (ASTNode $ = ¢; $ != null; $ = $.getParent())
      if (iz.methodDeclaration($))
        return az.methodDeclaration($);
    return null;
  }
}
