package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
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
 * Tested in {@link Issue880}
 * @author Yuval Simon
 * @since 2016-11-27 */
public class RemoveRedundantSwitchCases extends CarefulTipper<SwitchStatement> implements TipperCategory.Collapse {
  @Override public Tip tip(final SwitchStatement s) {
    return s == null ? null : new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Statement> l = step.statements(s);
        final int ind = getDefaultIndex(l);
        boolean hadDefault = false;
        if (ind >= 0) {
          hadDefault = true;
          int last = ind;
          for (int ¢ = ind + 1; ¢ < l.size(); ++¢) {
            if (!iz.switchCase(l.get(¢)))
              break;
            last = ¢;
          }
          for (int ¢ = last; ¢ > ind; --¢)
            l.remove(¢);
          for (int ¢ = ind - 1; ¢ >= 0; --¢) {
            if (!iz.switchCase(l.get(¢)))
              break;
            l.remove(¢);
          }
        }
        for (int ¢ = l.size() - 1; ¢ >= 0; --¢) {
          if (!iz.switchCase(l.get(¢)))
            break;
          l.remove(¢);
        }
        for (int ¢ = l.size() - 2; ¢ >= 0; --¢)
          if ((iz.switchCase(l.get(¢)) || iz.breakStatement(l.get(¢)))
              && iz.breakStatement(l.get(¢ + 1)))
            l.remove(¢);
        if (l.size() == 1)
          l.remove(0);
        if (l.size() == 2 && iz.switchCase(l.get(0)) && iz.breakStatement(l.get(1))) {
          l.remove(1);
          l.remove(0);
        }
        if (!l.isEmpty() && iz.breakStatement(l.get(l.size() - 1)))
          l.remove(l.size() - 1);
        final String tail = l.isEmpty() || !hadDefault || getDefaultIndex(l) >= 0 ? ""
            : (iz.returnStatement(l.get(l.size() - 1)) ? "" : "break; ") + "default:";
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
          if (iz.switchCase(¢.get($)) && az.switchCase(¢.get($)).isDefault())
            return $;
        return -1;
      }
    };
  }

  @Override @SuppressWarnings("boxing") protected boolean prerequisite(final SwitchStatement s) {
    final List<Statement> l = step.statements(s);
    if (!l.isEmpty() && iz.switchCase(l.get(l.size() - 1)) && !az.switchCase(l.get(l.size() - 1)).isDefault())
      return true;
    for (final Integer k : range.from(0).to(l.size() - 1))
      if (iz.switchCase(l.get(k)) && iz.breakStatement(l.get(k + 1))
          || iz.switchCase(l.get(k)) && iz.switchCase(l.get(k + 1))
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