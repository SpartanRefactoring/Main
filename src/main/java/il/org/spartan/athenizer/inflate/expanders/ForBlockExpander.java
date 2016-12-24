package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts while(condition)statement to while(condition){statement} relevant
 * also for for(condition)statement to for(condition){statement} relevant for
 * return (<ternary>)
 * @author Raviv Rachmiel
 * @since 03-12-16 */
public class ForBlockExpander extends ReplaceCurrentNode<ForStatement> implements TipperCategory.InVain {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final ForStatement s) {
    if (s == null)
      return null;
    final ForStatement $ = s.getAST().newForStatement();
    final Block b = s.getAST().newBlock();
    b.statements().add(duplicate.of(s.getBody()));
    $.setBody(b);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "expand to block";
  }
}
