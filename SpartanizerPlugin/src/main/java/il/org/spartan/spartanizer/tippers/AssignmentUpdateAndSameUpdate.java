package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * a += 3;
 * b += 6;
 * } to {@code
 * a  += 3 + 6;
 * }AssignmentUpdateAndSameUpdate
 * <p>
 * Tested by {@link Issue1132}
 * @author Yossi Gil
 * @since 2017-03-04 */
public final class AssignmentUpdateAndSameUpdate extends ReplaceToNextStatement<Assignment>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = 1L;

  @Override public String description(final Assignment ¢) {
    return "Consolidate update assignment to " + to(¢) + " with subsequent similar assignment";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (in(a1.getOperator(), ASSIGN, REMAINDER_ASSIGN, LEFT_SHIFT_ASSIGN, RIGHT_SHIFT_SIGNED_ASSIGN, RIGHT_SHIFT_UNSIGNED_ASSIGN))
      return null;
    final ASTNode parent = parent(a1);
    if (!iz.statement(parent))
      return null;
    final Assignment a2 = extract.assignment(nextStatement);
    if (operator(a1) != operator(a2))
      return null;
    final Expression to = to(a1);
    if (!wizard.same(to, to(a2)) || !sideEffects.free(to))
      return null;
    $.remove(parent, g);
    $.replace(from(a2), subject.operands(from(a1), from(a2)).to(unifying(a1)), g);
    return $;
  }

  private static Operator unifying(final Assignment ¢) {
    final Operator $ = wizard.assign2infix(¢.getOperator());
    return $ == MINUS2 ? PLUS2 : $ == DIVIDE ? TIMES : $;
  }
}
