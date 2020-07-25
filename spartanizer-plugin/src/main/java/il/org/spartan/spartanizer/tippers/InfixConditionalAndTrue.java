package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.have;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code
 * b && true
 * } to {@code
 * b
 * }
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class InfixConditionalAndTrue extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Transformation.Prune, Category.Theory.Logical {
  private static final long serialVersionUID = -0x756E452341001644L;

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'true' argument to '&&'";
  }
  @Override public boolean prerequisite(final InfixExpression ¢) {
    return iz.conditionalAnd(¢) && have.trueLiteral(extract.allOperands(¢));
  }
  @Override public Expression replacement(final InfixExpression ¢) {
    return remove.literal(¢, true);
  }
}
