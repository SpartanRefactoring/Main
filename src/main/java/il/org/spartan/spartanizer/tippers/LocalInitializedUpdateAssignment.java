package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.patterns.*;

/** See {@link #examples()}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class LocalInitializedUpdateAssignment extends LocalVariableInitializedStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 0x32344BE2ADA42ED4L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate initialization of " + ¢.getName() + " with the subsequent conditional assignment to it";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    if (initializer == null)
      return null;
    final IfStatement s = az.ifStatement(nextStatement);
    if (s == null || !iz.vacuousElse(s))
      return null;
    final Expression condition = s.getExpression();
    final Assignment a = extract.assignment(then(s));
    if (a == null || !wizard.same(to(a), name) || LocalVariable.doesUseForbiddenSiblings(fragment, condition, from(a))
        || a.getOperator() == Assignment.Operator.ASSIGN)
      return null;
    final ConditionalExpression newInitializer = subject.pair(make.assignmentAsExpression(a), initializer).toCondition(condition);
    final InlinerWithValue i = new Inliner(name, $, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(then(newInitializer), newInitializer.getExpression());
    $.remove(nextStatement, g);
    return $;
  }
}
