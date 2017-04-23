package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;

/** An abstract tipper pattern for something like {@code
 * int a = 2;
 * if (b)
 *   a ?= 3;
 * }
 * @author Yossi Gil
 * @since 2017-04-23 */
public abstract class LocalInitializedIfAssignmentPattern extends LocalInitialized implements TipperCategory.Inlining {
  private static final long serialVersionUID = 1L;
  protected IfStatement nextIf;
  protected Expression condition;
  protected Assignment assignment;
  protected Operator operator;

  @Override public String description() {
    return "Consolidate initialization of " + name + " with the subsequent conditional assignment to it";
  }

  public LocalInitializedIfAssignmentPattern() {
    notNil("Next statement is if", () -> nextIf = az.ifStatement(nextStatement));
    andAlso("Else is empty", () -> !iz.vacuousElse(nextIf));
    property("Condition", () -> condition = nextIf.getExpression());
    notNil("Extract assignment", () -> assignment = extract.assignment(then(nextIf)));
    andAlso("Assignment is to current variable", () -> wizard.eq(name, to(assignment)));
    andAlso("Does not not use forbbiden siblings", () -> doesUseForbiddenSiblings(current, condition, from(assignment)));
    property("Operator", () -> operator = assignment.getOperator());
  }
}
