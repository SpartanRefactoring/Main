package il.org.spartan.spartanizer.tippers;


import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;

import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** removes continue in for loop if it's last statement in the loop, Issue {@code Issue0882}.
 * @author Raviv Rachmiel
 * @author Doron Meshulam
 * @since 2016-11-26 */
public class ForRedundantContinue extends NonEmptyForLoop//
    implements TipperCategory.Shortcircuit {
  private static final long serialVersionUID = 0x1DA2D2D1173F3165L;


  @Override public Examples examples() {
    return 
        convert("for(int i=0;i<5;++i) continue;").to("for(int i=0;i<5;++i) ;"); 
    }
  
  
  public ForRedundantContinue() {
    andAlso(new Proposition.Singleton("Applicable only on loops ending with continue", () -> iz.continueStatement(lastStatement)));
  }
  
  @Override public String description(final ForStatement ¢) {
    return "Prune redundant " + extract.lastStatement(¢);
  }


  @Override protected ASTRewrite go(ASTRewrite $, TextEditGroup g) {
    Block b = az.block(forBody);
    if(b == null)
      $.replace(forBody, copy.of($.getAST().newEmptyStatement()), g);
    else {
      statements(b).remove(lastStatement);
      $.replace(az.block(forBody), copy.of(b), g);

    }
    
    return $;
  }
}