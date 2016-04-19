package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Rewrite;
import il.org.spartan.refactoring.utils.Source;
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
    List<ASTNode> nl = Source.getComments(parent, r);
    nl.addAll(Source.getComments(nextStatement, r));
    nl.add(Subject.operand(a).toReturn());
    r.replace(s, r.createGroupNode(nl.toArray(new ASTNode[nl.size()])), g);
    return r;
  }
  @Override String description(final Assignment a) {
    return "Inline assignment to " + left(a) + " with its subsequent 'return'";
  }
  @Override WringGroup wringGroup() {
	return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}
