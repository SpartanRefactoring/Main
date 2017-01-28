package il.org.spartan.bloater.bloaters;

import java.util.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts while(condition)statement to while(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
public class WhileBlockBloater extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.Bloater {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final WhileStatement s) {
    if (s == null)
      return null;
    final WhileStatement $ = copy.of(s);
    final Block b = $.getAST().newBlock();
    b.statements().add(copy.of(body(s)));
    final List<Boolean> cc = new ArrayList<>();
    body(s).accept(new ASTVisitor() {
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

  @Override public String description(@SuppressWarnings("unused") final WhileStatement __) {
    return "expand to block";
  }
}
