package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * b && true
 * } to {@code
 * b
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-20 */
public final class InfixConditionalAndTrue extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onBooleans {
  private static final long serialVersionUID = -8461776767592437316L;

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'true' argument to '&&'";
  }

  @Override public boolean prerequisite(@NotNull final InfixExpression ¢) {
    return iz.conditionalAnd(¢) && have.trueLiteral(extract.allOperands(¢));
  }

  @Override public Expression replacement(final InfixExpression ¢) {
    return Tippers.eliminateLiteral(¢, true);
  }
}
