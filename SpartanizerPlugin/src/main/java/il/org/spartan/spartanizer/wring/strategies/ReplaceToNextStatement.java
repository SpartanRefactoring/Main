package il.org.spartan.spartanizer.wring.strategies;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;

public abstract class ReplaceToNextStatement<N extends ASTNode> extends Wring<N> {
  @Override public Rewrite make(final N n, final ExclusionManager exclude) {
    final Statement nextStatement = extract.nextStatement(n);
    if (nextStatement == null || !eligible(n))
      return null;
    exclude.exclude(nextStatement);
    return new Rewrite(description(n), n, nextStatement) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        ReplaceToNextStatement.this.go(r, n, nextStatement, g);
      }
    };
  }

  @Override public boolean scopeIncludes(final N n) {
    final Statement nextStatement = extract.nextStatement(n);
    return nextStatement != null && go(ASTRewrite.create(n.getAST()), n, nextStatement, null) != null;
  }

  protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);
}