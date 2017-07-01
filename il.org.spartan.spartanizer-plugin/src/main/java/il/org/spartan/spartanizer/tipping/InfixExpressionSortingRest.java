package il.org.spartan.spartanizer.tipping;

import static fluent.ly.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Yossi Gil Document Class
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public abstract class InfixExpressionSortingRest extends InfixExpressionSorting {
  private static final long serialVersionUID = -0x110FB52AE2E828EFL;

  @Override public final boolean prerequisite(final InfixExpression ¢) {
    if (!suitable(¢))
      return false;
    final List<Expression> $ = extract.allOperands(¢);
    return $.size() > 2 && !misc.mixedLiteralKind($) && sort(chop($));
  }
  @Override public final Expression replacement(final InfixExpression ret) {
    final List<Expression> operands = extract.allOperands(ret);
    final Expression first = operands.remove(0);
    if (!sort(operands))
      return null;
    operands.add(0, first);
    return subject.operands(operands).to(ret.getOperator());
  }
}
