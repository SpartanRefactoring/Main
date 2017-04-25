package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts 'for(?;true;?)' to 'for(?;;?)'";
 * @author Alex Kopzon
 * @since 2016 */
public class ForTrueConditionRemove extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.Loops {
  private static final long serialVersionUID = 0x6788CE2890CE2390L;
  public static final String DESCRIPTION = "Remove implicit 'true' in 'for(?;true;?)' converting it to 'for(?;;?)'";

  @Override public String description() {
    return DESCRIPTION;
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return DESCRIPTION;
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    return iz.literal.true¢(step.expression(¢));
  }

  @Override public ASTNode replacement(final ForStatement ¢) {
    final ForStatement $ = copy.of(¢);
    $.setExpression(null);
    return $;
  }
}
