package il.org.spartan.zoomer.inflate.zoomers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ForStatement;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts foreach statement to foreach {statement} Issue ##1023
 * {@link Issue1023}
 * @author Raviv Rachmiel
 * @since 10-01-17 */
public class ForEachBlockExpander extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Expander {

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "expand to block";
  }
  @SuppressWarnings("unchecked")
  @Override public ASTNode replacement(ForStatement s) {
    if (s == null)
      return null;
    final ForStatement $ = copy.of(s);
    if(az.enhancedFor(s)==null) {
      return null;
    }
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
  
}

