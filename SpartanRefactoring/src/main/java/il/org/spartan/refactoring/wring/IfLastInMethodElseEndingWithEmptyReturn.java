package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.asBlock;
import static il.org.spartan.refactoring.utils.Funcs.asReturnStatement;
import static il.org.spartan.refactoring.utils.Funcs.elze;
import static il.org.spartan.utils.Utils.lastIn;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} to convert <code>if (a) { return x; } else { return y; }</code> into
 * <code>if (a) return x; return y;</code> provided that this
 * <code><b>if</b></code> statement is the last statement in a method.
 *
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-09-09
 */

public class IfLastInMethodElseEndingWithEmptyReturn extends Wring<IfStatement> {
  @SuppressWarnings("unused") @Override String description(final IfStatement _) {
    return "Remove redundant return statement in 'else' branch of if statement that terminates this method";
  }
  @Override Rewrite make(final IfStatement s) {
    final Block b = asBlock(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !lastIn(s, b.statements()))
      return null;
    final ReturnStatement deleteMe = asReturnStatement(Extract.lastStatement(elze(s)));
    return deleteMe == null || deleteMe.getExpression() != null ? null : new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(deleteMe, s.getAST().newEmptyStatement(), g);
      }
    };
  }
}
