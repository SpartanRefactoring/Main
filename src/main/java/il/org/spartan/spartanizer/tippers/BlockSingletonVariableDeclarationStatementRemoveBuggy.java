package il.org.spartan.spartanizer.tippers;

import java.util.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Remove blocks that include only variable declarations: For example, remove
 * the block: <code> {int a=0;} </code>
 * @author Dor Ma'ayan
 * @since 2016-09-11 */
public final class BlockSingletonVariableDeclarationStatementRemoveBuggy extends CarefulTipper<Block>//
    implements TipperCategory.Deadcode {
  @Override public String description() {
    return "Remove the block";
  }

  @Override public String description(final Block ¢) {
    return "Remove the block: " + trivia.gist(¢);
  }

  @Override public Tip tip(final Block n) {
    final List<Statement> $ = statements(n);
    if ($.isEmpty())
      return null;
    for (final Statement ¢ : $)
      if (!iz.variableDeclarationStatement(¢))
        return null;
    return new Tip(description(), n, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        $.forEach(λ -> r.remove(λ, g));
      }
    };
  }
}
