package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** Use {@link #examples()} for documentation
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-02 */
public class ParenthesizedRemoveExtraParenthesis extends CarefulTipper<ParenthesizedExpression>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 4265122877786904832L;

  @NotNull
  @Override public Tip tip(@NotNull final ParenthesizedExpression x) {
    return new Tip(description(x), x, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace(x, copy.of(expression(x)), g);
      }
    };
  }

  @NotNull
  @Override public String description(final ParenthesizedExpression ¢) {
    return "remove extra parenthesis " + trivia.gist(¢);
  }

  @NotNull
  @Override public Example[] examples() {
    return new Example[] { //
        convert("((x)).f();") //
            .to("(x).f();"), //
        convert("(x).f();") //
            .to("x.f();"), //
        convert("f((x));") //
            .to("f(x);"), //
        // TODO Marco: does not pass
        // converts("int i = (x).y;")
        // .to("int i = x.y;")
    };
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
