package il.org.spartan.spartanizer.tippers;
import static fluent.ly.the.*;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * if (X)
 *   return A;
 * if (Y)
 *   return A;
 * } into {@code
 * if (X || Y)
 *   return A;
 * }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfFooSequencerIfFooSameSequencer extends GoToNextStatement<IfStatement>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = 0x8B65A31C473C28BL;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate two 'if' statements with identical body";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!iz.vacuousElse(s))
      return null;
    final IfStatement $ = az.ifStatement(nextStatement);
    if ($ == null || !iz.vacuousElse($))
      return null;
    final Statement then = then(s);
    final List<Statement> ss1 = extract.statements(then);
    return !wizard.eq(ss1, extract.statements(then($))) || !iz.sequencer(last(ss1)) ? null
        : misc.replaceTwoStatements(r, s,
            make.ifWithoutElse(BlockSimplify.reorganizeNestedStatement(then), subject.pair(s.getExpression(), $.getExpression()).to(CONDITIONAL_OR)),
            g);
  }
}