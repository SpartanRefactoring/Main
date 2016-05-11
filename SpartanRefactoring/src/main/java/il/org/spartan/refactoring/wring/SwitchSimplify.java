package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode;

/**
 * A wring to remove ineffective cases from a switch statement. TODO Ori: add
 * tests
 *
 * @author Ori Roth
 * @since 2016/05/11
 */
public class SwitchSimplify extends ReplaceCurrentNode<SwitchStatement> {
  @SuppressWarnings("unchecked") @Override ASTNode replacement(SwitchStatement n) {
    final SwitchStatement s = n.getAST().newSwitchStatement();
    s.setExpression(comments.duplicateWithComments(n.getExpression()));
    for (final Statement c : (Iterable<Statement>) n.statements()) {
      if (c instanceof SwitchCase) {
        final int i = n.statements().indexOf(c);
        if (i == n.statements().size() - 1 || n.statements().get(i + 1) instanceof SwitchCase)
          continue;
        if (i == n.statements().size() - 2 && n.statements().get(i + 1) instanceof BreakStatement)
          break;
      }
      s.statements().add(comments.duplicateWithComments(c));
    }
    return n.statements().size() == s.statements().size() ? null : s;
  }
  @Override String description(@SuppressWarnings("unused") SwitchStatement __) {
    return "Simplify switch statement";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}
