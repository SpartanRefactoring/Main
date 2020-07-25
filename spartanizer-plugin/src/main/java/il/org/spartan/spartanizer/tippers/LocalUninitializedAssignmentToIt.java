package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.to;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.containing;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalUninitializedAssignmentToIt extends $FragmentAndStatement//
    implements Category.Collapse {
  private static final long serialVersionUID = 0xCE4CF4E3910F992L;

  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment $ = copy.of(f);
    $.setInitializer(copy.of(x));
    return $;
  }
  @Override public Examples examples() {
    return convert("int a;a=3;").to("int a=3;");
  }
  @Override public String description() {
    return "Consolidate uninitialized declaration with subsequent initialization";
  }
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer != null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.eq(n, to(a)) || $FragmentAndStatement.doesUseForbiddenSiblings(f, from(a)))
      return null;
    $.replace(f, makeVariableDeclarationFragement(f, from(a)), g);
    $.remove(containing.statement(a), g);
    return $;
  }
}
