package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A {@link Wring} to convert <code>a = 3;return a;</code> to
 * <code>return a = 3;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
public class AssignmentAndReturn extends Wring.ReplaceToNextStatement<Assignment> implements Kind.ConsolidateStatements {
  @SuppressWarnings("unused") @Override ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement,
      final TextEditGroup g) {
    final Statement parent = asStatement(a.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = asReturnStatement(nextStatement);
    if (s == null || !same(left(a), core(s.getExpression())))
      return null;
    scalpel.operate(nextStatement, parent).replaceWith(Subject.operand(a).toReturn());
    return r;
  }
  @Override String description(final Assignment a) {
    return "Inline assignment to " + left(a) + " with its subsequent 'return'";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}
