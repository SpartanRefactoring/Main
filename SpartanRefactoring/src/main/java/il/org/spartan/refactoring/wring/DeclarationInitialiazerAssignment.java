package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.wring.Wrings.size;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.wring.LocalInliner.LocalInlineWithValue;

/**
 * A {@link Wring} to convert <code>int a;
 * a = 3;</code> into <code>int a = 3;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-07
 */
public final class DeclarationInitialiazerAssignment extends Wring.VariableDeclarationFragementAndStatement {
  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer, final Statement nextStatement,
      final TextEditGroup g) {
    if (initializer == null)
      return null;
    final Assignment a = Extract.assignment(nextStatement);
    if (a == null || !same(n, left(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newInitializer = duplicate(right(a));
    if (doesUseForbiddenSiblings(f, newInitializer))
      return null;
    final LocalInlineWithValue i = new LocalInliner(n, r, g).byValue(initializer);
    if (!i.canInlineInto(newInitializer) || i.replacedSize(newInitializer) - size(nextStatement, initializer) > 0)
      return null;
    r.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    r.remove(nextStatement, g);
    return r;
  }
  @Override String description(final VariableDeclarationFragment f) {
    return "Consolidate declaration of " + f.getName() + " with its subsequent initialization";
  }
}