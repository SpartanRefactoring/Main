package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** Replace {@code 1*X} by {@code X}
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixMultiplicationByOne extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onNumbers {
  private static ASTNode replacement(@NotNull final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    $.addAll(xs.stream().filter(λ -> !iz.literal1(λ)).collect(Collectors.toList()));
    return $.size() == xs.size() ? null : $.isEmpty() ? copy.of(first(xs)) : $.size() == 1 ? copy.of(first($)) : subject.operands($).to(TIMES);
  }

  @Override @NotNull public String description(final InfixExpression ¢) {
    return "Remove all multiplications by 1 from " + ¢;
  }

  @Override public ASTNode replacement(@NotNull final InfixExpression ¢) {
    return ¢.getOperator() != TIMES ? null : replacement(extract.allOperands(¢));
  }
}
