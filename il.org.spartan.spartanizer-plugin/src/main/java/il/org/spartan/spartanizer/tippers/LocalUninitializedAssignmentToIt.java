package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalUninitializedAssignmentToIt extends $FragmentAndStatement//
    implements Category.Collapse {
  private static final long serialVersionUID = 0xCE4CF4E3910F992L;

  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment ret = copy.of(f);
    ret.setInitializer(copy.of(x));
    return ret;
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
  @Override protected ASTRewrite go(final ASTRewrite ret, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer != null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.eq(n, to(a)) || $FragmentAndStatement.doesUseForbiddenSiblings(f, from(a)))
      return null;
    ret.replace(f, makeVariableDeclarationFragement(f, from(a)), g);
    ret.remove(containing.statement(a), g);
    return ret;
  }
}
