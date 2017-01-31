package il.org.spartan.bloater.bloaters;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts for(condition)statement to for(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @since 22-12-16 */
public class ForBlockBloater extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.Bloater {
  @Override public ASTNode replacement(final ForStatement s) {
    if (s == null)
      return null;
    final ForStatement $ = copy.of(s);
    final Block b = $.getAST().newBlock();
statements(b).add(copy.of(body(s)));
    final List<Boolean> cc = new ArrayList<>();
    body(s).accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final Block node) {
        cc.add(box.it(true));
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
