package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Remove blocks that include only variable declerations: <br/>
 * For example, remove the block : </br>
 * <br/>
 * <code> {int a=0;} </code> </br>
 * @author Dor Ma'ayan
 * @since 2016-09-11 */
public final class CastBlockSingletonVariableDefinition extends CarefulTipper<Block> implements TipperCategory.Collapse {
  @Override public String description() {
    return "remove the block";
  }

  @Override public String description(final Block n) {
    return "remove the block: " + n;
  }

  @Override public Tip tip(final Block n) {
    final List<Statement> ss = statements(n);
    if (ss.isEmpty())
      return null;
    for (final Statement ¢ : ss)
      if (!iz.variableDeclarationStatement(¢))
        return null;
    return new Tip(description(), n, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final Statement ¢ : ss)
          r.remove(¢, g);
      }
    };
  }
}
