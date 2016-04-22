package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.Funcs.asReturnStatement;
import static il.org.spartan.refactoring.utils.Funcs.asStatement;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.same;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>a = 3;return a;</code> to
 * <code>return a = 3;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
public class AssignmentAndReturn extends Wring.ReplaceToNextStatement<Assignment> {
  @Override ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = asStatement(a.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = asReturnStatement(nextStatement);
    if (s == null || !same(left(a), core(s.getExpression())))
      return null;
    r.remove(parent, g);
    r.replace(s, Subject.operand(a).toReturn(), g);
    return r;
  }
  @Override String description(final Assignment a) {
    return "Inline assignment to " + left(a) + " with its subsequent 'return'";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}
