package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class GoToNextStatement<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 5265347217554350758L;

  @Override public boolean prerequisite(@NotNull final N current) {
    @Nullable final Statement $ = extract.nextStatement(current);
    return $ != null && go(ASTRewrite.create(current.getAST()), current, $, null) != null;
  }

  @Override @Nullable public Tip tip(final N n, @Nullable final ExclusionManager exclude) {
    @Nullable final Statement $ = extract.nextStatement(n);
    if ($ == null || exclude != null && exclude.isExcluded($))
      return null;
    if (exclude != null)
      exclude.exclude($);
    return new Tip(description(n), n, getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        GoToNextStatement.this.go(r, n, $, g);
      }
    };
  }

  @Nullable protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);
}
