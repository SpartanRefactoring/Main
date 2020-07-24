/* TODO alexkopzon <alexkopzon@192.168.1.10> please add a description
 *
 * @author alexkopzon <alexkopzon@192.168.1.10>
 *
 * @since Sep 29, 2016 */
package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.navigate.extract;

public abstract class ReplaceToNextStatementExclude<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = -0x283289F678CF10B1L;

  @Override public boolean prerequisite(final N ¢) {
    final Statement $ = extract.nextStatement(¢);
    return $ != null && go(ASTRewrite.create(¢.getAST()), ¢, $, null) != null;
  }
  @Override public Tip tip(final N n) {
    final Statement $ = extract.nextStatement(n);
    assert $ != null;
    return new Tip(description(n), myClass(), n, $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        ReplaceToNextStatementExclude.this.go(r, n, $, g);
      }
    }.spanning($);
  }
  protected abstract ASTRewrite go(ASTRewrite r, N n, Statement nextStatement, TextEditGroup g);
}
