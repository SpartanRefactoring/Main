package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.subject.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** convert {@code
 * if (x)
 *   a += 3;
 * else
 *   a += 9;
 * } into {@code
 * a += x ? 3 : 9;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-29 */
public final class IfAssignToFooElseAssignToFoo extends IfAbstractPattern //
    implements TipperCategory.Ternarization {
  private static final long serialVersionUID = 1076426871620962241L;
  private Assignment thenAssignment;
  private Assignment elzeAssignment;
  private Expression to;
  private Operator thenOperator;

  public IfAssignToFooElseAssignToFoo() {
    andAlso(Proposition.of("Then part is an assignment", //
        () -> (thenAssignment = extract.assignment(then)) != null));
    andAlso(Proposition.of("Else part is an assignment", //
        () -> (elzeAssignment = extract.assignment(elze)) != null));
    andAlso(Proposition.of("Both assignments are to the same target", //
        () -> wizard.same(to = to(thenAssignment), to(elzeAssignment))));
    andAlso(Proposition.of("Assignment operators are compatible", //
        () -> wizard.areEqual(thenOperator = thenAssignment.getOperator(), elzeAssignment.getOperator())));
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate assignments to " + to;
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    r.replace(current, //
        pair(to, //
            pair(from(thenAssignment), from(elzeAssignment)).toCondition(condition)//
        ).toStatement(thenOperator), g);
    return r;
  }

  @Override public Example[] examples() {
    return null;
  }
}
