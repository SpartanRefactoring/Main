package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
 *
 * <pre>
 * if (X)
 *   return A;
 * if (Y)
 *   return A;
 * </pre>
 *
 * into
 *
 * <pre>
 * if (X || Y)
 *   return A;
 * </pre>
 * 
 * @author Yossi Gil
 * @since 2015-07-29 */
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
    final List<Statement> ss1 = extract.statements(then);
    return !same(ss1, extract.statements(then(s2))) || !Is.sequencer(last(ss1)) ? null
        : Wrings.replaceTwoStatements(r, s,
            makeIfWithoutElse(BlockSimplify.reorganizeNestedStatement(then), Subject.pair(s.getExpression(), s2.getExpression()).to(CONDITIONAL_OR)),
            g);
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate two 'if' statements with identical body";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}