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
 *   case d:
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
 * . Tested in {@link Issue880}
 * @author Yuval Simon
 * @since 2016-11-27 */
public class RemoveRedundantSwitchCases extends CarefulTipper<SwitchStatement> implements TipperCategory.Collapse {
  @Override public Tip tip(final SwitchStatement s) {
    return s == null ? null : new Tip(description(s), s, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Statement> l = step.statements(s);
        final int ind = getDefaultIndex(l);
        if (ind > 0 && iz.switchCase(l.get(ind - 1)))
          l.remove(ind - 1);
        else if (ind >= 0 && ind < l.size() - 1 && iz.switchCase(l.get(ind + 1)))
          l.remove(ind + 1);
        r.replace(s, subject.statement(into.s("switch(" + s.getExpression() + "){" + statementsToString(l) + "}")).toOneStatementOrNull(), g);
      }

      String statementsToString(final List<Statement> ss) {
        String $ = new String();
        for (final Statement p : ss)
          $ += p;
        return $;
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchStatement s) {
    final List<Statement> $ = step.statements(s);
    final int ind = getDefaultIndex($);
    return ind > 0 && iz.switchCase($.get(ind - 1)) || ind >= 0 && ind < $.size() - 1 && iz.switchCase($.get(ind + 1));
  }

  static int getDefaultIndex(final List<Statement> ¢) {
    for (int $ = 0; $ < ¢.size(); ++$)
      if (iz.switchCase(¢.get($)) && az.switchCase(¢.get($)).isDefault())
        return $;
    return -1;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove cases that use default path";
  }
}