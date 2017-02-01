/** TODO: alexkopzon <alexkopzon@192.168.1.10> please add a description
 * @author alexkopzon <alexkopzon@192.168.1.10>
 * @since Sep 29, 2016 */
package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ReplaceToNextStatementExclude<N extends ASTNode> extends CarefulTipper<N> {
  @Override public boolean prerequisite(@NotNull final N current) {
    final Statement $ = extract.nextStatement(current);
    return $ != null && go(ASTRewrite.create(current.getAST()), current, $, null, new ExclusionManager()) != null;
  }

  @NotNull
  @Override public Tip tip(@NotNull final N n, @Nullable final ExclusionManager exclude) {
    final Statement $ = extract.nextStatement(n);
    assert $ != null;
    if (exclude != null)
      exclude.exclude($);
    return new Tip(description(n), n, getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        ReplaceToNextStatementExclude.this.go(r, n, $, g, exclude);
      }
    };
  }

  @Nullable
  protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g, ExclusionManager exclude);
}
