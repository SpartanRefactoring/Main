package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
 *
 * <pre>
 * int a;
 * a = 3;
 * </pre>
 *
 * into
 *
 * <pre>
 * int a = 3;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class DeclarationAssignment extends Wring.VariableDeclarationFragementAndStatement {
  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer != null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !same(n, left(a)) || doesUseForbiddenSiblings(f, right(a)))
      return null;
    r.replace(f, makeVariableDeclarationFragement(f, right(a)), g);
    r.remove(extract.statement(a), g);
    return r;
  }
  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression e) {
    final VariableDeclarationFragment $ = duplicate(f);
    $.setInitializer(duplicate(e));
    return $;
  }
  @Override String description(final VariableDeclarationFragment f) {
    return "Consolidate declaration of " + f.getName() + " with its subsequent initialization";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}