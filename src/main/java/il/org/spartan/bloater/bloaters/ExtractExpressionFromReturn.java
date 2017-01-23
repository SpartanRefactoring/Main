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

/** link {@Issue1000}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-07 */
public class ExtractExpressionFromReturn extends CarefulTipper<ReturnStatement>//
    implements TipperCategory.Bloater {
  @Override public String description(final ReturnStatement ¢) {
    return "Extract expression from " + ¢ + " statement";
  }

  // TODO: Doron - I spartanized your code. --yg
  @Override public Tip tip(final ReturnStatement s) {
    return expression(s) == null || !iz.assignment(expression(s)) || !iz.block(s.getParent()) ? null : new Tip(description(s), s, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Assignment a = az.assignment(expression(s));
        // TODO: Doron Meshulam: with class subject, you do not need to create
        // new objects. It does that for you.
        final ExpressionStatement exp = r.getAST().newExpressionStatement(copy.of(expression(s)));
        final ReturnStatement retNoExp = subject.operand(expression(exp)).toReturn();
        retNoExp.setExpression(copy.of(left(a)));
        final ListRewrite l = r.getListRewrite(s.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(retNoExp, s, g);
        l.insertAfter(exp, s, g);
        l.remove(s, g);
      }
    };
  }
}
