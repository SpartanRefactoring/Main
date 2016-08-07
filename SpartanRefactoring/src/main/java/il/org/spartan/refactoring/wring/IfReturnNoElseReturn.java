package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
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
 * return a; } g(); </pre>
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfReturnNoElseReturn extends Wring.ReplaceToNextStatement<IfStatement> {
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!Is.vacuousElse(s))
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
    return e2 == null ? null : Wrings.replaceTwoStatements(r, s, Subject.operand(Subject.pair(e1, e2).toCondition(s.getExpression())).toReturn(), g);
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return Is.vacuousElse(s) && extract.returnStatement(then(s)) != null && extract.nextReturn(s) != null;
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate into a single 'return'";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.IF_TO_TERNARY;
  }
}