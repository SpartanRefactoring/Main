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
        for (int ¢ = l.size() - 1; ¢ >= 0; --¢) {
          if (!isListContains(l, ¢, "case ") && !isListContains(l, ¢, "default"))
            break;
          l.remove(¢);
        }
        int start = -1;
        int end = -1;
        boolean combo = false;
        for (int i = l.size() - 1; i >= 0; --i)
          if (isListContains(l, i, "break") && !combo) {
            combo = true;
            end = i;
          } else if ((isListContains(l, i, "case ") || isListContains(l, i, "default")) && combo)
            start = i;
          else {
            if (combo && start >= 0) {
              for (int j = end; j >= start; --j)
                l.remove(j);
              ++i;
            }
            start = -1;
            combo = false;
          }
        if (combo)
          for (int j = end; j >= start; --j)
            l.remove(j);
        final int defaultInd = getDefaultIndex(l);
        if (defaultInd >= 0) {
          for (int ¢ = defaultInd - 1; ¢ >= 0; --¢) {
            if (!isListContains(l, ¢, "case "))
              break;
            l.remove(¢);
          }
          end = 0;
          for (int ¢ = defaultInd + 1; ¢ < l.size(); ++¢) {
            if (!isListContains(l, ¢, "case "))
              break;
            end = ¢;
          }
          for (int ¢ = end; ¢ > defaultInd; --¢)
            l.remove(¢);
        }
        if (l.size() == 1)
          l.remove(0);
        if (l.size() == 2 && isListContains(l, 0, "break")) {
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
          if (isListContains(¢, $, "default"))
            return $;
        return -1;
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchStatement s) {
    @SuppressWarnings("unchecked") final List<Statement> l = s.statements();
    for (int ¢ = 0; ¢ < l.size(); ++¢)
      if ((isListContains(l, ¢, "case ") || isListContains(l, ¢, "default"))
          && (isListContains(l, ¢ + 1, "default") || isListContains(l, ¢ + 1, "case ") || isListContains(l, ¢ + 1, "break")))
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
