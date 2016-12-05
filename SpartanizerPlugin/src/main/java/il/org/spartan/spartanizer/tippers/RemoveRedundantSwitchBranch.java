package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
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
 * @author Yuval Simon
 * @since 2016-11-26 */
public class RemoveRedundantSwitchBranch extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  @SuppressWarnings("boxing") @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    @SuppressWarnings("unchecked") List<Statement> ss = s.statements();
    List<Integer> ll = new ArrayList<>();
    getNotContainedCasesIndexes(ss, ll);
    StringBuilder r = new StringBuilder("switch(" + s.getExpression() + "){");
    boolean isChanged = false;
    for (int i = 0; i < ll.size(); ++i) {
      r.append(ss.get(ll.get(i)) + "");
      if (i < ll.size() - 1 && ll.get(i + 1) == ll.get(i) + 1)
        continue;
      for (int j = i + 1; j < ll.size(); ++j)
        if (sameCommands(ss, ll.get(i) + 1, ll.get(j) + 1)) {
          r.append(ss.get(ll.get(j)));
          ll.remove(j);
          --j;
          isChanged = true;
        }
      for (int k = ll.get(i) + 1; k < ss.size(); ++k) {
        r.append(ss.get(k));
        if (ss.get(k).getNodeType() == ASTNode.BREAK_STATEMENT)
          break;
      }
    }
    r.append("}");
    return !isChanged ? null : subject.statement(into.s((r + ""))).toOneStatementOrNull();
  }

  private static void getNotContainedCasesIndexes(List<Statement> ss, List<Integer> ll) {
    boolean startFromBreak = true;
    for (int ¢ = 0; ¢ < ss.size(); ++¢)
      if (startFromBreak && ss.get(¢).getNodeType() == ASTNode.SWITCH_CASE)
        ll.add(Integer.valueOf(¢));
      else
        startFromBreak = ss.get(¢).getNodeType() == ASTNode.BREAK_STATEMENT;
  }

  private static boolean sameCommands(List<Statement> ss, int t1, int t2) {
    int p1 = t1 < t2 ? t1 : t2;
    int p2 = t2 > t1 ? t2 : t1;
    for (; p1 < ss.size() && ss.get(p1).getNodeType() == ASTNode.SWITCH_CASE;)
      ++p1;
    for (; p2 < ss.size() && ss.get(p2).getNodeType() == ASTNode.SWITCH_CASE;)
      ++p2;
    if (p1 == ss.size())
      return false;
    for (int ¢ = 0; ¢ < ss.size(); ++¢) {
      if (p2 + ¢ == ss.size())
        return ss.get(p1 + ¢).getNodeType() == ASTNode.BREAK_STATEMENT;
      if (ss.get(p1 + ¢).getNodeType() == ASTNode.BREAK_STATEMENT && ss.get(p2 + ¢).getNodeType() == ASTNode.BREAK_STATEMENT)
        return true;
      if (!(ss.get(p1 + ¢) + "").equals((ss.get(p2 + ¢) + "")))
        return false;
    }
    return true;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Merging cases with identical code";
  }
}
