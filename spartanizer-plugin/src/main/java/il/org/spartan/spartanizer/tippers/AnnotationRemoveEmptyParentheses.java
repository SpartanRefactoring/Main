package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.values;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Removes the parentheses from annotations that do not take arguments,
 * converting {@code @Override()}> to {@code @Override}>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationRemoveEmptyParentheses extends ReplaceCurrentNode<NormalAnnotation>//
    implements Category.Transformation.Prune {
  private static final long serialVersionUID = -0x2BB9B06C96DB60B8L;

  @Override public String description(final NormalAnnotation ¢) {
    return "Remove redundant parenthesis from the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }
  @Override public ASTNode replacement(final NormalAnnotation ¢) {
    if (!values(¢).isEmpty())
      return null;
    final MarkerAnnotation $ = ¢.getAST().newMarkerAnnotation();
    $.setTypeName(copy.of(¢.getTypeName()));
    return $;
  }
}
