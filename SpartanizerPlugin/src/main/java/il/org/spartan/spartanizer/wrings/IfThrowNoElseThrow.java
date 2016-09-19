package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
 *
 * <pre>
 * if (x)
 *   throw foo();
 * throw bar();
 * </pre>
 *
 * into
 *
 * <pre>
 * throw a ? foo() : bar();
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-09-09 */
public final class IfThrowNoElseThrow extends ReplaceToNextStatement<IfStatement> implements Kind.Ternarization {
  static Expression getThrowExpression(final Statement ¢) {
    final ThrowStatement $ = extract.throwStatement(¢);
    return $ == null ? null : extract.core($.getExpression());
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate into a single 'throw'";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!iz.vacuousElse(s))
      return null;
    final Expression e1 = getThrowExpression(then(s));
    if (e1 == null)
      return null;
    final Expression e2 = getThrowExpression(nextStatement);
    return e2 == null ? null : Wrings.replaceTwoStatements(r, s, subject.operand(subject.pair(e1, e2).toCondition(s.getExpression())).toThrow(), g);
  }
}
