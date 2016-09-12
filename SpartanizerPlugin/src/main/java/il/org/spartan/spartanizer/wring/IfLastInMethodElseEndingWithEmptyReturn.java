package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** convert
 *
 * <pre>
 * if (a) {
 *   return x;
 * } else {
 *   return y;
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * if (a)
 *   return x;
 * return y;
 * </pre>
 *
 * provided that this
 *
 * <pre>
 * <b>if</b>
 * </pre>
 *
 * statement is the last statement in a method.
 * @author Yossi Gil
 * @author Daniel Mittelman <tt><mittelmania [at] gmail.com></tt>
 * @since 2015-09-09 */
public final class IfLastInMethodElseEndingWithEmptyReturn extends Wring<IfStatement> implements Kind.Canonicalization {
  @SuppressWarnings("unused") @Override String description(final IfStatement ____) {
    return "Remove redundant return statement in 'else' branch of if statement that terminates this method";
  }

  @Override Rewrite make(final IfStatement s) {
    final Block b = az.block(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !lastIn(s, statements(b)))
      return null;
    final ReturnStatement deleteMe = az.returnStatement(hop.lastStatement(elze(s)));
    return deleteMe == null || deleteMe.getExpression() != null ? null : new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(deleteMe, s.getAST().newEmptyStatement(), g);
      }
    };
  }
}
