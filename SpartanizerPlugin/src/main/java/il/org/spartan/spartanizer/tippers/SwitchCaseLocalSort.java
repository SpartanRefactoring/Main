package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** sorts cases of a local branch {@code switch(x) { case 2: case 1: break; }}
 * to {@code switch(x) { case 1: case 2: break; } } Tests are in
 * {@link Issue0860}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-09 */
public class SwitchCaseLocalSort extends CarefulTipper<SwitchCase>//
    implements TipperCategory.Sorting {
  @Override public Tip tip(final SwitchCase n, final ExclusionManager exclude) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInside(n));
    if (exclude != null)
      exclude.excludeAll(extract.casesOnSameBranch(az.switchStatement($.getParent()), n));
    return new Tip(description(n), n, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, copy.of($), g);
        r.replace($, copy.of(n), g);
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchCase n) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInside(n));
    final List<SwitchCase> cases = extract.casesOnSameBranch(az.switchStatement(parent(n)), n);
    if (cases.size() > switchBranch.MAX_CASES_FOR_SPARTANIZATION)
      return false;
    for (final SwitchCase ¢ : cases)
      if (¢.isDefault())
        return false;
    return $ != null//
        && !$.isDefault()//
        && !n.isDefault()//
        && (iz.intType(expression(n))//
            || (expression(n) + "").compareTo(expression($) + "") > 0)//
        && (!iz.intType(expression(n))//
            || Integer.parseInt(expression(n) + "") > Integer.parseInt(expression($) + ""));
  }

  @Override @SuppressWarnings("unused") public String description(final SwitchCase n) {
    return "sort cases with same flow control";
  }
}