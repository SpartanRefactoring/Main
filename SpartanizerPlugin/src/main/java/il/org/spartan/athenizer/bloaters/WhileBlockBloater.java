package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts while(condition)statement to while(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
public class WhileBlockBloater extends ReplaceCurrentNode<WhileStatement>//
    implements BloaterCategory.Clarity {
  private static final long serialVersionUID = -0x1DFF4C2FE0A6606BL;

  @Override public ASTNode replacement(final WhileStatement s) {
    if (s == null)
      return null;
    final WhileStatement $ = copy.of(s);
    final Block b = $.getAST().newBlock();
    statements(b).add(copy.of(body(s)));
    final Collection<Boolean> cc = an.empty.list();
    // noinspection SameReturnValue
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
  @Override public String description(@SuppressWarnings("unused") final WhileStatement __) {
    return "expand to block";
  }
}
