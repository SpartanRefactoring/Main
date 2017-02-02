package il.org.spartan.spartanizer.tipping;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import org.jetbrains.annotations.NotNull;

/** MultipleReplaceCurrentNode replaces multiple nodes in current statement with
 * multiple nodes (or a single node).
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-25 */
public abstract class MultipleReplaceCurrentNode<N extends ASTNode> extends CarefulTipper<N> {
  public abstract ASTRewrite go(ASTRewrite r, N n, TextEditGroup g, List<ASTNode> bss, List<ASTNode> crs);

  @Override public boolean prerequisite(@NotNull final N ¢) {
    return go(ASTRewrite.create(¢.getAST()), ¢, null, new ArrayList<>(), new ArrayList<>()) != null;
  }

  @NotNull
  @Override public final Tip tip(@NotNull final N n) {
    return new Tip(description(n), n, getClass()) {
      @Override @SuppressWarnings("boxing") public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final List<ASTNode> input = new ArrayList<>(), output = new ArrayList<>();
        MultipleReplaceCurrentNode.this.go(r, n, g, input, output);
        if (output.size() == 1)
          input.forEach(λ -> r.replace(λ, first(output), g));
        else if (input.size() == output.size())
          range.to(input.size()).forEach(λ -> r.replace(input.get(λ), output.get(λ), g));
      }
    };
  }
}