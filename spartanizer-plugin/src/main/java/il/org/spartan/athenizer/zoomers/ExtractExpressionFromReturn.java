package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.left;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** link {@Issue1000}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2017-01-07 */
public class ExtractExpressionFromReturn extends CarefulTipper<ReturnStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = -0x619C6820E2C43DE4L;

  @Override public String description(final ReturnStatement ¢) {
    return "Extract expression from " + ¢ + " statement";
  }
  @Override public Tip tip(final ReturnStatement s) {
    return expression(s) == null || !iz.assignment(expression(s)) || !iz.block(s.getParent()) ? null : new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Assignment a = az.assignment(expression(s));
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
