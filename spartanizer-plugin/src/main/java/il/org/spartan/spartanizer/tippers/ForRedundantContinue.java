package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** removes continue in for loop if it's last statement in the loop, Issue
 * {@code Issue0882}.
 * @author Raviv Rachmiel
 * @author Doron Meshulam
 * @since 2016-11-26 */
public class ForRedundantContinue extends NonEmptyForLoop//
    implements Category.Loops {
  private static final long serialVersionUID = 0x1DA2D2D1173F3165L;

  @Override public Examples examples() {
    return convert("for(int i=0;i<5;++i) continue;").to("for(int i=0;i<5;++i) ;");
  }
  public ForRedundantContinue() {
    andAlso("Applicable only on loops ending with continue", //
        () -> iz.continueStatement(lastStatement));
  }
  @Override public String description() {
    return "Prune redundant " + extract.lastStatement(current);
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final Block b = az.block(body);
    if (b == null)
      $.replace(body, copy.of($.getAST().newEmptyStatement()), g);
    else {
      statements(b).remove(lastStatement);
      $.replace(az.block(body), copy.of(b), g);
    }
    return $;
  }
}