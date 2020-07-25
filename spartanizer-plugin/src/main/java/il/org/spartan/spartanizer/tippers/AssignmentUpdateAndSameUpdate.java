package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.operator;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;
import static org.eclipse.jdt.core.dom.Assignment.Operator.LEFT_SHIFT_ASSIGN;
import static org.eclipse.jdt.core.dom.Assignment.Operator.REMAINDER_ASSIGN;
import static org.eclipse.jdt.core.dom.Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN;
import static org.eclipse.jdt.core.dom.Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.is;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.issues.Issue1132;
import il.org.spartan.spartanizer.java.sideEffects;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
public final class AssignmentUpdateAndSameUpdate extends GoToNextStatement<Assignment>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x10117DE08048A979L;

  @Override public String description(final Assignment ¢) {
    return "Consolidate update assignment to " + to(¢) + " with subsequent similar assignment";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (is.in(a1.getOperator(), ASSIGN, REMAINDER_ASSIGN, LEFT_SHIFT_ASSIGN, RIGHT_SHIFT_SIGNED_ASSIGN, RIGHT_SHIFT_UNSIGNED_ASSIGN))
      return null;
    final ASTNode parent = parent(a1);
    if (!iz.statement(parent))
      return null;
    final Assignment a2 = extract.assignment(nextStatement);
    if (operator(a1) != operator(a2))
      return null;
    final Expression to = to(a1);
    if (!wizard.eq(to, to(a2)) || !sideEffects.free(to))
      return null;
    $.remove(parent, g);
    $.replace(from(a2), subject.operands(from(a1), from(a2)).to(unifying(a1)), g);
    return $;
  }
  private static Operator unifying(final Assignment ¢) {
    final Operator $ = op.assign2infix(¢.getOperator());
    return $ == op.MINUS2 ? op.PLUS2 : $ == DIVIDE ? TIMES : $;
  }
}
