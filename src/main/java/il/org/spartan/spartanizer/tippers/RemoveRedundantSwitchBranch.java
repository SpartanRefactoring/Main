package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removing redundant case branches in switch statement such as
 *
 * <pre>
* switch(x) {
* case a: x=1; break;
* case b: x=2; break;
* default: x=1; break;
 * </pre>
 *
 * into
 *
 * <pre>
* switch(x) {
* case a: default: x=1; break;
* case b: x=2; break;
 * </pre>
 *
 * Tested in {@link Issue858}
 * @author Yuval Simon
 * @since 2016-11-26 */
public class RemoveRedundantSwitchBranch extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  @Override @SuppressWarnings("boxing") public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final List<Statement> ss = step.statements(s);
    final List<Integer> ll = new ArrayList<>();
    getNotContainedCasesIndexes(ss, ll);
    final StringBuilder $ = new StringBuilder("switch(" + s.getExpression() + "){");
    boolean isChanged = false;
    for (int i = 0; i < ll.size(); ++i) {
      $.append(ss.get(ll.get(i))).append("");
      if (i < ll.size() - 1 && ll.get(i + 1) == ll.get(i) + 1)
        continue;
      for (int j = i + 1; j < ll.size(); ++j)
        if (sameCommands(ss, ll.get(i) + 1, ll.get(j) + 1)) {
          $.append(ss.get(ll.get(j)));
          ll.remove(j);
          --j;
          isChanged = true;
        }
      for (int k = ll.get(i) + 1; k < ss.size(); ++k) {
        $.append(ss.get(k));
        if (iz.breakStatement(ss.get(k)))
          break;
      }
    }
    $.append("}");
    return !isChanged ? null : subject.statement(into.s($ + "")).toOneStatementOrNull();
  }

  private static void getNotContainedCasesIndexes(final List<Statement> ss, final List<Integer> ll) {
    boolean startFromBreak = true;
    for (int ¢ = 0; ¢ < ss.size(); ++¢)
      if (startFromBreak && iz.switchCase(ss.get(¢)))
        ll.add(Integer.valueOf(¢));
      else
        startFromBreak = iz.breakStatement(ss.get(¢));
  }

  @SuppressWarnings("boxing") private static boolean sameCommands(final List<Statement> ss, final int t1, final int t2) {
    int $ = t1 < t2 ? t1 : t2;
    int p2 = t2 > t1 ? t2 : t1;
    for (; $ < ss.size() && iz.switchCase(ss.get($));)
      ++$;
    for (; p2 < ss.size() && iz.switchCase(ss.get(p2));)
      ++p2;
    if ($ == ss.size())
      return false;
    for (final Integer ¢ : range.from(0).to(ss.size())) {
      if (p2 + ¢ == ss.size())
        return iz.breakStatement(ss.get($ + ¢));
      if (iz.breakStatement(ss.get($ + ¢)) && iz.breakStatement(ss.get(p2 + ¢)))
        return true;
      if (!(ss.get($ + ¢) + "").equals(ss.get(p2 + ¢) + ""))
        return false;
    }
    return true;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Merging cases with identical code";
  }
}
