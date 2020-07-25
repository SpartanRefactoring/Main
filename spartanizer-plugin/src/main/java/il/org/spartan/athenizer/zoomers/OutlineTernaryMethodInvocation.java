package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue1091;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.tippers.MethodInvocationPattern;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** convert {@code
 * f(cond ? a : b)
} to {@code
 * cond ? f(a) : f(b)
 *
 * } Test case is {@link Issue1091}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @author Dor Maayan
 * @since 2017-01-18 */
public class OutlineTernaryMethodInvocation extends MethodInvocationPattern//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x54464757E1B1C1A1L;
  ConditionalExpression $;

  public OutlineTernaryMethodInvocation() {
    andAlso("Parent is not a lambda lxpression", () -> !iz.lambdaExpression(current.getParent()));
    andAlso("There is at least one argument", () -> !arguments.isEmpty());
    andAlso("All conditions satisfy:", () -> {
      for (final Expression argument : arguments) {
        if (haz.sideEffects(argument))
          return false;
        if (($ = az.conditionalExpression(argument)) != null && !iz.nullLiteral(then($)) && !iz.nullLiteral(elze($)))
          return true;
      }
      return false;
    });
  }
  @Override public Examples examples() {
    return null;
  }
  @Override protected ASTRewrite go(final ASTRewrite ret, final TextEditGroup g) {
    final MethodInvocation whenTrue = copy.of(current), whenFalse = copy.of(current);
    final int i = arguments.indexOf($);
    arguments(whenTrue).remove(i);
    arguments(whenTrue).add(i, copy.of(then($)));
    arguments(whenFalse).remove(i);
    arguments(whenFalse).add(i, copy.of(elze($)));
    ret.replace(current, subject.operand(subject.pair(whenTrue, whenFalse).toCondition(expression($))).parenthesis(), g);
    return ret;
  }
}
