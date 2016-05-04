package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.asIfStatement;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.refactoring.utils.Funcs.then;
import static il.org.spartan.utils.Utils.last;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;

import java.util.List;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>if (X) return A; if (Y) return A;</code>
 * into <code>if (X || Y) return A;</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfFooSequencerIfFooSameSequencer extends Wring.ReplaceToNextStatement<IfStatement> {
  private static IfStatement makeIfWithoutElse(final Statement s, final InfixExpression condition) {
    final IfStatement $ = condition.getAST().newIfStatement();
    $.setExpression(condition);
    $.setThenStatement(s);
    $.setElseStatement(null);
    return $;
  }
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!Is.vacuousElse(s))
      return null;
    final IfStatement s2 = asIfStatement(nextStatement);
    if (s2 == null || !Is.vacuousElse(s2))
      return null;
    final Statement then = then(s);
    final List<Statement> ss1 = Extract.statements(then);
    if (!same(ss1, Extract.statements(then(s2))) || !Is.sequencer(last(ss1)))
      return null;
    r.remove(s, g);
    comments.setCore(makeIfWithoutElse(BlockSimplify.reorganizeNestedStatement(then),
        Subject.pair(s.getExpression(), s2.getExpression()).to(CONDITIONAL_OR)));
    return r;
  }
  @Override String description(final IfStatement s) {
    return "Consolidate if(" + s.getExpression() + ") ... with the next if' statements whose body is identical";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}