package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** ((x)) to (x) <br>
 * (x); to x; <br>
 * (x).y to x.y
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-02 */
public class ParenthesizedRemoveExtraParenthesis extends CarefulTipper<ParenthesizedExpression>//
    implements TipperCategory.SyntacticBaggage {
  @NotNull
  @Override public Tip tip(@NotNull final ParenthesizedExpression x) {
    return new Tip(description(x), x, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace(x, copy.of(expression(x)), g);
      }
    };
  }

  @NotNull
  @Override public String description(@NotNull final ParenthesizedExpression ¢) {
    return "remove extra parenthesis " + trivia.gist(¢);
  }

  @Override protected boolean prerequisite(final ParenthesizedExpression ¢) {
    return doubleParenthesis(¢)//
        || fluental(¢);
  }

  private static boolean doubleParenthesis(final ParenthesizedExpression ¢) {
    return iz.parenthesizedExpression(parent(¢))//
        || iz.methodInvocation(parent(¢))//
            && arguments(az.methodInvocation(parent(¢))).contains(¢);
  }

  private static boolean fluental(final ParenthesizedExpression ¢) {
    return iz.methodInvocation(parent(¢))//
        && expression(az.methodInvocation(parent(¢))) == ¢//
        && (iz.methodInvocation(expression(¢))//
            || iz.name(expression(¢))//
            || iz.literal(expression(¢)));
  }
}
