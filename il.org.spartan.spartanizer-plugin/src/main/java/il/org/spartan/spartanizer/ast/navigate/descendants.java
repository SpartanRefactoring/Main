package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

/** A class to search in the descendants of a given node. Based on
 * {@link yieldAncestors}
 * @author Ori Marcovitch
 * @since 2016 */
public abstract class descendants<N extends ASTNode> {
  /** Factory method, returning an instance which can search by a node class
   * @param pattern JD
   * @return a newly created instance */
  public static <N extends ASTNode> descendants<N> whoseClassIs(final Class<N> ¢) {
    return new whoseClassIs<>(¢);
  }
  /** @param n JD
   * @return descendants whose __ matches the given __. */
  public abstract List<N> from(ASTNode n);
  /** @param n JD
   * @return descendants whose __ matches the given __. */
  public abstract List<N> inclusiveFrom(ASTNode n);
  /** @param ¢ JD
   * @return add predicate to filter elements */
  public abstract descendants<N> suchThat(Predicate<N> ¢);
  public static boolean containsClassInstanceCreation(final Statement nextStatement) {
    return !descendants.whoseClassIs(ClassInstanceCreation.class).from(nextStatement).isEmpty();
  }
  public static boolean containsLambda(final Statement nextStatement) {
    return !descendants.whoseClassIs(LambdaExpression.class).from(nextStatement).isEmpty();
  }

  static class whoseClassIs<N extends ASTNode> extends descendants<N> {
    final Class<N> clazz;
    Predicate<N> p = λ -> true;

    whoseClassIs(final Class<N> clazz) {
      this.clazz = clazz;
    }
    @Override public whoseClassIs<N> suchThat(final Predicate<N> ¢) {
      p = ¢;
      return this;
    }
    @Override public List<N> from(final ASTNode n) {
      final List<N> $ = an.empty.list();
      n.accept(new ASTVisitor(true) {
        @Override public void preVisit(final ASTNode ¢) {
          if (n != ¢ && clazz.isAssignableFrom(¢.getClass()) && p.test(clazz.cast(¢)))
            $.add(clazz.cast(¢));
        }
      });
      return $;
    }
    @Override public List<N> inclusiveFrom(final ASTNode n) {
      final List<N> $ = an.empty.list();
      if (n == null)
        return $;
      n.accept(new ASTVisitor(true) {
        @Override public void preVisit(final ASTNode ¢) {
          if (clazz.isAssignableFrom(¢.getClass()) && p.test(clazz.cast(¢)))
            $.add(clazz.cast(¢));
        }
      });
      return $;
    }
  }

  public static List<ASTNode> of(final ASTNode n) {
    final List<ASTNode> $ = an.empty.list();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        $.add(¢);
      }
    });
    return $;
  }
  public static Stream<ASTNode> streamOf(final ASTNode ¢) {
    return descendants.of(¢).stream();
  }
}