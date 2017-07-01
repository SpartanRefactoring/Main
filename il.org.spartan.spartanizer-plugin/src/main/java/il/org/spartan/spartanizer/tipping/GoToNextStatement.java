package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public abstract class GoToNextStatement<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 0x49124565B96B22A6L;

  @Override public boolean prerequisite(final N ¢) {
    final Statement $ = extract.nextStatement(¢);
    return $ != null && go(ASTRewrite.create(¢.getAST()), ¢, $, null) != null;
  }
  @Override public Tip tip(final N n) {
    final Statement ret = extract.nextStatement(n);
    return new Tip(description(n), myClass(), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        GoToNextStatement.this.go(r, n, ret, g);
      }
    }.spanning(n, ret);
  }
  protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);
}
