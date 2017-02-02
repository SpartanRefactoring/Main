package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016 */
public class ForTrueConditionRemove extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.SyntacticBaggage {
  @NotNull private static ForStatement buildForWhithoutCondition(@NotNull final ForStatement $) {
    $.setExpression(null);
    return $;
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Convert loop: 'for(?;" + "true" + ";?)' to 'for(?;;?)'";
  }

  @Override public boolean prerequisite(@Nullable final ForStatement ¢) {
    return iz.literal.true¢(step.expression(¢));
  }

  @Override @Nullable public ASTNode replacement(final ForStatement ¢) {
    return buildForWhithoutCondition(copy.of(¢));
  }
}
