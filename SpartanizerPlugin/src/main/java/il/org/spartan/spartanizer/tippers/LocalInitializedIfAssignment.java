package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** convert {@code
 * int a = 2;
 * if (b)
 *   a = 3;
 * } into {@code
 * int a = b ? 3 : 2;
 * }
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedIfAssignment extends $FragmentAndStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 0xA6354D94D79638FL;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate initialization of " + ¢.getName() + " with the subsequent conditional assignment to it";
  }
  @Override public Examples examples() {
    return convert("int a = 2;if (b)a = 3;").to("int a = b ? 3 : 2;");
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null)
      return null;
    final IfStatement s = az.ifStatement(nextStatement);
    if (s == null || !iz.vacuousElse(s))
      return null;
    s.setElseStatement(null);
    final Expression condition = s.getExpression();
    if (condition == null)
      return null;
    final Assignment a = extract.assignment(then(s));
    if (a == null || !wizard.eq(to(a), n) || a.getOperator() != ASSIGN || $FragmentAndStatement.doesUseForbiddenSiblings(f, condition, from(a)))
      return null;
    final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (!i.canInlineinto(condition, from(a)))
      return null;
    final ConditionalExpression newInitializer = subject.pair(from(a), initializer).toCondition(condition);
    if (i.replacedSize(newInitializer) > metrics.size(nextStatement, initializer))
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(then(newInitializer), newInitializer.getExpression());
    $.remove(nextStatement, g);
    return $;
  }
}
