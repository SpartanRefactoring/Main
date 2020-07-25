package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.subject.pair;
import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.to;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.lisp;
import fluent.ly.not;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** @author Yossi Gil
 * @since 2015-07-29 */
public final class IfAssignToFooElseAssignToFoo extends IfAbstractPattern //
    implements Category.Ternarization {
  private static final long serialVersionUID = 0xEF03C895DA707C1L;
  private Assignment thenAssignment;
  private Assignment elzeAssignment;
  private Expression to;
  private Assignment.Operator thenOperator;

  @Override protected ASTNode highlight() {
    return to;
  }
  public IfAssignToFooElseAssignToFoo() {
    andAlso("Then part is an assignment", //
        () -> not.nil(thenAssignment = extract.assignment(then)));
    andAlso("Else part is an assignment", //
        () -> not.nil(elzeAssignment = extract.assignment(elze)));
    andAlso("Both assignments are to the same target", //
        () -> wizard.eq(to = to(thenAssignment), to(elzeAssignment)));
    andAlso("Assignment operators are compatible", //
        () -> lisp.areEqual(thenOperator = thenAssignment.getOperator(), elzeAssignment.getOperator()));
  }
  @Override public String description() {
    return "Consolidate assignments to " + to;
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(current, //
        pair(to, //
            pair(from(thenAssignment), from(elzeAssignment)).toCondition(condition)//
        ).toStatement(thenOperator), g);
    return r;
  }
  @Override public Examples examples() {
    return //
    convert("if(x) a += 3; else a += 9;")//
        .to("a += x ? 3 : 9;"). //
        convert("if(x) a = 3; else a = 9;")//
        .to("a = x ? 3 : 9;") //
    ;
  }
}
