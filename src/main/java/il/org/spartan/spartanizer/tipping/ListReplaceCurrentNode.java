package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.engine.*;

/** Used to replace a node with multiple replacements, taking its place in the
 * parent node children list. This class uses {@link ListRewrite} for the
 * replacement operations, so implementations may use
 * {@link ASTRewrite#createCopyTarget} and {@link ASTRewrite#createMoveTarget}
 * in order to preserve original code formatting.
 * @author Ori Roth
 * @since 2016 */
public abstract class ListReplaceCurrentNode<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 9100870643887091404L;

  @Nullable public abstract List<ASTNode> go(ASTRewrite r, N n, TextEditGroup g);

  /** @return child list property descriptor of the parent of the node we are
   *         replacing */
  public abstract ChildListPropertyDescriptor listDescriptor(N n);

  @Override public boolean prerequisite(@NotNull final N ¢) {
    return ¢.getParent() != null && go(ASTRewrite.create(¢.getAST()), ¢, null) != null;
  }

  @Nullable @Override public final Fragment tip(@NotNull final N n) {
    @NotNull @SuppressWarnings("unchecked") final Class<? extends ListReplaceCurrentNode<N>> class1 = (Class<? extends ListReplaceCurrentNode<N>>) getClass();
    return new Fragment(description(n), n, class1) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(n.getParent(), listDescriptor(n));
        ListReplaceCurrentNode.this.go(r, n, g).forEach(λ -> l.insertBefore(λ, n, g));
        l.remove(n, g);
      }
    };
  }
}
