package il.org.spartan.bloater.bloaters;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** converts foreach statement to foreach {statement} Test case
 * is{@link Issue1023}
 * @author Raviv Rachmiel
 * @since 10-01-17 */
public class ForEachBlockBloater extends ReplaceCurrentNode<EnhancedForStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 4091243669569596651L;

  @Override public ASTNode replacement(final EnhancedForStatement s) {
    if (s == null)
      return null;
    // TODO: Raviv please use class subject
    final EnhancedForStatement $ = copy.of(s);
    final Block b = $.getAST().newBlock();
    statements(b).add(copy.of(body(s)));
    final Collection<Boolean> cc = new ArrayList<>();
    body(s).accept(new ASTVisitor(true) {
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
