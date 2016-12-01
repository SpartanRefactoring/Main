package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts
 *
 * <pre>
 * void f() {
 *  .
 *  .
 *  .
 *   return;
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * void f() {
 * .
 * .
 * .
 * }
 * </pre>
 *
 * @author Dan Abramovich
 * @since 28-11-2016 */
public class RedundentReturnStatementInVoidTypeMethod extends ReplaceCurrentNode<MethodDeclaration> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final MethodDeclaration x) {
    final List<Statement> statements = step.statements(step.body(x));
    if (!"void".equals(x.getReturnType2() + ""))
      return null;
    final ReturnStatement r = az.returnStatement(statements.get(statements.size() - 1));
    if (r == null || r.getExpression() != null)
      return null;
    final MethodDeclaration $ = duplicate.of(x);
    step.statements(step.body($)).remove(statements.size() - 1);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration ¢) {
    return "Remove redundent return in void type method";
  }
}
