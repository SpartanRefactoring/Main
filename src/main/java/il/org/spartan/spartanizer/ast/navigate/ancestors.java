package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** {@link Iterable} over the ancestors of a given node.
 * @author Yossi Gil
 * @since 2016-12-23 */
public interface ancestors {
  static List<ASTNode> path(final ASTNode n) {
    final List<ASTNode> $ = an.empty.list();
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
