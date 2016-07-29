package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.then;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>if (x) throw foo(); throw bar();</code> into
 * <code>throw a ? foo (): bar();</code>
 *
 * @author Yossi Gil
 * @since 2015-09-09
 */
public final class IfThrowNoElseThrow extends Wring.ReplaceToNextStatement<IfStatement> {
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!Is.vacuousElse(s))
      return null;
    final Expression e1 = getThrowExpression(then(s));
    if (e1 == null)
      return null;
    final Expression e2 = getThrowExpression(nextStatement);
    return e2 == null ? null : Wrings.replaceTwoStatements(r, s, Subject.operand(Subject.pair(e1, e2).toCondition(s.getExpression())).toThrow(), g);
  }
  static Expression getThrowExpression(final Statement s) {
    final ThrowStatement $ = Extract.throwStatement(s);
    return $ == null ? null : Extract.core($.getExpression());
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate into a single 'throw'";
  }
  @Override WringGroup wringGroup() {
	return WringGroup.IF_TO_TERNARY;
  }
}