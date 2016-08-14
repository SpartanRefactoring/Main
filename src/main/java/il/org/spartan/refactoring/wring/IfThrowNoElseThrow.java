package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>if (x) throw foo(); throw bar();</code> into
 * <code>throw a ? foo (): bar();</code>
 *
 * @author Yossi Gil
 * @since 2015-09-09
 */
public final class IfThrowNoElseThrow extends Wring.ReplaceToNextStatement<IfStatement> implements Kind.Ternarize {
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!Is.vacuousElse(s))
      return null;
    final Expression e1 = getThrowExpression(then(s));
    if (e1 == null)
      return null;
    final Expression e2 = getThrowExpression(nextStatement);
    return e2 == null ? null : Wrings.replaceTwoStatements(r, s,
        Subject.operand(Subject.pair(e1, e2).toCondition(s.getExpression())).toThrow(), g);
  }
  static Expression getThrowExpression(final Statement s) {
    final ThrowStatement $ = extract.throwStatement(s);
    return $ == null ? null : extract.core($.getExpression());
  }
  @Override String description(final IfStatement s) {
    return "Consolidate if(" + s.getExpression() + ") into a singel throw";
  }
}