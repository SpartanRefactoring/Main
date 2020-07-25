package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.factory.subject.pair;
import static il.org.spartan.utils.Proposition.OR;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.not;
import il.org.spartan.athenizer.zoom.zoomers.Issue1049;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.tippers.Multiciary;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Example in {@link #examples} Test case is {@link Issue1049}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30 */
public final class TernaryPushup extends Multiciary implements Category.Bloater {
  private static final long serialVersionUID = 0x711512B65712ADF4L;
  Expression operandCondition, operandThen, operandElze;
  /** [[SuppressWarningsSpartan]] */
  private ConditionalExpression leftConditional;
  private ConditionalExpression operandConditional;

  @Override public Examples examples() {
    return convert("x = a + (cond ? b : c);").to("x = cond ? a + b : a + c;").convert("x = (cond ? b : c) + a;").to("x = cond ? b + a : c + a;");
  }
  public TernaryPushup() {
    andAlso(OR("Right or left operand is ternary expression",
        () -> not.nil(operandConditional = az.conditionalExpression(extract.core(right))) //
            && not.nil(operandCondition = step.expression(operandConditional)) //
            && not.nil(operandThen = step.then(operandConditional)) //
            && not.nil(operandElze = step.elze(operandConditional)),
        () -> not.nil(leftConditional = az.conditionalExpression(extract.core(left))) && not.nil(operandCondition = step.expression(leftConditional)) //
            && not.nil(operandThen = step.then(leftConditional)) //
            && not.nil(operandElze = step.elze(leftConditional))));
    andAlso("Condition has no side effect", //
        () -> !haz.sideEffects(operandCondition));
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(current,
        (iz.conditionalExpression(extract.core(right)) ? pair(pair(left, operandThen).to(operator), pair(left, operandElze).to(operator))
            : pair(pair(operandThen, right).to(operator), pair(operandElze, right).to(operator))).toCondition(operandCondition),
        g);
    return r;
  }
  @Override public String description() {
    return "";
  }
}
