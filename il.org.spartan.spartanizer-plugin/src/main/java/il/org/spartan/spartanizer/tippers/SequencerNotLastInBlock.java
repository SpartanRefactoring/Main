package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Convert {@code throw X;statement;} to {@code throw X;}.
 * @author Yossi Gil
 * @since 2016 */
public final class SequencerNotLastInBlock<S extends Statement> extends GoToNextStatement<S>//
    implements Category.Deadcode {
  private static final long serialVersionUID = -0x681FCB903C826414L;

  @Override public String description(final S ¢) {
    return "Remove dead statement after " + ¢;
  }
  @Override public Examples examples() {
    return //
    convert("{throw new Exception(); f();}") //
        .to("{throw new Exception();}") //
    ;
  }
  @Override protected ASTRewrite go(final ASTRewrite ret, final S s, final Statement nextStatement, final TextEditGroup g) {
    final Block b = az.block(parent(s));
    if (b == null)
      return null;
    ret.getListRewrite(b, Block.STATEMENTS_PROPERTY).remove(nextStatement, g);
    return ret;
  }
}
