package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} to convert an expression such as
 *
 * <pre>
 * 0 + X = X
 * </pre>
 *
 * or
 *
 * <pre>
 * X + 0 = X
 * </pre>
 *
 * to
 *
 * <pre>
 * X
 * </i>
 * or
 * <pre>
 * X + 0 + Y
 * </pre>
 *
 * to
 *
 * <pre>
 * X + Y
 * </pre>
 *
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixAdditionZero2 extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  @Override public ASTNode replacement(final InfixExpression x) {
    gather(x, new ArrayList<Expression>());
    x.getOperator();
    extract.allOperands(x);
    extract.allOperators(x);
    final List<Expression> ops = extract.allOperands(x);
    final ArrayList<Expression> ops2 = new ArrayList<>();
    for (int ¢ = 0; ¢ < ops.size(); ++¢)
      if (!iz.literal0(ops.get(¢)))
        ops2.add(ops.get(¢));
    InfixExpression $ = null;
    for (int ¢ = 0; ¢ < ops2.size() - 1; ++¢)
      $ = subject.pair($ != null ? $ : ops2.get(¢), ops2.get(¢ + 1)).to(Operator.PLUS);
    return ops2.size() != 1 ? $ : ops2.get(0);
  }

  private static boolean containsZeroOperand(final InfixExpression x) {
    for (final Expression ¢ : extract.allOperands(x))
      if (iz.literal0(¢))
        return true;
    return false;
  }

  /**  */
  private static boolean containsPlusOperator(final InfixExpression x) {
    for (final Operator ¢ : extract.allOperators(x))
      if (¢ == Operator.PLUS)
        return true;
    return false;
  }

  public static ASTNode replacement2(final InfixExpression x) {
    final List<Expression> ops = extract.allOperands(x);
    final ArrayList<Expression> ops2 = new ArrayList<>();
    for (int ¢ = 0; ¢ < ops.size(); ++¢)
      if (!iz.literal0(ops.get(¢)))
        ops2.add(ops.get(¢));
    InfixExpression $ = null;
    for (int ¢ = 0; ¢ < ops2.size() - 1; ++¢)
      $ = subject.pair($ != null ? $ : ops2.get(¢), ops2.get(¢ + 1)).to(Operator.PLUS);
    return ops2.size() != 1 ? $ : ops2.get(0);
  }

  @Override public boolean prerequisite(final InfixExpression $) {
    return $ != null && iz.infixPlus($) && containsZeroOperand($) && containsPlusOperator($);
  }

  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
  }

  private static List<Expression> gather(final InfixExpression x, final List<Expression> $) {
    if (x == null)
      return $;
    if (!in(x.getOperator(), PLUS, MINUS)) {
      $.add(x);
      return $;
    }
    gather(core(left(x)), $);
    gather(core(right(x)), $);
    if (x.hasExtendedOperands())
      gather(extendedOperands(x), $);
    return $;
  }

  private static List<Expression> gather(final List<Expression> xs, final List<Expression> $) {
    for (final Expression ¢ : xs)
      gather(¢, $);
    return $;
  }

  @Override public String description() {
    return "remove 0 in expressions like ";
  }

  @Override public String description(final InfixExpression ¢) {
    return description() + ¢;
  }
}
