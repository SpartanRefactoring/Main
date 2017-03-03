package il.org.spartan.bloater.bloaters;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** convert {@code
 * f(cond ? a : b)
} to {@code
 * cond ? f(a) : f(b)
 *
 * } Test case is {@link Issue1091}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-18 */
public class OutlineTernaryMethodInvocation extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -2446197092801176661L;

  @Override public ASTNode replacement(final MethodInvocation n) {
    if (n == null || iz.lambdaExpression(n.getParent()))
      return null;
    final List<Expression> l = arguments(n);
    if (l.isEmpty())
      return null;
    // TODO Yuval Simon: move into loop --yg
    for (int i = 0; i < l.size(); ++i) {
      ConditionalExpression $;
      if (($ = az.conditionalExpression(l.get(i))) != null) {
        if (iz.nullLiteral(then($)) || iz.nullLiteral(elze($)))
          return null;
        final MethodInvocation whenTrue = copy.of(n), whenFalse = copy.of(n);
        arguments(whenTrue).remove(i);
        arguments(whenTrue).add(i, copy.of(then($)));
        arguments(whenFalse).remove(i);
        arguments(whenFalse).add(i, copy.of(elze($)));
        return make.parethesized(subject.pair(whenTrue, whenFalse).toCondition(expression($)));
      }
      if (haz.sideEffects(l.get(i)))
        break;
    }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "";
  }
}
