package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
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
public class ReturnStatementRedundantInVoidMethod extends ReplaceCurrentNode<MethodDeclaration>
    implements TipperCategory.Collapse, MethodPatternUtilitiesTrait {
  @Override public ASTNode replacement(final MethodDeclaration x) {
    if (empty(x) || returnTypeNotVoid(x) || !iz.returnStatement(lastStatement(x)) || expression(lastStatement(x)) != null)
      return null;
    final MethodDeclaration $ = duplicate.of(x);
    statements($).remove(last(statements($)));
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration ¢) {
    return "Remove redundent return in void type method";
  }
}
