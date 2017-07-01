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

/** remove redundant return from switch in void method. convert {@code void a()
 * { switch(x) { case 1: y=2; break; default: return; } } } to {@code void a() {
 * switch(x) { case 1: y=2; break; } } } Test case is {@link Issue1070}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-15 */
public class RemoveRedundantSwitchReturn extends ReplaceCurrentNode<SwitchStatement>//
    implements Category.Shortcircuit {
  private static final long serialVersionUID = -0x345B570B87D036B1L;

  @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final Block b = az.block(s.getParent());
    if (b == null || !iz.methodDeclaration(b.getParent()) || !iz.voidType(step.returnType(az.methodDeclaration(b.getParent())))
        || the.lastOf(statements(b)) != s)
      return null;
    final List<switchBranch> ret = switchBranch.intoBranches(s);
    for (final switchBranch ¢ : ret)
      if (¢.hasDefault() && ¢.statements.size() == 1 && iz.returnStatement(the.firstOf(¢.statements))) {
        ret.remove(¢);
        return switchBranch.makeSwitchStatement(ret, s.getExpression(), s.getAST());
      }
    return null;
  }
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove redundant switch case";
  }
}
