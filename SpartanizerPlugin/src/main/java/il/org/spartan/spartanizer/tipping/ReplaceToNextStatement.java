/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ReplaceToNextStatement<N extends ASTNode> extends CarefulTipper<N> {
  @Override public boolean prerequisite(@NotNull final N current) {
    final Statement $ = extract.nextStatement(current);
    return $ != null && go(ASTRewrite.create(current.getAST()), current, $, null) != null;
  }

  @Override @Nullable public Tip tip(@NotNull final N n, @Nullable final ExclusionManager exclude) {
    final Statement $ = extract.nextStatement(n);
    assert $ != null;
    if (exclude != null && exclude.isExcluded($)) // see issue #1101
      return null;
    if (exclude != null)
      exclude.exclude($);
    return new Tip(description(n), n, getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        ReplaceToNextStatement.this.go(r, n, $, g);
      }
    };
  }

  @Nullable protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);
}
