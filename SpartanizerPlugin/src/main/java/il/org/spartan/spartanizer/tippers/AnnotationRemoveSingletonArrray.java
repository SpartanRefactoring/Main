package il.org.spartan.spartanizer.tippers;

import il.org.spartan.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-02-10 */
public final class AnnotationRemoveSingletonArrray extends ReplaceCurrentNode<SingleMemberAnnotation>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 8349592040340894302L;

  @Override public String description(final SingleMemberAnnotation ¢) {
    return "Remove the curly brackets in the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @Override public ASTNode replacement(final SingleMemberAnnotation a) {
    final Expression x = lisp.onlyOne(expressions(az.arrayInitializer(a.getValue())));
    if (x == null)
      return null;
    final SingleMemberAnnotation $ = copy.of(a);
    $.setValue(copy.of(x));
    return $;
  }
}