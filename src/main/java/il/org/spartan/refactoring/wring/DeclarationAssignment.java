package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.utils.*;

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
public final class DeclarationAssignment extends Wring.VariableDeclarationFragementAndStatement implements Kind.Canonicalization {
  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression e) {
    final VariableDeclarationFragment $ = wizard.duplicate(f);
    $.setInitializer(wizard.duplicate(e));
    return $;
  }

  @Override String description(final VariableDeclarationFragment f) {
    return "Consolidate declaration of " + f.getName() + " with its subsequent initialization";
  }

  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer != null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.same(n, expose.left(a)) || doesUseForbiddenSiblings(f, expose.right(a)))
      return null;
    r.replace(f, makeVariableDeclarationFragement(f, expose.right(a)), g);
    r.remove(extract.statement(a), g);
    return r;
  }
}
