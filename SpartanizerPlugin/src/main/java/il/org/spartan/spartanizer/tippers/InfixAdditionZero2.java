package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.plugin.PreferencesResources.*;
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
  @SuppressWarnings("unused") @Override public ASTNode replacement(final InfixExpression x) {
    final List<Expression> c = gather(x, new ArrayList<Expression>());
    final Operator b = x.getOperator();
    final List<Expression> allOperands = extract.allOperands(x);
    final List<Operator> allOperators = extract.allOperators(x);
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

  private boolean containsZeroOperand(final InfixExpression ¢) {
    final List<Expression> allOperands = extract.allOperands(¢);
    for (final Expression opnd : allOperands)
      if (iz.literal0(opnd))
        return true;
    return false;
  }

  /** [[SuppressWarningsSpartan]] */
  private static boolean containsPlusOperator(final InfixExpression e) {
    for (final Operator o : extract.allOperators(e))
      if (o == Operator.PLUS)
        return true;
    return false;
  }

  // public ASTNode replacement2(final InfixExpression ¢) {
  // List<Expression> ops = extract.allOperands(¢);
  // ArrayList<Expression> ops2 = new ArrayList<Expression>();
  // for(int i=0; i < ops.size(); i++){
  // Expression ¢2 = ops.get(i);
  // if(!iz.literal0(¢2)){
  // ops2.add(ops.get(i));
  // }
  // }
  // InfixExpression inexp = null;
  // for(int i=0; i < ops2.size()-1; i++ ){
  // if(inexp != null)
  // inexp = subject.pair(inexp, ops2.get(i+1)).to(Operator.PLUS);
  // else
  // inexp = subject.pair(ops2.get(i), ops2.get(i+1)).to(Operator.PLUS);
  // }
  // if(ops2.size() == 1)
  // return ops2.get(0);
  // return inexp;
  // }
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

  @Override public TipperGroup tipperGroup() {
    return TipperGroup.Abbreviation;
  }
}
