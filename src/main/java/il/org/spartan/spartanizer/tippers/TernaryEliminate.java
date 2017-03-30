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

/** A {@link Tipper} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-17 */
public final class TernaryEliminate extends AbstractPattern<ConditionalExpression>//
    implements TipperCategory.CommnonFactoring {
  Expression then, elze, condition;

  public TernaryEliminate() {
    andAlso("Then and else are identical",
        () -> wizard.eq(//
            then = current().getThenExpression(), //
            elze = current().getElseExpression()));
    andAlso("Condition has no side effects", //
        () -> sideEffects.free(condition = current().getExpression())//
    );
  }

  private static final long serialVersionUID = -6778845891475220340L;

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate conditional exprssion with identical branches";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(current(), make.plant(then).into(current().getParent()), g);
    return r;
  }
}
