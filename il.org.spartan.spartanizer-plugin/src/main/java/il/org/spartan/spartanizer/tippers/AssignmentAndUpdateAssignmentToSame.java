package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** convert {@code
 * a += 3;
 * b += 6;
 * } to {@code
 * a  += 3 + 6;
 * }AssignmentUpdateAndSameUpdate
 * <p>
 * Tested by {@link Issue1133}
 * @author Yossi Gil
 * @since 2017-03-04 */
public final class AssignmentAndUpdateAssignmentToSame extends GoToNextStatement<Assignment>//
    implements Category.Collapse {
  private static final long serialVersionUID = 0x5D0C16C013FC1AA3L;

  @Override public Examples examples() {
    return convert("a+=3;b=6;").to("a+=3+6");
  }
  @Override public String description(final Assignment ¢) {
    return "Consolidate assignment to " + to(¢) + " with subsequent update assignment";
  }
  @Override protected ASTRewrite go(final ASTRewrite ret, final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (a1.getOperator() != ASSIGN || !iz.statement(parent(a1)))
      return null;
    final Assignment a2 = extract.assignment(nextStatement);
    final Assignment.Operator o = a2.getOperator();
    if (o == ASSIGN)
      return null;
    final Expression to = to(a1);
    if (!wizard.eq(to, to(a2)) || !sideEffects.free(to))
      return null;
    ret.replace(from(a1), subject.operands(from(a1), from(a2)).to(op.assign2infix(o)), g);
    ret.remove(nextStatement, g);
    return ret;
  }
}
