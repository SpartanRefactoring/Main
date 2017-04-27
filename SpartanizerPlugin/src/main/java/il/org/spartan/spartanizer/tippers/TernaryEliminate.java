package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A {@link Tipper} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class TernaryEliminate extends NodePattern<ConditionalExpression>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -0x5E134C6C247F2774L;

  public TernaryEliminate() {
    andAlso("Then and else are identical",
        () -> wizard.eq(//
            then = current().getThenExpression(), //
            elze = current().getElseExpression()));
    andAlso("Condition has no side effects", //
        () -> sideEffects.free(condition = current().getExpression())//
    );
  }

  @Override public String description() {
    return "Eliminate conditional exprssion with identical branches";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(current(), make.plant(then).into(current().getParent()), g);
    return r;
  }

  Expression then, elze, condition;

  // TODO: Ori Roth, exmample test not working, please fix -rr
  @Override public Examples examples() {
    return null;
    // return convert("a() ? f() :f()").to("f();");
  }
}
