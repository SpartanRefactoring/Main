package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

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
  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment $ = duplicate.of(f);
    $.setInitializer(duplicate.of(x));
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
    if (a == null || !wizard.same(n, step.left(a)) || doesUseForbiddenSiblings(f, step.right(a)))
      return null;
    r.replace(f, makeVariableDeclarationFragement(f, step.right(a)), g);
    r.remove(extract.statement(a), g);
    return r;
  }
}
