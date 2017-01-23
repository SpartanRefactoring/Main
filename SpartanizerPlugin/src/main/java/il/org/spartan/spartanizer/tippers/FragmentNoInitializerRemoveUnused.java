package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** Remove unused variable
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-23 */
public final class FragmentNoInitializerRemoveUnused extends CarefulTipper<VariableDeclarationFragment>//
    implements TipperCategory.Deadcode {
  @Override public String description() {
    return "Remove unused, uninitialized variable";
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Remove unused variable: " + trivia.gist(¢);
  }

  @Override public Tip tip(final VariableDeclarationFragment f) {
    return !iz.variableDeclarationStatement(parent(f)) || f.getInitializer() != null || haz.annotation(f)
        || !collect.usesOf(f.getName()).in(scope.of(f)).isEmpty() ? null : new Tip(description(f), f, getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            eliminate(f, r, g);
          }
        };
  }

  @Override protected boolean prerequisite(final VariableDeclarationFragment f) {
    return iz.variableDeclarationStatement(parent(f)) && f.getInitializer() == null && !haz.annotation(f)
        && collect.usesOf(f.getName()).in(scope.of(f)).isEmpty();
  }
}
