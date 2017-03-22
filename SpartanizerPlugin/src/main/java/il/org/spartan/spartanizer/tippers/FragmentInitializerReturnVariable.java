package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;

/** convert {@code
 * int a = 3;
 * return a;
 * } into {@code
 * return a;
 * } https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op1.html
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerReturnVariable extends $FragementInitializerStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -7344214948464934471L;

  @NotNull @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Eliminate temporary " + ¢.getName() + " and return its value";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    @Nullable final ReturnStatement s = az.returnStatement(nextStatement());
    if (s == null)
      return null;
    @NotNull final Expression returnValue = expression(s);
    if (returnValue == null || !wizard.same(name(), returnValue))
      return null;
    eliminateFragment($, g);
    $.replace(s, subject.operand(initializer()).toReturn(), g);
    return $;
  }
}
