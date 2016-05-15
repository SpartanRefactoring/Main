package il.org.spartan.refactoring.wring;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Scalpel;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode;

/**
 * Inserts to every case within a {@link SwitchStatement} all the statements it
 * would conduct when matching the case. In other words, this {@link Wring}
 * makes sure every case would end with a sequencer TODO Ori: add tests
 *
 * @author Ori Roth
 * @since 2016/05/16
 */
public class SwitchDeductStatementsForCase extends ReplaceCurrentNode<SwitchStatement> {
  @SuppressWarnings({ "javadoc", "unchecked" }) public static boolean containsCaseWithoutSequencer(SwitchStatement n) {
    boolean ic = false;
    for (final Statement s : (Iterable<Statement>) n.statements())
      if (s instanceof SwitchCase) {
        if (ic)
          return true;
        ic = true;
      } else if (Is.sequencer(n))
        ic = false;
    return false;
  }
  @SuppressWarnings("javadoc") public static void insertUntilSequencer(List<Statement> f, List<Statement> t, int i,
      Scalpel scalpel) {
    int c = i;
    while (c < f.size()) {
      t.add(scalpel.duplicate(f.get(c)));
      if (Is.sequencer(f.get(c)))
        return;
      ++c;
    }
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(SwitchStatement n) {
    if (!containsCaseWithoutSequencer(n))
      return null;
    final SwitchStatement $ = n.getAST().newSwitchStatement();
    $.setExpression(scalpel.duplicate(n.getExpression()));
    boolean ic = false;
    if (!Is.sequencer((Statement) n.statements().get(n.statements().size() - 1)))
      n.statements().add(n.getAST().newBreakStatement());
    for (final Statement s : (Iterable<Statement>) n.statements()) {
      if (s instanceof SwitchCase) {
        if (ic)
          insertUntilSequencer(n.statements(), $.statements(), n.statements().indexOf(s) + 1, scalpel);
        ic = true;
      } else if (Is.sequencer(s))
        ic = false;
      $.statements().add(scalpel.duplicate(s));
    }
    return $;
  }
  @Override String description(@SuppressWarnings("unused") SwitchStatement __) {
    return "End cases with sequencers";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.SWITCH_IF_CONVERTION; // TODO Ori: change wring group
  }
}
