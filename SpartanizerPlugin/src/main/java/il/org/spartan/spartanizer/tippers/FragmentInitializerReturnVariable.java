package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;

/** convert {@code
 * int a = 3;
 * return a;
 * } into {@code
 * return a;
 * } https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op1.html
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerReturnVariable extends $FragementAndStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -7344214948464934471L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate temporary " + ¢.getName() + " and return its value";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || haz.annotation(f) || initializer instanceof ArrayInitializer)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null)
      return null;
    final Expression returnValue = expression(s);
    if (returnValue == null || !wizard.same(n, returnValue))
      return null;
    wizard.eliminate(f, $, g);
    $.replace(s, subject.operand(initializer).toReturn(), g);
    return $;
  }
}
