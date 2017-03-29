package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
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
public final class FragmentNoInitializerAssignment extends LocalVariableInitializedStatement//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 0xCE4CF4E3910F992L;

  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment fragment, final Expression x) {
    final VariableDeclarationFragment $ = copy.of(fragment);
    $.setInitializer(copy.of(x));
    return $;
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(final ASTRewrite $,  final TextEditGroup g) {
    if (initializer != null)
      return null;
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.same(name, to(a)) || LocalVariable.doesUseForbiddenSiblings(fragment, from(a)))
      return null;
    $.replace(fragment, makeVariableDeclarationFragement(fragment, from(a)), g);
    $.remove(extract.containingStatement(a), g);
    return $;
  }
}
