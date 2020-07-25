package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue0999;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Convert (a=b=??;) to (a=3;b=??;) Tested in {@link Issue0999}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-24 */
public class AssignmentAndAssignmentBloater extends CarefulTipper<ExpressionStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x1AF05236BEDFD45L;

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }
  @Override public Tip tip(final ExpressionStatement ¢) {
    final Assignment ret = az.assignment(expression(¢));
    return ret == null || !iz.assignment(right(ret)) ? null : new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final AST create = ¢.getAST();
        // TODO Doron Meshulam: use class subject --yg
        final Assignment newTail = copy.of(ret), p = rightMost(newTail), newHead = copy.of(az.assignment(right(p)));
        p.setRightHandSide(copy.of(left(newHead)));
        final ExpressionStatement head = create.newExpressionStatement(newHead), tail = create.newExpressionStatement(newTail);
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(tail, ¢, g);
        l.insertAfter(head, ¢, g);
        l.remove(¢, g);
      }
      public Assignment rightMost(final Assignment newTail) {
        for ( Assignment $ = newTail;; $ = az.assignment(right($)))
          if (!iz.assignment(right(az.assignment(right($)))))
            return $;
      }
    };
  }
  @Override public Examples examples() {
    return //
    convert("a=b=5;")//
        .to("b=5; a=b;") //
        .convert("a+=b+=3;").//
        to("b += 3; a += b;") //
    ;
  }
}
