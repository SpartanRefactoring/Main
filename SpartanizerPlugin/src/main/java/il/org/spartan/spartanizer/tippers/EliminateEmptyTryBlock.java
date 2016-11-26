package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
* try ; catch() {a;}
 * </pre>
 *
 * into
 *
 * <pre>
 * </pre>
 *
 * .
 * @author Sapir Bismot
 * @since 2016-11-21 */
public final class EliminateEmptyTryBlock extends CarefulTipper<TryStatement> implements TipperCategory.Collapse {
  @Override public boolean prerequisite(final TryStatement ¢) {
    final Block $ = ¢.getBody();
    return $ != null && $.statements().isEmpty();
  }

  @Override public Tip tip(final TryStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Block finallyBlock = s.getFinally();
        if (finallyBlock == null || finallyBlock.statements().isEmpty())
          r.remove(s, g);
        else
          r.replace(s, finallyBlock, g);
      }
    };
  }

  @Override public String description(@SuppressWarnings("unused") final TryStatement __) {
    return "Eliminate empty try block";
  }
}
