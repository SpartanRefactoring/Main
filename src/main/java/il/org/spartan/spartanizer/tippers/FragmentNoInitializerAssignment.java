package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentNoInitializerAssignment extends FragmentTipperNoInitializer implements TipperCategory.Unite {
  private static final long serialVersionUID = 929095358016977298L;
  private Assignment assignment;

  @Override public Example[] examples() {
    return new Example[] { //
        Example.convert("int a; a = 3;").to("int a=3;"), //
        Example.convert("int b=2,a c=3; a = 3;").to("int a=3;"), //
    };
  }

  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment $ = copy.of(f);
    $.setInitializer(copy.of(x));
    return $;
  }

  @NotNull @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    $.replace(object(), makeVariableDeclarationFragement(object(), from(assignment)), g);
    $.remove(extract.containingStatement(assignment), g);
    return $;
  }

  @Override public boolean prerequisite(VariableDeclarationFragment f) {
    if (!super.prerequisite(f))
      return false;
    assignment = extract.assignment(nextStatement());
    if (assignment == null || !wizard.same(name(), to(assignment)) || doesUseForbiddenSiblings(object(), from(assignment)))
      return false;
    return true;
  }
}
