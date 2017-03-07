package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * a += 3;
 * b += 6;
 * } to {@code
 * a  += 3 + 6;
 * }AssignmentUpdateAndSameUpdate
 * <p>
 * Tested by {@link Issue1133}
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-03-04 */
public final class AssignmentAndUpdateToSame extends ReplaceToNextStatement<Assignment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 4484468497316065887L;

  @Override public String description(final Assignment ¢) {
    return "Consolidate assignment to " + to(¢) + " with subsequent update assignment";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (a1.getOperator() != ASSIGN || !iz.statement(parent(a1)))
      return null;
    final Assignment a2 = extract.assignment(nextStatement);
    final Assignment.Operator o = a2.getOperator();
    if (o == ASSIGN)
      return null;
    final Expression to = to(a1);
    if (!wizard.same(to, to(a2)) || !sideEffects.free(to))
      return null;
    $.replace(from(a1), subject.operands(from(a1), from(a2)).to(assign2infix(o)), g);
    $.remove(nextStatement, g);
    return $;
  }
}
