package il.org.spartan.refactoring.wring;

import java.util.Iterator;

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
  /**
   * @param sl switch body statements
   * @return true iff the statements contains a default statement
   */
  public static boolean containsDefault(Iterable<Statement> sl) {
    for (final Statement s : sl)
      if (s instanceof SwitchCase && ((SwitchCase) s).isDefault())
        return true;
    return false;
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(SwitchStatement n) {
    final SwitchStatement s = n.getAST().newSwitchStatement();
    s.setExpression(scalpel.duplicate(n.getExpression()));
    final boolean d = containsDefault(n.statements());
    for (final Iterator<Statement> it = n.statements().iterator(); it.hasNext();) {
      final Statement c = it.next();
      if (c instanceof SwitchCase) {
        final int i = n.statements().indexOf(c);
        if (i == n.statements().size() - 1)
          continue;
        if (!d && n.statements().get(i + 1) instanceof BreakStatement) {
          it.next();
          continue;
        }
        if (i == n.statements().size() - 2 && n.statements().get(i + 1) instanceof BreakStatement)
          break;
      }
      s.statements().add(scalpel.duplicate(c));
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
