package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** A class to search in the descendants of a given node. Based on
 * {@link yieldAncestors}
 * @author Ori Marcovitch
 * @since 2016 */
public abstract class yieldDescendants<N extends ASTNode> {
  /** Factory method, returning an instance which can search by a node class
   * @param pattern JD
   * @return a newly created instance */
  public static <N extends ASTNode> yieldDescendants<N> ofClass(final Class<N> ¢) {
    return new ofClass<>(¢);
  }

  /** @param n JD
   * @return descendants whose type matches the given type. */
  public abstract List<N> from(ASTNode n);

  /** @param n JD
   * @return descendants whose type matches the given type. */
  public abstract List<N> inclusiveFrom(ASTNode n);

  /** @param ¢ JD
   * @return add predicate to filter elements */
  public abstract yieldDescendants<N> suchThat(Predicate<N> ¢);

  static class ofClass<N extends ASTNode> extends yieldDescendants<N> {
    final Class<N> clazz;
    Predicate<N> p = λ -> true;

    ofClass(final Class<N> clazz) {
      this.clazz = clazz;
    }

    @Override public ofClass<N> suchThat(final Predicate<N> ¢) {
      p = ¢;
      return this;
    }

 @Override public List<N> from(final ASTNode n) {
      final List<N> $ = new ArrayList<>();
      n.accept(new ASTVisitor() {
        @Override public void preVisit(final ASTNode ¢) {
          if (n != ¢ && clazz.isAssignableFrom(¢.getClass()) && p.test(clazz.cast(¢)))
            $.add(clazz.cast(¢));
        }
      });
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