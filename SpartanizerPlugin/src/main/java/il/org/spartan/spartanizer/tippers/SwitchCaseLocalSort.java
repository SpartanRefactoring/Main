package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** sorts cases of a local branch
 * 
 * <pre>
 * switch(x) { case 2: case 1: break; }
 * 
 * <pre>
 * to switch(x) { case 1: case 2: break; }
 * 
 * <pre>
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-09 */
public class SwitchCaseLocalSort extends CarefulTipper<SwitchStatement> implements TipperCategory.Sorting {
  SwitchCase c1;
  SwitchCase c2;

  @Override public Tip tip(SwitchStatement $) {
    List<Statement> l = statements($);
    boolean found = false;
    for (int ¢ = 0; ¢ < l.size() - 1; ++¢)
      if (iz.switchCase(l.get(¢)) && iz.switchCase(l.get(¢ + 1))) {
        c1 = az.switchCase(l.get(¢));
        c2 = az.switchCase(l.get(¢ + 1));
        if (c1.isDefault() || c2.isDefault())
          continue;
//        Expression aaa = exspression(c1);
        if (iz.intType(expression(c1)) && Integer.parseInt((expression(c1) + "")) > Integer.parseInt(expression(c2) + "")
            || !iz.intType(expression(c1)) && (expression(c1) + "").compareTo((expression(c2) + "")) > 0) {
          found = true;
          break;
        }
      }
    return !found ? null : new Tip(description($), $, getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        r.replace(c1, copy.of(c2), g);
        r.replace(c2, copy.of(c1), g);
      }
    };
  }

  @Override @SuppressWarnings("unused") public String description(SwitchStatement __) {
    return "sort cases with same flow control";
  }
}
