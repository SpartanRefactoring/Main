package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * a = 3;
 * return a;
 * </pre>
 *
 * to
 *
 * <pre>
 * return a = 3;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class AssignmentAndReturn extends ReplaceToNextStatement<Assignment> implements TipperCategory.Collapse {
  @Override public String description(final Assignment ¢) {
    return "Inline assignment to " + to(¢) + " with its subsequent 'return'";
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.statement(a.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.same(to(a), core(s.getExpression())))
      return null;
    r.remove(parent, g);
    r.replace(s, subject.operand(a).toReturn(), g);
    return r;
  }
}
