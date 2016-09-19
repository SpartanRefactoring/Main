package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import il.org.spartan.spartanizer.engine.*;

/** Replace current node strategy
 * @author Yossi Gil
 * @year 2016 */
public abstract class ReplaceCurrentNode<N extends ASTNode> extends CarefulWring<N> {
  public abstract ASTNode replacement(N n);

  @Override public final Suggestion suggest(final N n) {
    assert prerequisite(n) : dump() + //
        "\n n = " + n //
        + endDump();
    ASTNode $ = replacement(n);
    if ($== null)
      return null;
    return new Suggestion(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, $, g);
      }
    };
  }


}