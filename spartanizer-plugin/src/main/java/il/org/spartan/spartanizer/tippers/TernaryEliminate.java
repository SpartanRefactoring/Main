package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.java.sideEffects;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** A {@link Tipper} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class TernaryEliminate extends NodeMatcher<ConditionalExpression>//
    implements Category.EmptyCycles {
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
