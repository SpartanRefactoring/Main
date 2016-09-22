package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
 *
 * <pre>
 * if (a) {
 *   f();
 *   return;
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * if (a)
 *   f();
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
 * @since 2016 */
public final class IfLastInMethodThenEndingWithEmptyReturn extends EagerWring<IfStatement> implements Kind.EarlyReturn {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant return statement in 'then' branch of if statement that terminates this method";
  }

  @Override public Suggestion suggest(final IfStatement s, final ExclusionManager exclude) {
    final Block b = az.block(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !lastIn(s, statements(b)))
      return null;
    final ReturnStatement deleteMe = az.returnStatement(hop.lastStatement(then(s)));
    return deleteMe == null || deleteMe.getExpression() != null || exclude != null && exclude.equals(s) ? null : new Suggestion(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(deleteMe, make.emptyStatement(deleteMe), g);
      }
    };
  }
}
