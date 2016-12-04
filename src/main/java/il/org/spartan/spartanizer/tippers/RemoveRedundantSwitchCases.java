package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * switch (x) {
 *   case a:
 *   case b:
 *     x = 5;
 *     break;
 *   case c:
 *   default:
 *     break;
 * }
 * switch (x) {
 *   case a:
 *   case b:
 *     x = 5;
 *     break;
 * }
 * </pre>
 *
 * into
 *
 * <pre>
* (some commands)
 * </pre>
 *
 * .
 * @author Yuval Simon
 * @since 2016-11-27 */
public class RemoveRedundantSwitchCases extends CarefulTipper<SwitchStatement> implements TipperCategory.Collapse {
  @Override public Tip tip(final SwitchStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        @SuppressWarnings("unchecked") final List<Statement> l = s.statements();
        final int ind = getDefaultIndex(l);
        if (ind >= 0) {
          int last = ind;
          for (int ¢ = ind + 1; ¢ < l.size(); ++¢) {
            if (l.get(¢).getNodeType() != ASTNode.SWITCH_CASE)
              break;
            last = ¢;
          }
          for (int ¢ = last; ¢ > ind; --¢)
            l.remove(¢);
          for (int ¢ = ind - 1; ¢ >= 0; --¢) {
            if (l.get(¢).getNodeType() != ASTNode.SWITCH_CASE)
              break;
            l.remove(¢);
          }
        }
        for (int ¢ = l.size() - 1; ¢ >= 0; --¢) {
          if (l.get(¢).getNodeType() != ASTNode.SWITCH_CASE)
            break;
          l.remove(¢);
        }
        for (int ¢ = l.size() - 2; ¢ >= 0; --¢)
          if (((l.get(¢).getNodeType() == ASTNode.SWITCH_CASE) || (l.get(¢).getNodeType() == ASTNode.BREAK_STATEMENT))
              && (l.get(¢+1).getNodeType() == ASTNode.BREAK_STATEMENT))
            l.remove(¢);
        if (l.size() == 1)
          l.remove(0);
        if (l.size() == 2 && (l.get(0).getNodeType() == ASTNode.SWITCH_CASE) && (l.get(1).getNodeType() == ASTNode.BREAK_STATEMENT)) {
          l.remove(1);
          l.remove(0);
        }
        r.replace(s, subject.statement(into.s("switch(" + s.getExpression() + "){" + statementsToString(l) + "}")).toOneStatementOrNull(), g);
      }

      String statementsToString(final List<Statement> ss) {
        String $ = new String();
        for (final Statement p : ss)
          $ += p;
        return $;
      }

      int getDefaultIndex(final List<Statement> ¢) {
        for (int $ = 0; $ < ¢.size(); ++$)
          if (¢.get($).getNodeType() == ASTNode.SWITCH_CASE && isListContains(¢, $, "default"))
            return $;
        return -1;
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchStatement s) {
    @SuppressWarnings("unchecked") final List<Statement> l = s.statements();
    if (!l.isEmpty() && (l.get(l.size() - 1).getNodeType() == ASTNode.SWITCH_CASE))
      return true;
    for (int k = 0; k < l.size() - 1; ++k)
      if (l.get(k).getNodeType() == ASTNode.SWITCH_CASE && l.get(k + 1).getNodeType() == ASTNode.BREAK_STATEMENT
          || l.get(k).getNodeType() == ASTNode.SWITCH_CASE && (l.get(k + 1).getNodeType() == ASTNode.SWITCH_CASE)
              && (isListContains(l, k, "default") || isListContains(l, k + 1, "default")))
        return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty cases or cases combined with default case";
  }

  static boolean isListContains(final List<Statement> ss, final int i, final String q) {
    return (ss.get(i) + "").contains(q);
  }
}