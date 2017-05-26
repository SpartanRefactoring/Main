package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * b || false
 * } to {@code
 * b
 * }
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class InfixConditionalOrFalse extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onBooleans {
  private static final long serialVersionUID = -0x5E623D693022FD8DL;

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'false' argument to '||'";
  }
  @Override public boolean prerequisite(final InfixExpression ¢) {
    return iz.conditionalOr(¢) && have.falseLiteral(extract.allOperands(¢));
  }
  @Override public Expression replacement(final InfixExpression ¢) {
    return remove.literal(¢, false);
  }
}
