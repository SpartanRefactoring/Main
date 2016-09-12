package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Remove blocks that include only variable declerations: <br/>
 * For example, remove the block : </br>
 * <br/>
 * <code> {int a=0;} </code> </br>
 * @author Dor Ma'ayan
 * @since 2016-09-11 */
public class RemoveVariableDefinitionsBlocks extends Wring<Block> implements Kind.Canonicalization {
  @Override public String description() {
    return "remove the block";
  }

  @Override String description(final Block n) {
    return "remove the block: " + n + "";
  }

  @Override Rewrite make(final Block n) {
    final List<Statement> statementsList = n.statements();
    if (statementsList.isEmpty())
      return null;
    for (final Statement s : statementsList)
      if (!iz.variableDeclarationStatement(s))
        return null;
    return new Rewrite(description(), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final Statement s : statementsList)
          r.remove(s, g);
      }
    };
  }
}
