package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** converts while(condition)statement to while(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
public class WhileBlockBloater extends ReplaceCurrentNode<WhileStatement>//
    implements Category.Bloater {
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
