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
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-01-23 */
public final class FragmentNoInitializerRemoveUnused extends CarefulTipper<VariableDeclarationFragment>//
    implements TipperCategory.Deadcode {
  private static final long serialVersionUID = -855471283048149285L;

  @Override public String description() {
    return "Remove unused, uninitialized variable";
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Remove unused variable: " + trivia.gist(¢);
  }

  @Override public Tip tip(final VariableDeclarationFragment f) {
    return !iz.variableDeclarationStatement(parentStatement(f)) || f.getInitializer() != null || haz.annotation(f)
        || !collect.usesOf(f.getName()).in(scope.of(f)).isEmpty() ? null : new Tip(description(f), f.getName(), getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            eliminate(f, r, g);
          }
        };
  }

  @Override protected boolean prerequisite(final VariableDeclarationFragment ¢) {
    return iz.variableDeclarationStatement(parentStatement(¢)) && ¢.getInitializer() == null && !haz.annotation(¢)
        && collect.usesOf(¢.getName()).in(scope.of(¢)).isEmpty();
  }
}
