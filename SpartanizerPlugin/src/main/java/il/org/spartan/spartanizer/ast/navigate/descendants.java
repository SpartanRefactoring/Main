package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A class to search in the descendants of a given node. Based on
 * {@link yieldAncestors}
 * @author Ori Marcovitch
 * @since 2016 */
public abstract class descendants<N extends ASTNode> {
  /** Factory method, returning an instance which can search by a node class
   * @param pattern JD
   * @return a newly created instance */
  @NotNull
  public static <N extends ASTNode> descendants<N> whoseClassIs(final Class<N> ¢) {
    return new whoseClassIs<>(¢);
  }

  /** @param n JD
   * @return descendants whose type matches the given type. */
  @NotNull
  public abstract List<N> from(ASTNode n);

  /** @param n JD
   * @return descendants whose type matches the given type. */
  @NotNull
  public abstract List<N> inclusiveFrom(ASTNode n);

  /** @param ¢ JD
   * @return add predicate to filter elements */
  @NotNull
  public abstract descendants<N> suchThat(Predicate<N> ¢);

  static class whoseClassIs<N extends ASTNode> extends descendants<N> {
    final Class<N> clazz;
    Predicate<N> p = λ -> true;

    whoseClassIs(final Class<N> clazz) {
      this.clazz = clazz;
    }

    @NotNull
    @Override public whoseClassIs<N> suchThat(final Predicate<N> ¢) {
      p = ¢;
      return this;
    }

    @NotNull
    @Override public List<N> from(@NotNull final ASTNode n) {
      @NotNull final List<N> $ = new ArrayList<>();
      n.accept(new ASTVisitor(true) {
        @Override public void preVisit(@NotNull final ASTNode ¢) {
          if (n != ¢ && clazz.isAssignableFrom(¢.getClass()) && p.test(clazz.cast(¢)))
            $.add(clazz.cast(¢));
        }
      });
      return $;
    }

    @NotNull
    @Override public List<N> inclusiveFrom(@Nullable final ASTNode n) {
      @NotNull final List<N> $ = new ArrayList<>();
      if (n == null)
        return $;
      n.accept(new ASTVisitor(true) {
        @Override public void preVisit(@NotNull final ASTNode ¢) {
          if (clazz.isAssignableFrom(¢.getClass()) && p.test(clazz.cast(¢)))
            $.add(clazz.cast(¢));
        }
      });
      return $;
    }
  }

  @NotNull
  public static List<ASTNode> of(@NotNull final ASTNode n) {
    @NotNull final List<ASTNode> $ = new ArrayList<>();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        $.add(¢);
      }
    });
    return $;
  }

  public static Stream<ASTNode> streamOf(@NotNull final ASTNode ¢) {
    return descendants.of(¢).stream();
  }
}