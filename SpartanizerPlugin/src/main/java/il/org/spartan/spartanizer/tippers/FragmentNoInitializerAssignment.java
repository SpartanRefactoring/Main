package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentNoInitializerAssignment extends LocalVariableUninitialized implements TipperCategory.Unite {
  private static final long serialVersionUID = 0xCE4CF4E3910F992L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final Assignment a = az.assignment(extract.nextStatement(statement()));
    if (a == null || !wizard.same(name, to(a)))
      return null;
    $.replace(fragment, make.fragment(fragment, from(a)), g);
    $.remove(extract.containingStatement(a), g);
    return $;
  }
}
