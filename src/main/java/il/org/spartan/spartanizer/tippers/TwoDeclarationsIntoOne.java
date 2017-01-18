package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test case is {@link Issue1012} Issue #1012 Convert:
 *
 * <pre>
 * int a = 0;
 * int b = 1;
 * int c = 2;
 * </pre>
 *
 * to:
 *
 * <pre>
 * int a = 0, b = 1, c = 2;
 * </pre>
 *
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-13 */
public class TwoDeclarationsIntoOne extends ReplaceToNextStatement<VariableDeclarationStatement> implements TipperCategory.Abbreviation {
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!canTip(s, nextStatement))
      return null;
    final VariableDeclarationStatement sc = copy.of(s);
    fragments(az.variableDeclarationStatement(nextStatement)).forEach(¢ -> fragments(sc).add(copy.of(¢)));
    $.replace(s, sc, g);
    $.remove(nextStatement, g);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final VariableDeclarationStatement __) {
    return "Unify two variable declarations of the same type into one";
  }

  private static boolean canTip(final VariableDeclarationStatement $, final Statement nextStatement) {
    final Block parent = az.block(parent($));
    return (parent == null
        ? iz.variableDeclarationStatement(nextStatement) && (type(az.variableDeclarationStatement(nextStatement)) + "").equals(type($) + "")
            && az.variableDeclarationStatement(nextStatement).getModifiers() == $.getModifiers()
        : !lastIn(nextStatement, statements(parent)) && iz.variableDeclarationStatement(nextStatement)
            && (type(az.variableDeclarationStatement(nextStatement)) + "").equals(type($) + "")
            && az.variableDeclarationStatement(nextStatement).getModifiers() == $.getModifiers())
        && extract.annotations($).equals(extract.annotations(az.variableDeclarationStatement(nextStatement)));
  }
}
