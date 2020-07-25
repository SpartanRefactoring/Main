package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;

import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code
 * int a = 3;
 * return a;
 * } into {@code
 * return a;
 * } https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op1.html
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedStatementReturnVariable extends $FragmentAndStatement//
    implements Category.Inlining {
  private static final long serialVersionUID = -0x65EBE495955DBE47L;

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
    if (returnValue == null || !wizard.eq(n, returnValue))
      return null;
    remove.deadFragment(f, $, g);
    $.replace(s, subject.operand(initializer).toReturn(), g);
    return $;
  }
}
