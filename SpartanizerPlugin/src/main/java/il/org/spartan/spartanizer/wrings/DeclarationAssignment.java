package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
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
public final class DeclarationAssignment extends VariableDeclarationFragementAndStatement implements Kind.Collapse {
  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment $ = duplicate.of(f);
    $.setInitializer(duplicate.of(x));
    return $;
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer != null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.same(n, to(a)) || doesUseForbiddenSiblings(f, from(a)))
      return null;
    r.replace(f, makeVariableDeclarationFragement(f, from(a)), g);
    r.remove(extract.statement(a), g);
    return r;
  }
}
