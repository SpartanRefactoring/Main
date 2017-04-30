package il.org.spartan.spartanizer.ast.navigate;
import static fluent.ly.idiomatic.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** A class to search in the ancestry line of a given node.
 * @author Yossi Gil
 * @author Dor Ma'ayan
 * @since 2015-08-22 */
public abstract class yieldAncestors<N extends ASTNode> {
  static class ByNodeClass<N extends ASTNode> extends yieldAncestors<N> {
    private final Class<N> clazz;

    ByNodeClass(final Class<N> clazz) {
      this.clazz = clazz;
    }

    @Override @SuppressWarnings("unchecked") public N from(final ASTNode ¢) {
      if (¢ != null)
        for (ASTNode $ = ¢.getParent(); $ != null; $ = $.getParent())
          if ($.getClass().equals(clazz) || clazz.isAssignableFrom($.getClass()))
            return (N) $;
      return null;
    }

    @Override public ASTNode inclusiveFrom(final ASTNode ¢) {
      return ¢ != null && (¢.getClass().equals(clazz) || clazz.isAssignableFrom(¢.getClass())) ? ¢ : from(¢);
    }
  }

  static class ByNodeInstances<N extends ASTNode> extends yieldAncestors<N> {
    private final List<N> instances;

    ByNodeInstances(final List<N> instances) {
      this.instances = instances;
    }

    @Override @SuppressWarnings("unchecked") public N from(final ASTNode ¢) {
      if (¢ != null)
        for (ASTNode $ = ¢.getParent(); $ != null; $ = $.getParent())
          if (instances.contains($))
            return (N) $;
      return null;
    }

    @Override public ASTNode inclusiveFrom(final ASTNode ¢) {
      return ¢ != null && instances.contains(¢) ? ¢ : from(¢);
    }
  }

  static class ByNodeType extends yieldAncestors<ASTNode> {
    final int type;

    ByNodeType(final int type) {
      this.type = type;
    }

    @Override public ASTNode from(final ASTNode ¢) {
      if (¢ != null)
        for (ASTNode $ = ¢.getParent(); $ != null; $ = $.getParent())
          if (type == $.getNodeType())
            return $;
      return null;
    }

    @Override public ASTNode inclusiveFrom(final ASTNode ¢) {
      return ¢ != null && type == ¢.getNodeType() ? ¢ : from(¢);
    }
  }

  public static class Until {
    final ASTNode until;

    Until(final ASTNode until) {
      this.until = until;
    }

    public Iterable<ASTNode> ancestors(final SimpleName n) {
      return () -> new Iterator<ASTNode>() {
        ASTNode next = n;

        @Override public boolean hasNext() {
          return next != null;
        }

        @Override public ASTNode next() {
          final ASTNode $ = next;
          next = eval(() -> next.getParent()).unless(next == until);
          return $;
        }
      };
    }
  }

  public static Until until(final ASTNode ¢) {
    return new Until(¢);
  }

  /** Factory method, returning an instance which can search by a node class
   * @param pattern JD
   * @return a newly created instance
   * @see ASTNode#getNodeType() */
  public static <N extends ASTNode> yieldAncestors<N> untilClass(final Class<N> ¢) {
    return new ByNodeClass<>(¢);
  }

  public static yieldAncestors<CompilationUnit> untilContainingCompilationUnit() {
    return new ByNodeClass<>(CompilationUnit.class);
  }

  public static yieldAncestors<MethodDeclaration> untilContainingMethod() {
    return new ByNodeClass<>(MethodDeclaration.class);
  }

  public static yieldAncestors<AbstractTypeDeclaration> untilContainingType() {
    return new ByNodeClass<>(AbstractTypeDeclaration.class);
  }

  public static yieldAncestors<Block> untilContainingBlock() {
    return new ByNodeClass<>(Block.class);
  }

  /** Factory method, returning an instance which can search by a node
   * instances.
   * @param pattern JD
   * @return a newly created instance */
  @SuppressWarnings({ "unchecked", "rawtypes" }) //
  public static <N extends ASTNode> yieldAncestors untilNode(final N... ¢) {
    return new ByNodeInstances<>(as.list(¢));
  }

  /** Factory method, returning an instance which can search by the integer
   * present on a node.
   * @param __ JD
   * @return a newly created instance
   * @see ASTNode#getNodeType() */
  public static yieldAncestors<ASTNode> untilNodeType(final int type) {
    return new ByNodeType(type);
  }

  /** Factory method, returning an instance which can search by a node
   * instances.
   * @param pattern JD
   * @return a newly created instance */
  public static <N extends ASTNode> yieldAncestors<N> untilOneOf(final List<N> ¢) {
    return new ByNodeInstances<>(¢);
  }

  /** @param n JD
   * @return closest ancestor whose __ matches the given __. */
  public abstract N from(ASTNode n);

  /** @param n JD
   * @return closest ancestor whose __ matches the given __. */
  public abstract ASTNode inclusiveFrom(ASTNode n);

  /** @param ¢ JD
   * @return furtherest ancestor whose __ matches the given __. */
  public ASTNode inclusiveLastFrom(final ASTNode ¢) {
    for (ASTNode $ = inclusiveFrom(¢), p = $;; $ = p, p = from(p.getParent()))
      if (p == null)
        return $;
  }

  /** @param n JD
   * @return furtherest ancestor whose __ matches the given __. */
  public ASTNode lastFrom(final ASTNode n) {
    // TODO Alex: Polish this loop manually and add a test case for future
    // generations
    ASTNode $ = from(n);
    for (ASTNode p = $; p != null; p = from(p))
      $ = p;
    return $;
  }
}