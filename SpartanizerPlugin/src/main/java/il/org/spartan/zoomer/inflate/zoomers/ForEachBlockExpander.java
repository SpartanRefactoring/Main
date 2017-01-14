package il.org.spartan.zoomer.inflate.zoomers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts foreach statement to foreach {statement} Test case
 * is{@link Issue1023}
 * @author Raviv Rachmiel
 * @since 10-01-17 */
public class ForEachBlockExpander extends ReplaceCurrentNode<EnhancedForStatement> implements TipperCategory.Expander {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final EnhancedForStatement s) {
    if (s == null)
      return null;
    final EnhancedForStatement $ = copy.of(s);
    // TODO: Raviv Rachmiel ... the next two statements do nothing, I think.--yg
    if (az.enhancedFor(s) == null)
      return null;
    final Block b = $.getAST().newBlock();
    b.statements().add(copy.of(s.getBody()));
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

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "expand to block";
  }
}
