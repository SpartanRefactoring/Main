package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import static il.org.spartan.spartanizer.ast.safety.iz.*;

import java.util.*;

import il.org.spartan.spartanizer.java.*;

/** convert
 * <pre>
 * f(cond ? a : b)
 * <pre>
 * to
 * <pre>
 * cond ? f(a) : f(b)
 * <pre>
 * Test case is {@link Issue1091}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-18
 * [[SuppressWarningsSpartan]]
 */
public class OutlineTernaryMethodInvocation extends ReplaceCurrentNode<MethodInvocation> implements TipperCategory.InVain {

  @Override public ASTNode replacement(MethodInvocation n) {
    List<Expression> l = arguments(n);
    if(l.isEmpty())
      return null;
    ConditionalExpression f = null;
    for(int i = 0; i < l.size(); ++i) {
      if((f = az.conditionalExpression(l.get(i))) != null) {
        MethodInvocation whenTrue = copy.of(n);
        MethodInvocation whenFalse = copy.of(n);
        arguments(whenTrue).remove(i);
        arguments(whenTrue).add(i, copy.of(then(f)));
        arguments(whenFalse).remove(i);
        arguments(whenFalse).add(i, copy.of(elze(f)));
        return subject.pair(whenTrue, whenFalse).toCondition(expression(f));
      }
      if(haz.sideEffects(l.get(i)))
        return null;
    }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") MethodInvocation n) {
    return "";
  }
  }
