package il.org.spartan.spartanizer.wringing;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

/** MultipleReplaceCurrentNode replaces multiple nodes in current statement with
 * multiple nodes (or a single node).
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-25 */
abstract class MultipleReplaceCurrentNode<N extends ASTNode> extends CarefulWring<N> {
  @Override public boolean prerequisite(final N ¢) {
    return go(ASTRewrite.create(¢.getAST()), ¢, null, new ArrayList<>(), new ArrayList<>()) != null;
  }

  @Override public final Suggestion suggest(final N n) {
    return new Suggestion(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<ASTNode> input = new ArrayList<>();
        final List<ASTNode> output = new ArrayList<>();
        MultipleReplaceCurrentNode.this.go(r, n, g, input, output);
        if (output.size() == 1)
          for (final ASTNode ¢ : input)
            r.replace(¢, output.get(0), g);
        else if (input.size() == output.size())
          for (int ¢ = 0; ¢ < input.size(); ++¢)
            r.replace(input.get(¢), output.get(¢), g);
      }
    };
  }

  abstract ASTRewrite go(ASTRewrite r, N n, TextEditGroup g, List<ASTNode> bss, List<ASTNode> crs);
}