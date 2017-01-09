package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.ast.safety.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;

/**
 * 
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-09
 */
public class SwitchCaseLocalSort extends CarefulTipper<SwitchCase> implements TipperCategory.Sorting {
  @Override public Tip tip(SwitchCase n) {
    SwitchCase next = az.switchCase(extract.nextStatement(n));
    if(next == null || next.isDefault() || n.isDefault())
      return null;
    if(!iz.intType(expression(n)) && expression(n).toString().compareTo(expression(next).toString()) <= 0)
      return null;
    else if(Integer.parseInt(expression(n).toString()) <= Integer.parseInt(expression(next).toString()))
      return null;
    return new Tip(description(n), n, getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(n.getParent(), SwitchStatement.STATEMENTS_PROPERTY);
        SwitchCase tmp = copy.of(n);
        l.replace(n, copy.of(next), g);
        l.replace(next, tmp, g);
      }
    };
  }
  @SuppressWarnings("unused")
  @Override public String description(SwitchCase n) {
    return "sort cases with same flow control";
  }
}
