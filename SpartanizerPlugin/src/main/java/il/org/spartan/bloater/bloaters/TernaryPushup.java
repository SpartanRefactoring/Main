package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

import static il.org.spartan.spartanizer.ast.safety.iz.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 * 
 * <pre>
 * a + (cond ? b : c)
 * 
 * <pre>
 * to
 * 
 * <pre>
 * cond ? a + b : a + c
 * 
 * <pre>
 * Test case is {@link Issue1049}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-18 [[SuppressWarningsSpartan]] */
public class TernaryPushup extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  @Override public ASTNode replacement(final InfixExpression n) {
    Expression l = left(n);
    Expression r = right(n);
    if (parenthesizedExpression(l))
      l = expression(az.parenthesizedExpression(l));
    if (parenthesizedExpression(r))
      r = expression(az.parenthesizedExpression(r));
    if (conditionalExpression(r)) {
      final ConditionalExpression rr = az.conditionalExpression(r);
      final InfixExpression le = subject.pair(l, then(rr)).to(operator(n));
      final InfixExpression ri = subject.pair(l, elze(rr)).to(operator(n));
      return subject.pair(le, ri).toCondition(expression(rr));
    }
    final ConditionalExpression ll = az.conditionalExpression(l);
    final InfixExpression le = subject.pair(then(ll), r).to(operator(n));
    final InfixExpression ri = subject.pair(elze(ll), r).to(operator(n));
    return subject.pair(le, ri).toCondition(expression(ll));
  }

  @Override protected boolean prerequisite(final InfixExpression n) {
    if (n == null)
      return false;
    Expression l = left(n);
    Expression r = right(n);
    if (parenthesizedExpression(l))
      l = expression(az.parenthesizedExpression(l));
    if (parenthesizedExpression(r))
      r = expression(az.parenthesizedExpression(r));
    if (conditionalExpression(l) && !haz.sideEffects(expression(az.conditionalExpression(l))))
      return true;
    if (conditionalExpression(r) && !haz.sideEffects(expression(az.conditionalExpression(r))))
      return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression n) {
    return "";
  }
}
