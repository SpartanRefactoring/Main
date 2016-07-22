package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * Inserts to every case within a {@link SwitchStatement} all the statements it
 * would conduct when matching the case. In other words, this {@link Wring}
 * makes sure every case would end with a sequencer TODO Ori: add tests
 *
 * @author Ori Roth
 * @since 2016/05/16
 */
@Deprecated public class SwitchDeductStatementsForCase extends ReplaceCurrentNode<SwitchStatement> implements
Kind.SWITCH_IF_CONVERTION {
  @SuppressWarnings({ "javadoc", "unchecked" }) public static boolean containsCaseWithoutSequencer(final SwitchStatement n) {
    boolean ic = false;
    for (final Statement s : (Iterable<Statement>) n.statements())
      if (!(s instanceof SwitchCase)) {
        if (Is.sequencer(s))
          ic = false;
      } else {
        if (ic)
          return true;
        ic = true;
      }
    return false;
  }
  @SuppressWarnings("javadoc") public static void insertUntilSequencer(final List<Statement> f, final List<Statement> ss,
      final int i, final Scalpel s) {
    int c = i;
    while (c < f.size()) {
      ss.add(s.duplicate(f.get(c)));
      if (Is.sequencer(f.get(c)))
        return;
      ++c;
    }
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(final SwitchStatement n) {
    if (!containsCaseWithoutSequencer(n))
      return null;
    final SwitchStatement $ = n.getAST().newSwitchStatement();
    $.setExpression(scalpel.duplicate(n.getExpression()));
    boolean ic = false;
    if (!Is.sequencer((Statement) n.statements().get(n.statements().size() - 1)))
      n.statements().add(n.getAST().newBreakStatement());
    for (final Statement s : (Iterable<Statement>) n.statements()) {
      if (!(s instanceof SwitchCase)) {
        if (Is.sequencer(s))
          ic = false;
      } else {
        if (ic)
          insertUntilSequencer(n.statements(), $.statements(), n.statements().indexOf(s) + 1, scalpel);
        ic = true;
      }
      $.statements().add(scalpel.duplicate(s));
    }
    return $;
  }
  @Override String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "End cases with sequencers";
  }
}
