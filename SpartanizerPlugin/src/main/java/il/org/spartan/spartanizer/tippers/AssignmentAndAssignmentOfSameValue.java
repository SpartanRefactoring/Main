package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** See {@link #examples()} for documentation
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class AssignmentAndAssignmentOfSameValue extends AssignmentPattern//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = 0x69CDEE55CA481121L;
  private Assignment nextAssignment;
  private ExpressionStatement nextExpressionStatement;
  private Expression origin;
  private Expression nextOrigin;

  @Override public Examples examples() {
    return convert("a=3;b=3;").to("b=a=3;") //
        .convert("a=c;b=c;").to("b=a=c;") //
    ;
  }

  public AssignmentAndAssignmentOfSameValue() {
    andAlso("Assignment is an expression statement", //
        () -> iz.expressionStatement(parent));
    andAlso("Has origin", //
        () -> not.nil(origin = hop.origin(current)));
    andAlso("Origin is not null", //
        () -> !iz.nullLiteral(origin));
    andAlso("Next statement is expression statement", //
        () -> not.nil(nextExpressionStatement = az.expressionStatement(nextStatement)));
    andAlso("Next statement is assignmet", //
        () -> not.nil(nextAssignment = az.assignment(nextExpressionStatement.getExpression())));
    andAlso("Next assignment has origin", //
        () -> not.nil(nextOrigin = hop.origin(nextAssignment)));
    andAlso("Origins are identical value is identical", //
        () -> wizard.eq(origin, nextOrigin));
    andAlso("Assigned value is deterministic", //
        () -> sideEffects.deterministic(origin));
  }

  @Override public String description() {
    return "Consolidate assignment to " + to + " with subsequent similar assignment";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    $.remove(parent, g);
    $.replace(nextOrigin, copy.of(current), g);
    return $;
  }
}
