package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

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
 * }
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class AssignmentUpdateAndSameUpdate extends ReplaceToNextStatement<Assignment>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = 1L;

  @Override public String description(final Assignment ¢) {
    return "Consolidate update assignment to " + to(¢) + " with subsequent similar assignment";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (a1.getOperator() == ASSIGN)
      return null;
    final ASTNode parent = parent(a1);
    if (!iz.statement(parent))
      return null;
    final Assignment a2 = extract.assignment(nextStatement);
    if (a1.getOperator() != a2.getOperator())
      return null;
    final Expression to = to(a1);
    if (!wizard.same(to, to(a2)) || !sideEffects.free(to))
      return null;
    $.remove(parent, g);
    $.replace(from(a2), subject.operands(from(a1), from(a2)).to(wizard.assign2infix(a1)), g);
    return $;
  }
}
