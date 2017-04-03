package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** convert {@code
 * f(cond ? a : b)
} to {@code
 * cond ? f(a) : f(b)
 *
 * } Test case is {@link Issue1091}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-18 */
public class OutlineTernaryMethodInvocation2 extends MethodInvocationAbstarctPattern//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 6072619590329942433L;

  public OutlineTernaryMethodInvocation2() {
    andAlso("Parnt is not a Lambda Expression", () -> !iz.lambdaExpression(current.getParent()));
    andAlso("There is at least one argument argument", () -> !arguments.isEmpty());
  }

  @Override public Examples examples() {
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final MethodInvocation ¢) {
    return null;
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    for (int i = 0; i < arguments.size(); ++i) {
      final ConditionalExpression $;
      if (($ = az.conditionalExpression(arguments.get(i))) != null) {
        if (iz.nullLiteral(then($)) || iz.nullLiteral(elze($)))
          return null;
        final MethodInvocation whenTrue = copy.of(current), whenFalse = copy.of(current);
        arguments(whenTrue).remove(i);
        arguments(whenTrue).add(i, copy.of(then($)));
        arguments(whenFalse).remove(i);
        arguments(whenFalse).add(i, copy.of(elze($)));
        r.replace(current, make.parethesized(subject.pair(whenTrue, whenFalse).toCondition(expression($))), g);
        return r;
      }
      if (haz.sideEffects(arguments.get(i)))
        break;
    }
    return null;
  }
}
