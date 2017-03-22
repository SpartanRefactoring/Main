package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** remove redundant return from switch in void method. convert {@code void a()
 * { switch(x) { case 1: y=2; break; default: return; } } } to {@code void a() {
 * switch(x) { case 1: y=2; break; } } } Test case is {@link Issue1070}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-15 */
public class RemoveRedundantSwitchReturn extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Shortcircuit {
  private static final long serialVersionUID = -3772704819887748785L;

  @Nullable @Override public ASTNode replacement(@Nullable final SwitchStatement s) {
    if (s == null)
      return null;
    @Nullable final Block b = az.block(s.getParent());
    if (b == null || !iz.methodDeclaration(b.getParent()) || !iz.voidType(step.returnType(az.methodDeclaration(b.getParent())))
        || last(statements(b)) != s)
      return null;
    @NotNull final List<switchBranch> $ = switchBranch.intoBranches(s);
    for (@NotNull final switchBranch ¢ : $)
      if (¢.hasDefault() && ¢.statements.size() == 1 && iz.returnStatement(first(¢.statements))) {
        $.remove(¢);
        return switchBranch.makeSwitchStatement($, s.getExpression(), s.getAST());
      }
    return null;
  }

  @NotNull @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove redundant switch case";
  }
}
