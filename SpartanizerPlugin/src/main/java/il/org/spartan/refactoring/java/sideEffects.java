package il.org.spartan.refactoring.java;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PrefixExpression.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

public interface sideEffects {

  public static boolean sideEffectsFree(final Expression... es) {
    for (final Expression e : es)
      if (!Is.sideEffectFree(e))
        return false;
    return true;
  }

  public static boolean sideEffectsFree(final List<?> os) {
    for (final Object o : os)
      if (o == null || !Is.sideEffectFree(Funcs.asExpression((ASTNode) o)))
        return false;
    return true;
  }

  public static boolean sideEffectFreeArrayCreation(final ArrayCreation c) {
    final ArrayInitializer i = c.getInitializer();
    return sideEffectsFree(c.dimensions()) && (i == null || sideEffectsFree(i.expressions()));
  }

  public static boolean sideEffectFreePrefixExpression(final PrefixExpression e) {
    return in(e.getOperator(), PrefixExpression.Operator.PLUS, PrefixExpression.Operator.MINUS, PrefixExpression.Operator.COMPLEMENT,
        PrefixExpression.Operator.NOT) && Is.sideEffectFree(extract.operand(e));
  }

  static boolean deterministic(final Expression e) {
    if (!Is.sideEffectFree(e))
      return false;
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.TRUE);
    e.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final ArrayCreation __) {
        $.set(Boolean.FALSE);
        return false;
      }
    });
    return $.get().booleanValue();
  }
}
