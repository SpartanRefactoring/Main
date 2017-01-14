package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
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
  // TODO: Tomer Dragucki  use class step if necessary and remove
  // @SuppressWarnings("unchecked") --yg
 
  @Override @SuppressWarnings("unchecked") protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationStatement s, final Statement nextStatement,
      final TextEditGroup g) {
    if (!canTip(s, nextStatement))
      return null;
    final VariableDeclarationStatement ns = (VariableDeclarationStatement) nextStatement, sc = copy.of(s);
    for (final VariableDeclarationFragment ¢ : (List<VariableDeclarationFragment>) ns.fragments())
      sc.fragments().add(copy.of(¢));
    $.replace(s, sc, g);
    $.remove(nextStatement, g);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final VariableDeclarationStatement __) {
    return "Unify two variable declarations of the same type into one";
  }

  private static boolean canTip(final VariableDeclarationStatement $, final Statement nextStatement) {
    final Block parent = az.block($.getParent());
    return parent == null
        ? iz.variableDeclarationStatement(nextStatement) && (((VariableDeclarationStatement) nextStatement).getType() + "").equals($.getType() + "")
        : !lastIn(nextStatement, statements(parent)) && iz.variableDeclarationStatement(nextStatement)
            && (((VariableDeclarationStatement) nextStatement).getType() + "").equals($.getType() + "");
  }
}
