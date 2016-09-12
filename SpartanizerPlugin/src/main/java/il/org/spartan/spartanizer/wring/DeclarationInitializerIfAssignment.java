package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.wring.Wrings.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.LocalInliner.*;

/** convert
 *
 * <pre>
 * int a = 2;
 * if (b)
 *   a = 3;
 * </pre>
 *
 * into
 *
 * <pre>
 * int a = b ? 3 : 2;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class DeclarationInitializerIfAssignment //
    extends Wring.VariableDeclarationFragementAndStatement implements Kind.Collapse {
  @Override public String description(final VariableDeclarationFragment f) {
    return "Consolidate initialization of " + f.getName() + " with the subsequent conditional assignment to it";
  }

  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
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
    if (a == null || !wizard.same(left(a), n) || a.getOperator() != Assignment.Operator.ASSIGN || doesUseForbiddenSiblings(f, condition, right(a)))
      return null;
    final LocalInlineWithValue i = new LocalInliner(n, r, g).byValue(initializer);
    if (!i.canInlineinto(condition, right(a)))
      return null;
    final ConditionalExpression newInitializer = subject.pair(right(a), initializer).toCondition(condition);
    final int spending = i.replacedSize(newInitializer);
    final int savings = size(nextStatement, initializer);
    if (spending > savings)
      return null;
    r.replace(initializer, newInitializer, g);
    i.inlineinto(then(newInitializer), newInitializer.getExpression());
    r.remove(nextStatement, g);
    return r;
  }
}
