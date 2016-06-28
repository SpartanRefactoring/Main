package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A {@link Wring} that removes empty return statements, provided that they are
 * last in method.
 *
 * @author Yossi Gil
 * @since 2015-7-17
 */
public final class ReturnLastInMethod extends Wring<ReturnStatement> implements Kind.Simplify {
  @Override String description(final ReturnStatement s) {
    return "Remove redundant return statement: " + s;
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
