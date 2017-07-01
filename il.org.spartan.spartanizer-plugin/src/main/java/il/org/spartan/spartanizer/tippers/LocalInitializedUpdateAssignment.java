package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedUpdateAssignment extends $FragmentAndStatement//
    implements Category.Collapse {
  private static final long serialVersionUID = -0x601DD969FC862E65L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }
  @Override public String description() {
    return "Consolidate declaration of variable with its subsequent initialization";
  }
  @Override protected ASTRewrite go(final ASTRewrite ret, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.eq(n, to(a)) || $FragmentAndStatement.doesUseForbiddenSiblings(f, from(a)))
      return null;
    final Operator o = a.getOperator();
    if (o == ASSIGN)
      return null;
    final InfixExpression newInitializer = subject.pair(to(a), from(a)).to(op.assign2infix(o));
    final InlinerWithValue i = new Inliner(n, ret, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    ret.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    ret.remove(nextStatement, g);
    return ret;
  }
}
