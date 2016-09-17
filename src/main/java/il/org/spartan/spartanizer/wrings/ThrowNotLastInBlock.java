package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Convert <code>throw X;statement;</code> to <code>throw X;</code>.
 * @author Yossi Gil
 * @since 2016 */
public final class ThrowNotLastInBlock extends ReplaceToNextStatement<ThrowStatement> implements Kind.NOP {
  @Override public String description(final ThrowStatement ¢) {
    return "Remove dead statement after " + ¢;
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final ThrowStatement s, final Statement nextStatement, final TextEditGroup g) {
    final ASTNode parent = parent(s);
    if (!iz.block(parent))
      r.remove(nextStatement, g);
    else {
      final ListRewrite $ = r.getListRewrite(s.getParent(), Block.STATEMENTS_PROPERTY);
      $.remove(nextStatement, g);
    }
    return r;
  }
}
