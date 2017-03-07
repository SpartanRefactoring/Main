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

/** convert {@code
 * a = 3;
 * return a;
 * } to {@code
 * return a = 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class AssignmentAndReturn extends ReplaceToNextStatement<Assignment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -1263526923784459386L;

  @Override public String description(final Assignment ¢) {
    return "Inline assignment to " + to(¢) + " into its subsequent 'return'";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.statement(parent(a));
    if (parent == null || iz.forStatement(parent))
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.same(to(a), core(expression(s))))
      return null;
    $.remove(parent, g);
    $.replace(s, subject.operand(a).toReturn(), g);
    return $;
  }
}
