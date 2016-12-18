package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import static il.org.spartan.lisp.*;
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
public class LambdaExpressionRemoveRedundantCurlyBraces extends CarefulTipper<LambdaExpression> implements TipperCategory.ScopeReduction {
  @Override public Tip tip(final LambdaExpression x) {
    assert prerequisite(x) : fault.dump() + "\n n = " + x + fault.done();
    return new Tip(description(x), x, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(x, replacement(x, r, g), g);
      }
    };
  }

  public static ASTNode replacement(final LambdaExpression x, final ASTRewrite r, final TextEditGroup g) {
    if (onlyOne(statements(body(x))) == null)
      return null;
    final Statement s = step.statements(step.body(x)).get(0);
    final LambdaExpression $ = x.getAST().newLambdaExpression();
    for (final Object ¢ : x.parameters())
      r.getListRewrite($, LambdaExpression.PARAMETERS_PROPERTY).insertLast((ASTNode) ¢, g);
    r.replace(step.body($), iz.expressionStatement(s) ? step.expression(s)
        : expression(az.returnStatement(s)) == null ? x.getAST().newBlock() : step.expression(az.returnStatement(s)), g);
    $.setParentheses(x.hasParentheses());
    return $;
  }

  @Override public String description(final LambdaExpression ¢) {
    return "remove curly braces from " + wizard.trim(¢);
  }

  @Override protected boolean prerequisite(final LambdaExpression ¢) {
    return !iz.expression(body(¢))//
        && !iz.methodInvocation(body(¢))//
        && onlyOne(statements(body(¢))) != null//
        && iz.expressionStatement(onlyOne(statements(body(¢))))//
        || iz.returnStatement(onlyOne(statements(body(¢))));
  }
}
