package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tipping.Tipper.Example.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.issues.*;
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
    final SimpleName to = az.simpleName(to(a1));
    if (!wizard.same(to, to(a2)))
      return null;
    final Inliner r = Inliner.of(to).by(from(a1)).in(as.list(from(a2)));
    return !r.ok() ? null : r.fire($, g);
  }

  private static ASTRewrite go(final ASTRewrite $, final Assignment a1, final TextEditGroup g, final SimpleName to, final Expression from1,
      final Expression from2) {
    $.remove(a1, g);
    final Expression newFrom = copy.of(from2);
    new DefunctInliner(to, $, g).byValue(from1).inlineInto(newFrom);
    $.replace(from2, newFrom, g);
    return $;
  }
}
