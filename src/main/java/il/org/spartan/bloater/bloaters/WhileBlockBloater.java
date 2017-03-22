package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts while(condition)statement to while(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
public class WhileBlockBloater extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -2161530114677301355L;

  @Override @Nullable public ASTNode replacement(@Nullable final WhileStatement s) {
    if (s == null)
      return null;
    final WhileStatement $ = copy.of(s);
    final Block b = $.getAST().newBlock();
    statements(b).add(copy.of(body(s)));
    final Collection<Boolean> cc = new ArrayList<>();
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

  @Override @NotNull public String description(@SuppressWarnings("unused") final WhileStatement __) {
    return "expand to block";
  }
}
