package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

public abstract class ReplaceToNextStatementExclude<N extends ASTNode> extends CarefulTipper<N> {
  @Override public boolean prerequisite(final N current) {
    final Statement $ = extract.nextStatement(current);
    return $ != null && go(ASTRewrite.create(current.getAST()), current, $, null, new ExclusionManager()) != null;
  }

  @Override public Tip tip(final N n, final ExclusionManager exclude) {
    final Statement $ = extract.nextStatement(n);
    assert $ != null;
    if (exclude != null)
      exclude.exclude($);
    return new Tip(description(n), n, this.getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        ReplaceToNextStatementExclude.this.go(r, n, $, g, exclude);
      }
    };
  }

  protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g, ExclusionManager exclude);
}