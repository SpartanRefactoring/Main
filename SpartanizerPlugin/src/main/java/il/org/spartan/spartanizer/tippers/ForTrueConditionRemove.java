package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;



/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016 */
public class ForTrueConditionRemove extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.SyntacticBaggage {
   private static ForStatement buildForWhithoutCondition( final ForStatement $) {
    $.setExpression(null);
    return $;
  }

  @Override  public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Convert loop: 'for(?;" + "true" + ";?)' to 'for(?;;?)'";
  }

  @Override public boolean prerequisite( final ForStatement ¢) {
    return iz.literal.true¢(step.expression(¢));
  }

  @Override  public ASTNode replacement(final ForStatement ¢) {
    return buildForWhithoutCondition(copy.of(¢));
  }
}
