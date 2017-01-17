package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts (a?b:c;) to (if(a) b; else c;) relevant to return <ternary> also
 * relevant for return (<ternary>) Issue #883 {@link Issue0883}
 * @author Raviv Rachmiel
 * @author Yuval Simon
 * @since 03-12-16 */
public class ReturnTernaryExpander extends CarefulTipper<ReturnStatement> implements TipperCategory.Expander {
  @Override public Tip tip(final ReturnStatement x) {
    return new Tip(description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression ee = expression(x);
        final ConditionalExpression e = az.conditionalExpression(!iz.parenthesizedExpression(ee) ? ee : expression(ee));
        final Expression cond = expression(e);
        final AST a = x.getAST();
        final ReturnStatement whenTrue = a.newReturnStatement();
        final ReturnStatement whenFalse = a.newReturnStatement();
        whenTrue.setExpression(copy.of(then(e)));
        whenFalse.setExpression(copy.of(elze(e)));
        final IfStatement f = a.newIfStatement();
        f.setExpression(copy.of(cond));
        f.setThenStatement(copy.of(whenTrue));
        final ListRewrite l = r.getListRewrite(x.getParent(),
            iz.block(x.getParent()) ? Block.STATEMENTS_PROPERTY : SwitchStatement.STATEMENTS_PROPERTY);
        l.insertAfter(whenFalse, x, g);
        l.replace(x, f, g);
      }
    };
  }

  @Override protected boolean prerequisite(final ReturnStatement $) {
    if ($ == null)
      return false;
    final Expression e = expression($);
    return (iz.block($.getParent()) || iz.switchStatement($.getParent()))
        && (iz.conditionalExpression(e) || iz.parenthesizedExpression(e) && iz.conditionalExpression(expression(az.parenthesizedExpression(e))));
  }

  @Override public String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
