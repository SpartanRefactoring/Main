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
        
        int ind = getDefaultIndex(l);
        if (ind >= 0) {
          int last = ind;
          for (int ¢ = ind + 1; ¢ < l.size(); ++¢) {
            if (!isListContains(l, ¢, "case "))
              break;
            last = ¢;
          }
          for (int ¢ = last; ¢ > ind; --¢)
            l.remove(¢);
          for (int ¢ = ind - 1; ¢ >= 0; --¢) {
            if (!isListContains(l, ¢, "case "))
              break;
            l.remove(¢);
          }
        }
        
        for(int ¢ = l.size()-1; ¢>=0; --¢) {
          if (!isListContains(l, ¢, "case ") && !isListContains(l, ¢, "default"))
            break;
          l.remove(¢);
        }
        
        for(int ¢=l.size()-2; ¢>=0; --¢)
          if((isListContains(l, ¢, "case ") || isListContains(l, ¢, "default") || isListContains(l, ¢, "break")) && isListContains(l, ¢+1, "break"))
            l.remove(¢);
        
        if(l.size()==1)
          l.remove(0);
        
        if(l.size() == 2 && (isListContains(l, 0, "case ") || isListContains(l, 0, "default")) && isListContains(l, 1, "break")) {
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
    if(!l.isEmpty() && (isListContains(l, l.size() - 1, "case ") || isListContains(l, l.size() - 1, "default")))
      return true;
    for (int ¢ = 0; ¢ < l.size()-1; ++¢)
      if ((isListContains(l, ¢, "case ") || isListContains(l, ¢, "default")) && isListContains(l, ¢ + 1, "break")
          || (isListContains(l, ¢, "case ") || isListContains(l, ¢, "default"))
              && (isListContains(l, ¢ + 1, "case ") || isListContains(l, ¢ + 1, "default")))
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
