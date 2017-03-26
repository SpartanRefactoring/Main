package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Convert {@code throw X;statement;} to {@code throw X;}.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public final class SequencerNotLastInBlock<S extends Statement> extends GoToNextStatement<S>//
    implements TipperCategory.Deadcode {
  private static final long serialVersionUID = -7502939324573443092L;

  @Override  public String description(final S ¢) {
    return "Remove dead statement after " + ¢;
  }

  // TODO Roth: failing test
  @Override  public Example[] examples() {
    return new Example[] { //
        convert("{throw new Exeption(); f();}") //
            .to("{throw new Exception();}") //
    };
  }

  @Override  protected ASTRewrite go( final ASTRewrite $, final S s, final Statement nextStatement, final TextEditGroup g) {
     final Block b = az.block(parent(s));
    if (b == null) {
      $.remove(nextStatement, g);
      return $;
    }
    $.getListRewrite(b, Block.STATEMENTS_PROPERTY).remove(nextStatement, g);
    return $;
  }
}
