package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;

/** convert {@code
 * int a = 3;
 * return a;
 * } into {@code
 * return a;
 * } https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op1.html
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerReturnVariable extends LocalVariableInitializedStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -7344214948464934471L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate temporary " + ¢.getName() + " and return its value";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    if (initializer == null || haz.annotation(fragment) || initializer instanceof ArrayInitializer)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null)
      return null;
    final Expression returnValue = expression(s);
    if (returnValue == null || !wizard.same(name, returnValue))
      return null;
    action.removeDeadFragment(fragment, $, g);
    $.replace(s, subject.operand(initializer).toReturn(), g);
    return $;
  }
}
