package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Tested by {@link Issue1105}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-22 */
public class IfStatementBlockSequencerBlockSameSequencer extends CarefulTipper<IfStatement> implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = 8015068204117686495L;

  @Nullable
  @Override public Tip tip(@NotNull final IfStatement s) {
    return new Tip(description(s), s, IfStatementBlockSequencerBlockSameSequencer.class) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final IfStatement $ = copy.of(s);
        r.getListRewrite(then($), Block.STATEMENTS_PROPERTY).remove(extract.lastStatement(then($)), g);
        @Nullable final Block b = az.block(parent(s));
        final ListRewrite lr = r.getListRewrite(b, Block.STATEMENTS_PROPERTY);
        // This is buggy
        lr.remove(b, g);
        lr.remove(b, g);
        r.replace(s, $, g);
      }
    };
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    if (elze(¢) != null)
      return false;
    final Statement $ = extract.lastStatement(az.block(then(¢)));
    return iz.sequencer($) && wizard.same($, extract.lastStatement(az.block(parent(¢))));
  }

  @Override public String description() {
    return super.description();
  }

  @NotNull
  @Override public String description(final IfStatement ¢) {
    return "Consolidate " + ¢ + " with next statements";
  }
}
