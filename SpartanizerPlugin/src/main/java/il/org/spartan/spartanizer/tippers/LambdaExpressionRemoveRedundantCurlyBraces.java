package il.org.spartan.spartanizer.tippers;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
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
        final ASTNode $ = replacement(x, r, g);
        r.replace(x, $, g);
      }
    };
  }

  public static ASTNode replacement(final LambdaExpression x, final ASTRewrite r, final TextEditGroup g) {
    final List<?> stmnts = step.statements(step.body(x));
    assert stmnts.size() == 1;
    final Statement s = step.statements(step.body(x)).get(0);
    final LambdaExpression $ = x.getAST().newLambdaExpression();
    final ListRewrite lr = r.getListRewrite($, LambdaExpression.PARAMETERS_PROPERTY);
    for (final Object ¢ : x.parameters())
      lr.insertLast((ASTNode) ¢, g);
    final ASTNode replacement = az.returnStatement(s).getExpression() == null ? x.getAST().newBlock() : az.returnStatement(s).getExpression();
    r.replace(step.body($), replacement, g);
    $.setParentheses(x.hasParentheses());
    return $;
  }

  @Override public String description(final LambdaExpression ¢) {
    // TODO Auto-generated method stub
    return "remove curly braces from: " + ¢;
  }

  @Override protected boolean prerequisite(@SuppressWarnings("unused") final LambdaExpression ¢) {
    return !iz.expression(step.body(¢)) && !iz.methodInvocation(step.body(¢)) && step.body(¢).statements().size() == 1;
  }
}
