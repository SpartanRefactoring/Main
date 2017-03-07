package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code throw X;statement;} to {@code throw X;}.
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
public final class SequencerNotLastInBlock<S extends Statement> extends ReplaceToNextStatement<S>//
    implements TipperCategory.Deadcode {
  private static final long serialVersionUID = -7502939324573443092L;

  @Override public String description(final S ¢) {
    return "Remove dead statement after " + ¢;
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final S s, final Statement nextStatement, final TextEditGroup g) {
    final Block b = az.block(parent(s));
    if (b == null) {
      $.remove(nextStatement, g);
      return $;
    }
    $.getListRewrite(b, Block.STATEMENTS_PROPERTY).remove(nextStatement, g);
    return $;
  }
}
