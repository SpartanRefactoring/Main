package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Removes redundant assignment- an assignment with same variable subsequent
 * assignment.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-03-20
 * @see issue #1110 */
public class AssignmentAndAssignmentOfSameVariable extends GoToNextStatement<Assignment> //
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -2175075259560385549L;

  @Override  public String description(@SuppressWarnings("unused") final Assignment __) {
    return description();
  }

  @Override  public String description() {
    return "eliminate redundant assignment";
  }

  @Override  public Example[] examples() {
    return new Example[] { //
        convert("x = 1; x = 2;") //
            .to("x = 2;"), //
        convert("x.y = 1; x.y = 2;") //
            .to("x.y = 2;"), //
        Example.ignores("x = f(); x = 2;"), //
        Example.ignores("x = 1; x += 2;") //
    };
  }

  @Override @Nullable protected ASTRewrite go( final ASTRewrite $,  final Assignment a,  final Statement nextStatement,
      final TextEditGroup g) {
    final Assignment nextAssignment = Optional.of(nextStatement) //
        .map(λ -> az.expressionStatement(λ)) //
        .map(λ -> az.assignment(λ.getExpression())).orElse(null);
    if (nextAssignment == null || nextAssignment.getOperator() != Operator.ASSIGN)
      return null;
    @Nullable final Name left1 = az.name(a.getLeftHandSide());
    final Expression right1 = a.getRightHandSide();
    if (left1 == null || right1 == null)
      return null;
    @Nullable final Name left2 = az.name(nextAssignment.getLeftHandSide());
    if (left2 == null //
        || !left1.getFullyQualifiedName().equals(left2.getFullyQualifiedName()) //
        || !sideEffects.sink(right1))
      return null;
    $.remove(a.getParent(), g);
    return $;
  }
}
