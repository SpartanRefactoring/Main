package il.org.spartan.zoomer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts for(condition)statement to for(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @since 22-12-16 */
public class ForBlockExpander extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Expander {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final ForStatement s) {
    if (s == null)
      return null;
    final ForStatement $ = duplicate.of(s);
    final Block b = $.getAST().newBlock();
    b.statements().add(duplicate.of(s.getBody()));
    final List<Boolean> cc = new ArrayList<>();
    s.getBody().accept(new ASTVisitor() {
      @Override @SuppressWarnings("boxing") public boolean visit(@SuppressWarnings("unused") final Block node) {
        cc.add(true);
        return true;
      }
    });
    if (!cc.isEmpty())
      return null;
    $.setBody(b);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "expand to block";
  }
}
