package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** remove redundant continue in switch in loops. for example converts
 * {@code while(b) { switch(x) { case 1: x=2; break; default: continue; } } } to
 * {@code while(b) { switch(x) { case 1: x=2; break; } } } Test case is
 * {@link Issue1070}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-15 */
public class RemoveRedundantSwitchContinue extends ReplaceCurrentNode<SwitchStatement>//
    implements Category.Shortcircuit {
  private static final long serialVersionUID = -0x2B193D518362C674L;

  @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final Block b = az.block(s.getParent());
    if (b == null) {
      if (!iz.loop(s.getParent()))
        return null;
    } else if (!iz.loop(b.getParent()) || the.lastOf(statements(b)) != s)
      return null;
    final List<switchBranch> ret = switchBranch.intoBranches(s);
    for (final switchBranch ¢ : ret)
      if (¢.hasDefault() && ¢.statements.size() == 1 && iz.continueStatement(the.firstOf(¢.statements))) {
        ret.remove(¢);
        return switchBranch.makeSwitchStatement(ret, s.getExpression(), s.getAST());
      }
    return null;
  }
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove redundant switch case";
  }
}