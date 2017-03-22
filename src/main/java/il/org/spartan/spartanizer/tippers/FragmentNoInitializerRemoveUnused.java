package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;

/** Remove unused variable
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-23 */
public final class FragmentNoInitializerRemoveUnused extends $Fragment implements TipperCategory.Deadcode {
  private static final long serialVersionUID = -855471283048149285L;

  @Override public String description() {
    return "Remove unused, uninitialized variable";
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Remove unused and unitialized variable: " + trivia.gist(¢);
  }

  @Override public Tip tip(final VariableDeclarationFragment f) {
    return !collect.usesOf(name()).in(scope.of(f)).isEmpty() ? null : new Tip(description(f), name(), getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            eliminateFragment(r, g);
          }
        };
  }

  @Override
  public boolean prerequisite(final VariableDeclarationFragment ¢) {
    return super.prerequisite(¢) && initializer() == null && collect.usesOf(¢.getName()).in(scope.of(¢)).isEmpty();
  }

  protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    return null;
  }
}
