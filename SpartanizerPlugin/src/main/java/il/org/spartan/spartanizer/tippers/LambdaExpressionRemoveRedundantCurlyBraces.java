package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Remove curly braces from a lambda expression if and only if </br>
 * its body has only one statement.
 * @author Oren Afek
 * @since 2016-11-17 */
public class LambdaExpressionRemoveRedundantCurlyBraces extends CarefulTipper<LambdaExpression>//
    implements TipperCategory.SyntacticBaggage {
  @Override public Tip tip(final LambdaExpression x) {
    assert prerequisite(x) : fault.dump() + "\n n = " + x + fault.done();
    return new Tip(description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(x, replacement(x, r, g), g);
      }
    };
  }

  public static ASTNode replacement(final LambdaExpression x, final ASTRewrite r, final TextEditGroup g) {
    if (onlyOne(statements(body(x))) == null)
      return null;
    final Statement s = first(statements(x));
    final LambdaExpression $ = x.getAST().newLambdaExpression();
    parameters(x).forEach(¢ -> r.getListRewrite($, LambdaExpression.PARAMETERS_PROPERTY).insertLast(¢, g));
    r.replace(body($), iz.expressionStatement(s) ? expression(s)
        : expression(az.returnStatement(s)) == null ? x.getAST().newBlock() : expression(az.returnStatement(s)), g);
    $.setParentheses(x.hasParentheses());
    return $;
  }

  @Override public String description(final LambdaExpression ¢) {
    return "remove curly braces from " + trivia.gist(¢);
  }

  @Override protected boolean prerequisite(final LambdaExpression ¢) {
    return !iz.expression(body(¢))//
        && !iz.methodInvocation(body(¢))//
        && onlyOne(statements(¢)) != null//
        && iz.expressionStatement(onlyOne(statements(¢)))//
        || iz.returnStatement(onlyOne(statements(¢)));
  }
}
