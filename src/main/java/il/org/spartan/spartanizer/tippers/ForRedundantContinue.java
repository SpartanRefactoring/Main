package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** removes continue in for loop if it's last statement in the loop, Issue
 * {@code Issue0882}.
 * @author Raviv Rachmiel
 * @author Doron Meshulam
 * @since 2016-11-26 */
public class ForRedundantContinue extends NonEmptyForLoop//
    implements TipperCategory.Loops {
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