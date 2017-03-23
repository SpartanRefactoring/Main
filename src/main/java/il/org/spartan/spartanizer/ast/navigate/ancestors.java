package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

/** {@link Iterable} over the ancestors of a given node.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-12-23 */
public interface ancestors {
  @NotNull static List<ASTNode> path(@NotNull final ASTNode n) {
    @NotNull final List<ASTNode> $ = new ArrayList<>();
    for (ASTNode parent = n; parent != null; parent = n.getParent())
      $.add(parent);
    Collections.reverse($);
    return $;
  }

  static Iterable<ASTNode> of(final ASTNode n) {
    return () -> new Iterator<ASTNode>() {
      ASTNode next = n;

      @Override public boolean hasNext() {
        return next != null;
      }

      @Override public ASTNode next() {
        final ASTNode $ = next;
        next = parent($);
        return $;
      }
    };
  }
}
