package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;

/** convert
 *
 * <pre>
 * int a;
 * a = 3;
 * </pre>
 *
 * into
 *
 * <pre>
 * int a = 3;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class FragmentInitialiazerUpdateAssignment extends $FragementAndStatement//
    implements TipperCategory.Unite {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.same(n, to(a)) || doesUseForbiddenSiblings(f, from(a)))
      return null;
    final Operator o = a.getOperator();
    if (o == ASSIGN)
      return null;
    final InfixExpression newInitializer = subject.pair(to(a), from(a)).to(wizard.assign2infix(o));
    final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement, g);
    return $;
  }
}
