package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** convert
 *
 * <pre>
 * if (x)
 *   return foo();
 * return bar();
 * </pre>
 *
 * into
 *
 * <pre>
 * return a ? foo() : bar();
 * </pre>
 *
 * return a; } g();
 * </pre>
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfReturnNoElseReturn extends ReplaceToNextStatement<IfStatement> implements Kind.Ternarization {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate into a single 'return'";
  }

  @Override public boolean claims(final IfStatement ¢) {
    return iz.vacuousElse(¢) && extract.returnStatement(then(¢)) != null && extract.nextReturn(¢) != null;
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!iz.vacuousElse(s))
      return null;
    final ReturnStatement r1 = extract.returnStatement(then(s));
    if (r1 == null)
      return null;
    final Expression e1 = extract.core(r1.getExpression());
    if (e1 == null)
      return null;
    final ReturnStatement r2 = extract.returnStatement(nextStatement);
    if (r2 == null)
      return null;
    final Expression e2 = extract.core(r2.getExpression());
    return e2 == null ? null : Wrings.replaceTwoStatements(r, s, subject.operand(subject.pair(e1, e2).toCondition(s.getExpression())).toReturn(), g);
  }
}
