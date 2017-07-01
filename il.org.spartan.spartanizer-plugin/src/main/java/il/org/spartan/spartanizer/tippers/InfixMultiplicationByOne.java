package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static java.util.stream.Collectors.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Replace {@code 1*X} by {@code X}
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixMultiplicationByOne extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Transformation.Prune, Category.Theory.Arithmetics {
  private static final long serialVersionUID = 0x7AAE85AAEB6D72C8L;

  private static ASTNode replacement(final List<Expression> ¢) {
    final List<Expression> ret = ¢.stream().filter(λ -> !iz.literal1(λ)).collect(toList());
    return ret.size() == ¢.size() ? null
        : ret.isEmpty() ? copy.of(the.firstOf(¢)) : ret.size() == 1 ? copy.of(the.firstOf(ret)) : subject.operands(ret).to(TIMES);
  }
  @Override public String description(final InfixExpression ¢) {
    return "Remove all multiplications by 1 from " + ¢;
  }
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != TIMES ? null : replacement(extract.allOperands(¢));
  }
}
