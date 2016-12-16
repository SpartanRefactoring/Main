package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * switch (x) {
 * case a:
 * }
 *
 * switch(x) {
 * default: (some commands)
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * (some commands)
 * </pre>
 *
 * . Tested in {@link Issue233}
 * @author Yuval Simon
 * @since 2016-11-20 */
public final class SwitchEmpty extends CarefulTipper<SwitchStatement> implements TipperCategory.Collapse {
  @Override public Tip tip(final SwitchStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Statement> ll = step.statements(s);
        if (ll.isEmpty() || ll.size() == 1 || ll.size() == 2 && iz.breakStatement(ll.get(1))) {
          r.remove(s, g);
          if(haz.sideEffects(s.getExpression()))
            r.replace(s, wizard.ast(s.getExpression() + ";"), g);
          return;
        }
        if (iz.breakStatement(ll.get(ll.size()-1)))
          ll.remove(ll.size() - 1);
        ll.remove(0);
        ASTNode res = wizard.ast(!haz.sideEffects(s.getExpression()) ? statementsToString(ll) : s.getExpression() + ";" + statementsToString(ll));
        r.replace(s, res, g);

      }
    };
  }
  
  static String statementsToString(List<Statement> ss) {
    StringBuilder p = new StringBuilder();
    for(Statement k : ss)
      p.append((k + ""));
    return p + "";
  }

  @Override protected boolean prerequisite(final SwitchStatement s) {
    final List<SwitchCase> $ = extract.switchCases(s);
    final List<Statement> ll = step.statements(s);
    return $.isEmpty() || ll.size() == 1 || ll.size() == 2 && iz.breakStatement(ll.get(1)) || $.size() == 1 && $.get(0).isDefault();
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty switch statement or switch statement with only default case";
  }
}
