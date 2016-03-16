package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.asBlock;
import static il.org.spartan.utils.Utils.lastIn;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} that removes empty return statements, provided that 
 * they are last in method.
 *
 * @author Yossi Gil
 * @since 2015-7-17
 */

public final class ReturnLastInMethod extends Wring<ReturnStatement> {
  @Override String description(@SuppressWarnings("unused") final ReturnStatement _) {
    return "Remove redundant return statement";
  }
  @Override Rewrite make(final ReturnStatement s) {
    if (s.getExpression() != null)
      return null;
    final Block b = asBlock(s.getParent());
    return b == null || !lastIn(s, b.statements()) || !(b.getParent() instanceof MethodDeclaration) ? null //
        : new Rewrite(description(s), s) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(s, g);
          }
        };
  }
}
