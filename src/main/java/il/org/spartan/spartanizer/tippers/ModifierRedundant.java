package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class ModifierRedundant extends CarefulTipper<Modifier>//
    implements TipperCategory.SyntacticBaggage {
  @NotNull
  @Override public String description(final Modifier ¢) {
    return "Remove redundant [" + ¢ + "] modifier";
  }

  @Override public String description() {
    return "Remove redundant modifier";
  }

  @NotNull
  @Override public Tip tip(@NotNull final Modifier ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.getListRewrite(parent(¢), az.bodyDeclaration(parent(¢)).getModifiersProperty()).remove(¢, g);
      }
    };
  }

  @Override public boolean prerequisite(final Modifier ¢) {
    return test(¢, redundancies(az.bodyDeclaration(parent(¢))));
  }
}
