package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code 1*X} by {@code X}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-09-05 */
public final class InfixMultiplicationByOne extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onNumbers {
  private static final long serialVersionUID = 8840150087715615432L;

  private static ASTNode replacement(final List<Expression> ¢) {
    final List<Expression> $ = new ArrayList<>(¢.stream().filter(λ -> !iz.literal1(λ)).collect(toList()));
    return $.size() == ¢.size() ? null : $.isEmpty() ? copy.of(first(¢)) : $.size() == 1 ? copy.of(first($)) : subject.operands($).to(TIMES);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Remove all multiplications by 1 from " + ¢;
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != TIMES ? null : replacement(extract.allOperands(¢));
  }
}
