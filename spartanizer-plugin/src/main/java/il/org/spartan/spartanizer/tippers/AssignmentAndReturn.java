package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.to;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
  @Override public ASTRewrite go(final ASTRewrite $, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.statement(parent(a));
    if (parent == null || iz.forStatement(parent))
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.eq(to(a), core(expression(s))))
      return null;
    $.remove(parent, g);
    $.replace(s, subject.operand(a).toReturn(), g);
    return $;
  }
}
