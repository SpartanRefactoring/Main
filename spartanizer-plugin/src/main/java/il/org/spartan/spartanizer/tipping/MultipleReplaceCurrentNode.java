package il.org.spartan.spartanizer.tipping;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.range;
import fluent.ly.the;

/** MultipleReplaceCurrentNode replaces multiple nodes in current statement with
 * multiple nodes (or a single node).
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-25 */
public abstract class MultipleReplaceCurrentNode<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 0x1A7A32CF8CA4583FL;

  public abstract ASTRewrite go(ASTRewrite r, N n, TextEditGroup g, List<ASTNode> bss, List<ASTNode> crs);
  @Override public boolean prerequisite(final N ¢) {
    return go(ASTRewrite.create(¢.getAST()), ¢, null, an.empty.list(), an.empty.list()) != null;
  }
  @Override public final Tip tip(final N n) {
    return new Tip(description(n), myClass(), n) {
      @Override @SuppressWarnings("boxing") public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<ASTNode> input = an.empty.list(), output = an.empty.list();
        MultipleReplaceCurrentNode.this.go(r, n, g, input, output);
        if (output.size() == 1)
          input.forEach(λ -> r.replace(λ, the.firstOf(output), g));
        else if (input.size() == output.size())
          range.to(input.size()).forEach(λ -> r.replace(input.get(λ), output.get(λ), g));
      }
    };
  }
}