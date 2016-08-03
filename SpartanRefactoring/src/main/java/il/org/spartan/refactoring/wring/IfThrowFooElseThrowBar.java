package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert <code>if (x) throw b; else throw c;</code> into
 * <code>throw x? b : c</code>
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfThrowFooElseThrowBar extends Wring.ReplaceCurrentNode<IfStatement> {
  @Override Statement replacement(final IfStatement s) {
    final Expression condition = s.getExpression();
    final Expression then = extract.throwExpression(then(s));
    final Expression elze = extract.throwExpression(elze(s));
    return then == null || elze == null ? null : makeThrowStatement(Subject.pair(then, elze).toCondition(condition));
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && extract.throwExpression(then(s)) != null && extract.throwExpression(elze(s)) != null;
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate 'if' into a 'throw' statement of a conditional expression";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.IF_TO_TERNARY;
  }
}