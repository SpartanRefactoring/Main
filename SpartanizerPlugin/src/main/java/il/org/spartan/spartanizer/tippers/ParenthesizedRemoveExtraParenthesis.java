package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Proposition.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Use {@link #examples()} for documentation
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-02 */
public class ParenthesizedRemoveExtraParenthesis extends NodePattern<ParenthesizedExpression>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x3B30C2A8E5E15900L;
  private Expression inner;

  public ParenthesizedRemoveExtraParenthesis() {
    notNil("Extract inner", () -> inner = current.getExpression());
    andAlso(OR(//
        that("Around literal", () -> iz.literal(inner)), //
        that("Around simple name", () -> iz.name(inner)), //
        that("Double parenthesis", () -> iz.parenthesizedExpression(parent)), //
        that("Method argument", () -> iz.methodInvocation(parent) && arguments(az.methodInvocation(parent)).contains(current)), //
        that("Method receiver",
            () -> iz.methodInvocation(inner) && iz.methodInvocation(parent) && expression(az.methodInvocation(parent)) == current), //
        // that("Field access", () -> iz.fieldAccess(parent) &&
        // expression(az.fieldAccess(parent)) == current), //
        that("Double parenthesis", () -> iz.parenthesizedExpression(parent)), F));
  }
  @Override protected ASTNode highlight() {
    return null;
  }
  @Override public ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(current, copy.of(inner), g);
    return r;
  }
  @Override public String description() {
    return "Remove redundant parenthesis around " + Trivia.gist(inner);
  }
  @Override public Examples examples() {
    return //
    convert("((x)).f();") //
        .to("(x).f();"). //
        convert("(x).f();") //
        .to("x.f();"). //
        convert("f((x));") //
        .to("f(x);") //
        .convert("int i = (x).y;")//
        .to("int i = x.y;");
  }
}
