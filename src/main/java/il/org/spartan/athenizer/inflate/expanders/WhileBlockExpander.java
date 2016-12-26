package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts while(condition)statement to while(condition){statement}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
public class WhileBlockExpander extends ReplaceCurrentNode<WhileStatement> implements TipperCategory.InVain {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final WhileStatement s) {
    if (s == null)
      return null;
    final WhileStatement $ = duplicate.of(s);
    final Block b = $.getAST().newBlock();
    b.statements().add(duplicate.of(s.getBody()));
    final List<Boolean> cc = new ArrayList<>();
    s.getBody().accept(new ASTVisitor() { @SuppressWarnings("boxing")
    @Override public boolean visit(@SuppressWarnings("unused") Block node) {
      cc.add(true);
      return true;
    }});
    if(!cc.isEmpty()) {
      return null;
    }
    $.setBody(b);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final WhileStatement __) {
    return "expand to block";
  }
}
