package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;

/** A class to search in the descendants of a given node. Based on
 * {@link yieldAncestors}
 * @author Ori Marcovitch
 * @since 2016 */
public abstract class yieldDescendants<N extends ASTNode> {
  /** Factory method, returning an instance which can search by a node class
   * @param pattern JD
   * @return a newly created instance */
  @NotNull public static <N extends ASTNode> yieldDescendants<N> untilClass(final Class<N> ¢) {
    return new untilClass<>(¢);
  }

  /** @param n JD
   * @return descendants whose type matches the given type. */
  @NotNull public abstract List<N> from(ASTNode n);

  /** @param n JD
   * @return descendants whose type matches the given type. */
  @NotNull public abstract List<N> inclusiveFrom(ASTNode n);

  /** @param ¢ JD
   * @return add predicate to filter elements */
  @NotNull public abstract yieldDescendants<N> suchThat(Predicate<N> ¢);

  static class untilClass<N extends ASTNode> extends yieldDescendants<N> {
    final Class<N> clazz;
    Predicate<N> p = λ -> true;

    untilClass(final Class<N> clazz) {
      this.clazz = clazz;
    }

    @Override @NotNull public untilClass<N> suchThat(final Predicate<N> ¢) {
      p = ¢;
      return this;
    }

    @Override @NotNull public List<N> from(@NotNull final ASTNode ¢) {
      final List<N> $ = inclusiveFrom(¢);
      if ($.contains(¢))
        $.remove(¢);
      return $;
    }

    @Override @NotNull public List<N> inclusiveFrom(@NotNull final ASTNode n) {
      final List<N> $ = new ArrayList<>();
      n.accept(new ASTVisitor() {
        @Override public void preVisit(@NotNull final ASTNode ¢) {
          if (clazz.isAssignableFrom(¢.getClass()) && p.test(clazz.cast(¢)))
            $.add(clazz.cast(¢));
        }
      });
      return $;
    }
  }
}