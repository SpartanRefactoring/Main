package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;

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

  static Until until(final Predicate<ASTNode> ¢) {
    return new Until(¢);
  }

  // previous node / current node
  static Until until(final BiPredicate<ASTNode, ASTNode> ¢) {
    return new Until(¢);
  }

  static Until whil(final Predicate<ASTNode> ¢) {
    return new Until(λ -> !¢.test(λ));
  }

  class Until {
    final Predicate<ASTNode> predicate;
    final BiPredicate<ASTNode, ASTNode> bipredicate;

    Until(final Predicate<ASTNode> predicate) {
      this.predicate = predicate;
      bipredicate = null;
    }

    Until(final BiPredicate<ASTNode, ASTNode> bipredicate) {
      predicate = null;
      this.bipredicate = bipredicate;
    }

    public List<ASTNode> from(final ASTNode n) {
      final List<ASTNode> $ = an.empty.list();
      for (ASTNode current = n, previous = null; current != null && !test(previous, current); previous = current, current = current.getParent())
        $.add(current);
      return $;
    }

    private boolean test(final ASTNode previous, final ASTNode current) {
      return predicate != null ? predicate.test(current) : bipredicate != null && bipredicate.test(previous, current);
    }
  }
}
