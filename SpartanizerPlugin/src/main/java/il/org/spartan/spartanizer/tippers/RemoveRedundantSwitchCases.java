package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
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
 *   default:
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
    return s == null ? null : new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        @SuppressWarnings("unchecked") final List<Statement> l = s.statements();
        final int ind = getDefaultIndex(l);
        boolean hadDefault = false;
        if (ind >= 0) {
          hadDefault = true;
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
          if ((l.get(¢).getNodeType() == ASTNode.SWITCH_CASE || l.get(¢).getNodeType() == ASTNode.BREAK_STATEMENT)
              && l.get(¢ + 1).getNodeType() == ASTNode.BREAK_STATEMENT)
            l.remove(¢);
        if (l.size() == 1)
          l.remove(0);
        if (l.size() == 2 && l.get(0).getNodeType() == ASTNode.SWITCH_CASE && l.get(1).getNodeType() == ASTNode.BREAK_STATEMENT) {
          l.remove(1);
          l.remove(0);
        }
        if (!l.isEmpty() && l.get(l.size() - 1).getNodeType() == ASTNode.BREAK_STATEMENT)
          l.remove(l.size() - 1);
        final String tail = l.isEmpty() || !hadDefault || getDefaultIndex(l) >= 0 ? ""
            : (l.get(l.size() - 1).getNodeType() == ASTNode.RETURN_STATEMENT ? "" : "break; ") + "default:";
        r.replace(s, subject.statement(into.s("switch(" + s.getExpression() + "){" + statementsToString(l) + tail + "}")).toOneStatementOrNull(), g);
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

  @Override @SuppressWarnings("boxing") protected boolean prerequisite(final SwitchStatement s) {
    @SuppressWarnings("unchecked") final List<Statement> l = s.statements();
    if (!l.isEmpty() && l.get(l.size() - 1).getNodeType() == ASTNode.SWITCH_CASE && !az.switchCase(l.get(l.size() - 1)).isDefault())
      return true;
    for (final Integer k : range.from(0).to(l.size() - 1))
      if (l.get(k).getNodeType() == ASTNode.SWITCH_CASE && l.get(k + 1).getNodeType() == ASTNode.BREAK_STATEMENT
          || l.get(k).getNodeType() == ASTNode.SWITCH_CASE && l.get(k + 1).getNodeType() == ASTNode.SWITCH_CASE
              && (az.switchCase(l.get(k)).isDefault() || az.switchCase(l.get(k)).isDefault()))
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