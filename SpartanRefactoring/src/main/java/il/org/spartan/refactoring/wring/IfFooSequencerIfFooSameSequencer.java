package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A {@link Wring} to convert <code>if (X) return A; if (Y) return A;</code>
 * into <code>if (X || Y) return A;</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfFooSequencerIfFooSameSequencer extends Wring.ReplaceToNextStatement<IfStatement> implements
    Kind.ConsolidateStatements {
  private static IfStatement makeIfWithoutElse(final Statement s, final InfixExpression condition) {
    final IfStatement $ = condition.getAST().newIfStatement();
    $.setExpression(condition);
    $.setThenStatement(s);
    $.setElseStatement(null);
    return $;
  }
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement,
      @SuppressWarnings("unused") final TextEditGroup __) {
    if (!Is.vacuousElse(s))
      return null;
    final IfStatement s2 = asIfStatement(nextStatement);
    if (s2 == null || !Is.vacuousElse(s2))
      return null;
    final Statement then = then(s);
    final List<Statement> ss1 = extract.statements(then);
    if (!same(ss1, extract.statements(then(s2))) || !Is.sequencer(last(ss1)))
      return null;
    scalpel.replaceWith(makeIfWithoutElse(BlockSimplify.reorganizeNestedStatement(then, scalpel),
        Subject.pair(s.getExpression(), s2.getExpression()).to(CONDITIONAL_OR)));
    return r;
  }
  @Override String description(final IfStatement s) {
    return "Consolidate if(" + s.getExpression() + ") ... with the next if' statements whose body is identical";
  }
}