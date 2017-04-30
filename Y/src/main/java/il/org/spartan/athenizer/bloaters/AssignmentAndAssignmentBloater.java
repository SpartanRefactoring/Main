package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Convert (a=b=??;) to (a=3;b=??;) Tested in {@link Issue0999}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-24 */
public class AssignmentAndAssignmentBloater extends CarefulTipper<ExpressionStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x1AF05236BEDFD45L;

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }

  @Override public Tip tip(final ExpressionStatement ¢) {
    final Assignment $ = az.assignment(expression(¢));
    return $ == null || !iz.assignment(right($)) ? null : new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final AST create = ¢.getAST();
        // TODO Doron Meshulam: use class subject --yg
        final Assignment newTail = copy.of($), p = rightMost(newTail), newHead = copy.of(az.assignment(right(p)));
        p.setRightHandSide(copy.of(left(newHead)));
        final ExpressionStatement head = create.newExpressionStatement(newHead), tail = create.newExpressionStatement(newTail);
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(tail, ¢, g);
        l.insertAfter(head, ¢, g);
        l.remove(¢, g);
      }

      public Assignment rightMost(final Assignment newTail) {
        for (@SuppressWarnings("hiding") Assignment $ = newTail;; $ = az.assignment(right($)))
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
