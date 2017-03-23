package il.org.spartan.spartanizer.tippers;
import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Removes redundant assignment- an assignment with same variable subsequent
 * assignment.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-20
 * @see issue #1110 */
public class AssignmentAndAssignmentOfSameVariable extends ReplaceToNextStatement<Assignment> //
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -2175075259560385549L;

  @Override public String description(Assignment a) {
    return "eliminate dead assignment to '" + to(a) + "'";
  }

  @Override public String description() {
    return "eliminate dead assignment";
  }

  @Override public Example[] examples() {
    return new Example[] { //
        convert("x = 1; x = 2;") //
            .to("x = 2;"), //
        convert("x.y = 1; x.y = 2;") //
            .to("x.y = 2;"), //
        ignores("x = f(); x = 2;"), //
        ignores("x = 1; x += 2;") //
    };
  }

  @Override protected ASTRewrite go(ASTRewrite $, Assignment a, Statement nextStatement, TextEditGroup g) {
    Assignment nextAssignment = az.assignment(expression(az.expressionStatement(nextStatement)));
    if (nextAssignment == null || nextAssignment.getOperator() != Operator.ASSIGN)
      return null;
    Name to1 = az.name(a.getLeftHandSide());
    Expression from1 = a.getRightHandSide();
    if (to1 == null || from1 == null)
      return null;
    Name to2 = az.name(nextAssignment.getLeftHandSide());
    if (to2 == null || !to1.getFullyQualifiedName().equals(to2.getFullyQualifiedName()) || !sideEffects.sink(from1) || !(to1 instanceof SimpleName))
      return null;
    collect.usesOf((SimpleName) to1).in(from(nextAssignment));
    $.remove(a.getParent(), g);
    return $;
  }
}
