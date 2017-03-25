package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.utils.*;

/** Remove unused variable
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-23 */
public final class FragmentVariablleInitializedRemoveUnused extends LocalVariableInitialized implements TipperCategory.Deadcode {
  private static final long serialVersionUID = -855471283048149285L;

  public FragmentVariablleInitializedRemoveUnused() {
    andAlso(Proposition.of("Local variable is unused", () -> collect.usesOf(name).in(scope.of(name)).isEmpty()));
  }

  @Override public String description() {
    return "Remove unused, uninitialized variable";
  }

  @Override @NotNull public String description(final VariableDeclarationFragment ¢) {
    return "Remove unused variable: " + trivia.gist(¢);
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    wizard.eliminate(object(), r, g);
    return r;
  }
}
