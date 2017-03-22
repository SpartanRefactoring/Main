package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Convert (a=b=??;) to (a=3;b=??;) Tested in {@link Issue0999}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-24 */
public class AssignmentAndAssignmentBloater extends CarefulTipper<ExpressionStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 121321364655045957L;

  @NotNull @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }

  // TODO Doron - I spartanized your code. --yg
  @Nullable @Override public Fragment tip(@NotNull final ExpressionStatement ¢) {
    @Nullable final Assignment $ = az.assignment(expression(¢));
    return $ == null || !iz.assignment(right($)) ? null : new Fragment(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final AST create = ¢.getAST();
        // TODO Doron Meshulam: use class subject --yg
        @Nullable final Assignment newTail = copy.of($), p = rightMost(newTail), newHead = copy.of(az.assignment(right(p)));
        p.setRightHandSide(copy.of(left(newHead)));
        final ExpressionStatement head = create.newExpressionStatement(newHead), tail = create.newExpressionStatement(newTail);
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(tail, ¢, g);
        l.insertAfter(head, ¢, g);
        l.remove(¢, g);
      }

      @Nullable public Assignment rightMost(final Assignment newTail) {
        for (@Nullable @SuppressWarnings("hiding") Assignment $ = newTail;; $ = az.assignment(right($)))
          if (!iz.assignment(right(az.assignment(right($)))))
            return $;
      }
    };
  }
}
