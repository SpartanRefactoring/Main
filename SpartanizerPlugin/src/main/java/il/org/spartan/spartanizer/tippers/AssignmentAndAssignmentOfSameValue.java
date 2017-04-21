package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
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

  @Override public Examples examples() {
    return convert("a=3;b=3;").to("b=a=3;") //
        .convert("a=c;b=c;").to("b=a=c;") //
    ;
  }

  public AssignmentAndAssignmentOfSameValue() {
    andAlso("Assignment is an expression statement", //
        () -> iz.expressionStatement(parent));
    andAlso("Simple assignment", //
        () -> operator == ASSIGN);
    andAlso("Assigned value is not null", //
        () -> !iz.nullLiteral(from));
    andAlso("Next statement is assignmet", //
        () -> not.nil(nextAssignment = az.assignment(nextStatement))); 
    andAlso("Next assignment is plain assignmnet", //
        () -> (nextAssignment.getOperator() == ASSIGN)); 
    andAlso("Assigned value is identical", //
        () -> wizard.eq(from, from(nextAssignment))); 
    andAlso("Assigned value is deterministic", //
        () -> sideEffects.deterministic(from)); 
  }

  @Override public String description() {
    return "Consolidate assignment to " + to + " with subsequent similar assignment";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    $.remove(parent, g);
    $.replace(from(nextAssignment), copy.of(current), g);
    return $;
  }

}
