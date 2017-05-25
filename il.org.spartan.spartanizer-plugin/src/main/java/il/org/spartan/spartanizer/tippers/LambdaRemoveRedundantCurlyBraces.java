package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Remove curly braces from a lambda expression if and only if toList its body
 * has only one statement.
 * @author Oren Afek
 * @since 2016-11-17 */
public class LambdaRemoveRedundantCurlyBraces extends CarefulTipper<LambdaExpression>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -0x7F8F90EF9ECDB09L;

  @Override public Tip tip(final LambdaExpression x) {
    assert prerequisite(x) : fault.dump() + "\n n = " + x + fault.done();
    return new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(x, replacement(x, r, g), g);
      }
    };
  }
  public static ASTNode replacement(final LambdaExpression x, final ASTRewrite r, final TextEditGroup g) {
    if (the.onlyOneOf(statements(body(x))) == null)
      return null;
    final Statement s = the.firstOf(statements(x));
    final LambdaExpression $ = x.getAST().newLambdaExpression();
    parameters(x).forEach(λ -> r.getListRewrite($, LambdaExpression.PARAMETERS_PROPERTY).insertLast(λ, g));
    r.replace(body($), iz.expressionStatement(s) ? expression(s)
        : expression(az.returnStatement(s)) == null ? x.getAST().newBlock() : expression(az.returnStatement(s)), g);
    $.setParentheses(x.hasParentheses());
    return $;
  }
  @Override public String description(final LambdaExpression ¢) {
    return "Remove curly braces from " + Trivia.gist(¢);
  }
  @Override protected boolean prerequisite(final LambdaExpression ¢) {
    return !iz.expression(body(¢))//
        && !iz.methodInvocation(body(¢))//
        && the.onlyOneOf(statements(¢)) != null//
        && iz.expressionStatement(the.onlyOneOf(statements(¢)))//
        || iz.returnStatement(the.onlyOneOf(statements(¢)));
  }
}
