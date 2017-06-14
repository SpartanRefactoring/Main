package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Removes redundant assignment- an assignment with same variable subsequent
 * assignment.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-03-20
 * @see issue #1110 */
public class AssignmentAndAssignmentToSameKill extends GoToNextStatement<Assignment> //
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x1E2F6B6CADD65C0DL;

  @Override public String description(@SuppressWarnings("unused") final Assignment __) {
    return description();
  }
  @Override public String description() {
    return "eliminate redundant assignment";
  }
  @Override public Examples examples() {
    return //
    convert("x = 1; x = 2;") //
        .to("x = 2;") //
        .convert("x.y = 1; x.y = 2;") //
        .to("x.y = 2;") //
        .ignores("x = f(); x = 2;") //
        .ignores("x = 1; x += 2;") //
    ;
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Assignment nextAssignment = Optional.of(nextStatement) //
        .map(az::expressionStatement) //
        .map(λ -> az.assignment(λ.getExpression())).orElse(null);
    if (nextAssignment == null || !Operator.ASSIGN.equals(a.getOperator()) || !Operator.ASSIGN.equals(nextAssignment.getOperator()))
      return null;
    final Name left1 = az.name(a.getLeftHandSide());
    final Expression right1 = a.getRightHandSide();
    if (left1 == null || right1 == null)
      return null;
    final Name left2 = az.name(nextAssignment.getLeftHandSide());
    if (left2 == null //
        || !left1.getFullyQualifiedName().equals(left2.getFullyQualifiedName()) //
        || !sideEffects.sink(right1))
      return null;
    $.remove(a.getParent(), g);
    return $;
  }
}
