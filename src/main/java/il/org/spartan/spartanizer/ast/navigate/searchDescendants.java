package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

/** A class to search in the descendants of a given node. Based on
 * {@link searchAncestors}
 * @author Ori Marcovitch
 * @since 2016 */
public abstract class searchDescendants<N extends ASTNode> {
  /** Factory method, returning an instance which can search by a node class
   * @param pattern JD
   * @return a newly created instance */
  public static <N extends ASTNode> searchDescendants<N> forClass(final Class<N> ¢) {
    return new ByNodeClass<>(¢);
  }
  /** @param n JD
   * @return descendants whose type matches the given type. */
  public abstract List<N> from(final ASTNode n);
  /** @param n JD
   * @return descendants whose type matches the given type. */
  public abstract List<N> inclusiveFrom(final ASTNode n);
  /** @param ¢ JD
   * @return add predicate to filter elements */
  public abstract searchDescendants<N> suchThat(final Predicate<N> ¢);

  static class ByNodeClass<N extends ASTNode> extends searchDescendants<N> {
    final Class<N> clazz;
    Predicate<N> p = (x) -> true;

    public ByNodeClass(final Class<N> clazz) {
      this.clazz = clazz;
    }
    @Override public ByNodeClass<N> suchThat(final Predicate<N> ¢) {
      p = ¢;
      return this;
    }
    @Override public List<N> from(final ASTNode ¢) {
      final List<N> $ = inclusiveFrom(¢);
      if ($.contains(¢))
        $.remove(¢);
      return $;
    }
    @Override public List<N> inclusiveFrom(final ASTNode n) {
      final List<N> $ = new ArrayList<>();
      n.accept(new ASTVisitor() {
        @Override public void preVisit(final ASTNode ¢) {
          if (clazz.isAssignableFrom(¢.getClass()) && p.test(clazz.cast(¢)))
            $.add(clazz.cast(¢));
        }
      });
      return $;
    }
  }
}