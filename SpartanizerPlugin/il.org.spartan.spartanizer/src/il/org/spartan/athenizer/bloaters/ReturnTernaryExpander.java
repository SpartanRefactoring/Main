package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts (a?b:c;) to (if(a) b; else c;) relevant to return <em>ternary</em>
 * also relevant for return (ternary>) Issue #883 {@link Issue0883}
 * @author Raviv Rachmiel
 * @author Yuval Simon
 * @since 03-12-16 */
public class ReturnTernaryExpander extends CarefulTipper<ReturnStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -0x3A172046EB702ADAL;

  @Override public Tip tip(final ReturnStatement x) {
    return new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression ee = expression(x);
        final ConditionalExpression e = az.conditionalExpression(!iz.parenthesizedExpression(ee) ? ee : expression(ee));
        final Expression cond = expression(e);
        final AST a = x.getAST();
        final ReturnStatement whenTrue = a.newReturnStatement(), whenFalse = a.newReturnStatement();
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
    return $ != null && (iz.block($.getParent()) || iz.switchStatement($.getParent())) && iz.conditionalExpression(extract.core(expression($)));
  }

  @Override public String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
