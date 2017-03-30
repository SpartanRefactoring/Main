package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

import static il.org.spartan.spartanizer.ast.factory.subject.*;
import static il.org.spartan.utils.Example.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;

/** Example in {@link #examples} 
 * Test case is {@link Issue1049}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30
 */
public final class TernaryPushup2 extends InfixExprezzion implements TipperCategory.Bloater {
  private static final long serialVersionUID = 8148439675150970356L;
  Expression condition, then, elze;
  boolean rightIsTernary; 
  
  @Override public Example[] examples() {
    return new Example[] {
        convert("x = a + (cond ? b : c);")
            .to("x = cond ? a + b : a + c;"),
        convert("x = (cond ? b : c) + a;")
            .to("x = cond ? b + a : c + a;")
    };
  }
  
  public TernaryPushup2() {
    andAlso(Proposition.OR("Right or left operand is ternary expression", 
        () -> {
          ConditionalExpression $;
          return ($ = az.conditionalExpression(extract.core(right))) != null &&
              (condition = step.expression($)) != null &&
              (then = step.then($)) != null &&
              (elze = step.elze($)) != null &&
              (rightIsTernary = true);
        }, 
        () -> {
          ConditionalExpression e;
          return (e = az.conditionalExpression(extract.core(left))) != null &&
              (condition = step.expression(e)) != null &&
              (then = step.then(e)) != null &&
              (elze = step.elze(e)) != null;
        }));
    andAlso(Proposition.of("Condition has no side effect", () -> !haz.sideEffects(condition)));
  }
  
  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    r.replace(current, (rightIsTernary ? pair(pair(left, then).to(operator), pair(left, elze).to(operator))
        : pair(pair(then, right).to(operator), pair(elze, right).to(operator))).toCondition(condition), g);
    return r;
  }

  @Override public String description(@SuppressWarnings("unused") InfixExpression __) {
    return "";
  }
}
