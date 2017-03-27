package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.utils.*;

/** Remove unused variable
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-23 */
public final class LocalVariableInitializedUnusedRemove extends LocalVariableInitialized implements TipperCategory.Deadcode {
  private static final long serialVersionUID = -855471283048149285L;

  public LocalVariableInitializedUnusedRemove() {
    andAlso(Proposition.of("Local variable is unused", () -> collect.usesOf(name).in(scope.of(name)).isEmpty()));
  }

  @Override public String description() {
    return "Remove unused, uninitialized variable";
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Remove unused variable: " + trivia.gist(¢);
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final Block b = az.block(parent().getParent());
    if (b == null)
      return r;
    final ListRewrite l = r.getListRewrite(b, Block.STATEMENTS_PROPERTY);
    for (final Statement s : wizard.decompose(initializer()))
      l.insertBefore(copy.of(s), parent(), g);
    wizard.removeFragment(object(), r, g);
    return r;
  }
}
