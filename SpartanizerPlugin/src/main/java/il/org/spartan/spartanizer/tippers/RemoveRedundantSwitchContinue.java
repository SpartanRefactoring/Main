package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** remove redundant continue in switch in loops. for example converts
 * {@code while(b) { switch(x) { case 1: x=2; break; default: continue; } } } to
 * {@code while(b) { switch(x) { case 1: x=2; break; } } } Test case is
 * {@link Issue1070}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-15 */
public class RemoveRedundantSwitchContinue extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Shortcircuit {
  @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final Block b = az.block(s.getParent());
    if (b == null) {
      if (!iz.loop(s.getParent()))
        return null;
    } else if (!iz.loop(b.getParent()) || lisp.last(step.statements(b)) != s)
      return null;
    final List<switchBranch> $ = switchBranch.intoBranches(s);
    for (final switchBranch ¢ : $)
      if (¢.hasDefault() && ¢.statements.size() == 1 && iz.continueStatement(lisp.first(¢.statements))) {
        $.remove(¢);
        return switchBranch.makeSwitchStatement($, s.getExpression(), s.getAST());
      }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove redundant switch case";
  }
}