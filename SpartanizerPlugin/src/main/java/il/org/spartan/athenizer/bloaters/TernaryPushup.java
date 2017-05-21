package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.factory.subject.*;
import static il.org.spartan.utils.Proposition.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.athenizer.*;
import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** Example in {@link #examples} Test case is {@link Issue1049}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30 */
public final class TernaryPushup extends Multiciary implements BloaterCategory.Ternarization {
  private static final long serialVersionUID = 0x711512B65712ADF4L;
  Expression operandCondition, operandThen, operandElze;
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
