package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.then;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>if (x) return foo(); return bar();</code>
 * into <code>return a ? foo (): bar();</code> return a; } g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfReturnNoElseReturn extends Wring.ReplaceToNextStatement<IfStatement> {
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!Is.vacuousElse(s))
      return null;
    final ReturnStatement r1 = Extract.returnStatement(then(s));
    if (r1 == null)
      return null;
    final Expression e1 = Extract.core(r1.getExpression());
    if (e1 == null)
      return null;
    final ReturnStatement r2 = Extract.returnStatement(nextStatement);
    if (r2 == null)
      return null;
    final Expression e2 = Extract.core(r2.getExpression());
    if (e2 == null)
      return null;
    scalpel.operate(s, nextStatement);
    r.remove(nextStatement, g);
    scalpel.replaceWith(Subject.operand(Subject.pair(e1, e2).toCondition(s.getExpression())).toReturn());
    return r;
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return Is.vacuousElse(s) && Extract.returnStatement(then(s)) != null && Extract.nextReturn(s) != null;
  }
  @Override String description(final IfStatement s) {
    return "Consolidate if(" + s.getExpression() + ") ... into a single 'return'";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.IF_TO_TERNARY;
  }
}