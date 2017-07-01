package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code
 * a = 3;
 * return a;
 * } to {@code
 * return a = 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class AssignmentAndReturn extends GoToNextStatement<Assignment>//
    implements Category.Collapse {
  private static final long serialVersionUID = -0x1188F30AF050947AL;

  @Override public String description(final Assignment ¢) {
    return "Inline assignment to " + to(¢) + " into its subsequent 'return'";
  }
  @Override public ASTRewrite go(final ASTRewrite ret, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.statement(parent(a));
    if (parent == null || iz.forStatement(parent))
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.eq(to(a), core(expression(s))))
      return null;
    ret.remove(parent, g);
    ret.replace(s, subject.operand(a).toReturn(), g);
    return ret;
  }
}
