package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** converts for(condition)statement to for(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @since 22-12-16 */
public class ForBlockBloater extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -2414682418747217785L;

  @Nullable @Override public ASTNode replacement(@Nullable final ForStatement s) {
    if (s == null)
      return null;
    final ForStatement $ = copy.of(s);
    // TODO Raviv please use class subject --yg
    final Block b = $.getAST().newBlock();
    statements(b).add(copy.of(body(s)));
    @NotNull final Collection<Boolean> cc = new ArrayList<>();
    // noinspection SameReturnValue
    body(s).accept(new ASTVisitor(true) {
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

  @NotNull @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "expand to block";
  }
}
