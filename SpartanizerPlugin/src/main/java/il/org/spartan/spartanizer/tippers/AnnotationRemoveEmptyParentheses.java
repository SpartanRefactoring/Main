package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** Removes the parentheses from annotations that do not take arguments,
 * converting {@code @Override()}> to {@code @Override}>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationRemoveEmptyParentheses extends ReplaceCurrentNode<NormalAnnotation>//
    implements TipperCategory.SyntacticBaggage {
  @Override @NotNull public String description(@NotNull final NormalAnnotation ¢) {
    return "Remove redundant parentheses from the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @Override public ASTNode replacement(@NotNull final NormalAnnotation ¢) {
    if (!values(¢).isEmpty())
      return null;
    final MarkerAnnotation $ = ¢.getAST().newMarkerAnnotation();
    $.setTypeName(copy.of(¢.getTypeName()));
    return $;
  }
}
