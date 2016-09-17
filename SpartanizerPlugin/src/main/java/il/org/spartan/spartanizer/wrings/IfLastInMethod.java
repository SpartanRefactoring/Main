package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** convert
 *
 * <pre>
 * <b>if</b> (a) { f(); g(); }
 * </pre>
 *
 * into
 *
 * <pre>
 * <b>if</b> (!a) return f(); g();
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
 * @since 2015-09-09 */
public final class IfLastInMethod extends Wring<IfStatement> implements Kind.EarlyReturn {
  @Override public String description(final IfStatement ¢) {
    return "Invert conditional " + ¢.getExpression() + " for early return";
  }

  @Override public Suggestion suggest(final IfStatement s) {
    if (iz.vacuousThen(s) || !iz.vacuousElse(s) || extract.statements(then(s)).size() < 2)
      return null;
    final Block b = az.block(s.getParent());
    return b == null || !lastIn(s, statements(b)) || !(b.getParent() instanceof MethodDeclaration) ? null : new Suggestion(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.insertAfter(s, extract.statements(then(s)), r, g);
        final IfStatement newIf = duplicate.of(s);
        newIf.setExpression(duplicate.of(make.notOf(s.getExpression())));
        newIf.setThenStatement(s.getAST().newReturnStatement());
        newIf.setElseStatement(null);
        r.replace(s, newIf, g);
      }
    };
  }
}
