package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** See {@link #examples()}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedAssignment extends $FragmentAndStatement//
    implements Category.Inlining {
  private static final long serialVersionUID = -0x27C8502BBC6F019FL;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + Trivia.gist(¢.getName()) + " with its subsequent initialization";
  }
  @Override public Examples examples() {
    return convert("int a = 2; a = b;")//
        .to("int a = b;")//
        .ignores("int a, b = 2; a = b;") //
    ;
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.eq(n, to(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newInitializer = copy.of(from(a));
    if ($FragmentAndStatement.doesUseForbiddenSiblings(f, newInitializer))
      return null;
    final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement, g);
    return $;
  }
}
