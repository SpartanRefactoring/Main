package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

public abstract class ReplaceToNextStatement<N extends ASTNode> extends CarefulWring<N> {
  @Override public final boolean prerequisite(final N current) {
    final Statement next = extract.nextStatement(current);
    return next != null && go(ASTRewrite.create(current.getAST()), current, next, null) != null;
  }


  @Override public Suggestion suggest(final N n, final ExclusionManager exclude) {
    final Statement nextStatement = extract.nextStatement(n);
    if (nextStatement == null)
      return null;
    if (exclude != null)
      exclude.exclude(nextStatement);
    return new Suggestion(description(n), n, nextStatement) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        ReplaceToNextStatement.this.go(r, n, nextStatement, g);
      }
    };
  }

  protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);
}