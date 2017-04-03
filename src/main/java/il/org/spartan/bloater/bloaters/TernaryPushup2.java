package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.factory.subject.*;
import static il.org.spartan.utils.Proposition.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Example in {@link #examples} Test case is {@link Issue1049}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30 */
public final class TernaryPushup2 extends InfixExprezzion implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x711512B65712ADF4L;
  Expression operandCondition, operandThen, operandElze;
  private ConditionalExpression leftConditional;
  private ConditionalExpression operandConditional;

  @Override public Examples examples() {
    return convert("x = a + (cond ? b : c);").to("x = cond ? a + b : a + c;").convert("x = (cond ? b : c) + a;").to("x = cond ? b + a : c + a;");
  }

  public TernaryPushup2() {
    andAlso(OR("Right or left operand is ternary expression",
        () -> iz.not.null¢(operandConditional = az.conditionalExpression(extract.core(right))) //
            && iz.not.null¢(operandCondition = step.expression(operandConditional)) //
            && iz.not.null¢(operandThen = step.then(operandConditional)) //
            && iz.not.null¢(operandElze = step.elze(operandConditional)),
        () -> iz.not.null¢(leftConditional = az.conditionalExpression(extract.core(left)))
            && iz.not.null¢(operandCondition = step.expression(leftConditional)) //
            && iz.not.null¢(operandThen = step.then(leftConditional)) //
            && iz.not.null¢(operandElze = step.elze(leftConditional))));
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

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "";
  }
}
