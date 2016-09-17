package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
 *
 * <pre>
 * a = 3;
 * b = 3;
 * </pre>
 *
 * to
 *
 * <pre>
 * a = b = 3
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class AssignmentAndAssignment extends ReplaceToNextStatement<Assignment> implements Kind.DistributiveRefactoring {
  static Expression extractRight(final Assignment a) {
    final Expression $ = extract.core(from(a));
    return !($ instanceof Assignment) || ((Assignment) $).getOperator() != ASSIGN ? $ : extractRight((Assignment) $);
  }

  static Expression getRight(final Assignment ¢) {
    return ¢.getOperator() != ASSIGN ? null : extractRight(¢);
  }

  @Override public String description(final Assignment ¢) {
    return "Consolidate assignment to " + to(¢) + " with subsequent similar assignment";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final ASTNode parent = a.getParent();
    if (!(parent instanceof Statement))
      return null;
    final Expression right = getRight(a);
    if (right == null || right.getNodeType() == NULL_LITERAL)
      return null;
    final Assignment a1 = extract.assignment(nextStatement);
    if (a1 == null)
      return null;
    final Expression right1 = getRight(a1);
    if (right1 == null || !wizard.same(right, right1) || !sideEffects.deterministic(right))
      return null;
    r.remove(parent, g);
    r.replace(right1, duplicate.of(a), g);
    return r;
  }
}
