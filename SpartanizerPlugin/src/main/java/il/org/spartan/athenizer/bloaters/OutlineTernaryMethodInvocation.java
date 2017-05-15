package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

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
    implements TipperCategory.Bloater {
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
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final MethodInvocation whenTrue = copy.of(current), whenFalse = copy.of(current);
    final int i = arguments.indexOf($);
    arguments(whenTrue).remove(i);
    arguments(whenTrue).add(i, copy.of(then($)));
    arguments(whenFalse).remove(i);
    arguments(whenFalse).add(i, copy.of(elze($)));
    r.replace(current, make.parethesized(subject.pair(whenTrue, whenFalse).toCondition(expression($))), g);
    return r;
  }
}
