package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tipping.Tipper.Example.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** See {@link #examples()} for documentation
 * <p>
 * Tested by {@link Issue1133}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-04 */
public final class AssignmentAndAssignmentToSame extends ReplaceToNextStatement<Assignment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 1L;

  @Override public Example[] examples() {
    return new Example[] { //
        convert("s=s.f();s=s.f();").to("s=s.f().f()"), //
    };
  }

  @Override public String description(final Assignment ¢) {
    return "Inline assignment to " + to(¢) + " into subsequent assignment";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (a1.getOperator() != ASSIGN || !iz.statement(parent(a1)))
      return null;
    final Assignment a2 = extract.assignment(nextStatement);
    if (operator(a2) != ASSIGN)
      return null;
    final Expression to = to(a1);
    if (!wizard.same(to, to(a2)) || !sideEffects.free(to))
      return null;
    $.replace(from(a1), subject.operands(from(a1), from(a2)).to(assign2infix(a2.getOperator())), g);
    $.remove(nextStatement, g);
    return $;
  }
}
