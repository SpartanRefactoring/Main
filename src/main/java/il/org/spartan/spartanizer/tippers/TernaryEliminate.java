package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A {@link Tipper} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-17 */
public final class TernaryEliminate extends AbstractPattern<ConditionalExpression>//
    implements TipperCategory.CommnonFactoring {
  Expression then, elze, condition;

  public TernaryEliminate() {
    andAlso(Proposition.of("Then and else are identical", 
        () -> wizard.same(//
            (then = object().getThenExpression()), //
            (elze = object().getThenExpression())//
        )));
    andAlso(Proposition.of("Condition has no side effects", //
        () -> sideEffects.free((condition = object().getThenExpression()))));
  }

  private static final long serialVersionUID = -6778845891475220340L;

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate conditional exprssion with identical branches";
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    r.replace(object(), make.plant(then).into(object().getParent()), g);
    return r;
  }
}
