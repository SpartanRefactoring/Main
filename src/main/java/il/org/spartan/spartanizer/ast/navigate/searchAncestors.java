package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.idiomatic.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

// TODO Yossi Gil: review class, functionality added by Ori
/** A class to search in the ancestry line of a given node.
 * @author Yossi Gil
 * @since 2015-08-22 */
public abstract class searchAncestors {
  /** Factory method, returning an instance which can search by a node class
   * @param pattern JD
   * @return a newly created instance
   * @see ASTNode#getNodeType() */
  public static <N extends ASTNode> searchAncestors forClass(final Class<N> ¢) {
    return new ByNodeClass(¢);
  }

  /** Factory method, returning an instance which can search by the integer
   * present on a node.
   * @param type JD
   * @return a newly created instance
   * @see ASTNode#getNodeType() */
  public static searchAncestors forType(final int type) {
    return new ByNodeType(type);
  }

  /** Factory method, returning an instance which can search by a node
   * instances.
   * @param pattern JD
   * @return a newly created instance */
  public static <N extends ASTNode> searchAncestors specificallyFor(final List<N> ¢) {
    return new ByNodeInstances<>(¢);
  }

  /** Factory method, returning an instance which can search by a node
   * instances.
   * @param pattern JD
   * @return a newly created instance */
  @SuppressWarnings({ "unchecked", "rawtypes" }) //
  public static <N extends ASTNode> searchAncestors specificallyFor(final N... ¢) {
    return new ByNodeInstances(as.list(¢));
  }

  public static Until until(final ASTNode ¢) {
    return new Until(¢);
  }

  /** @param n JD
   * @return closest ancestor whose type matches the given type. */
  public abstract ASTNode from(final ASTNode n);

  /** @param n JD
   * @return closest ancestor whose type matches the given type. */
  public abstract ASTNode inclusiveFrom(final ASTNode n);

  /** XXX: This is a bug of auto-laconize [[SuppressWarningsSpartan]]
   * @param ¢ JD
   * @return furtherest ancestor whose type matches the given type. */
  public ASTNode inclusiveLastFrom(final ASTNode ¢) {
    ASTNode $ = inclusiveFrom(¢);
    for (ASTNode p = $;; p = from(p.getParent())) {
      if (p == null)
        return $;
      $ = p;
    }
  }

  /** @param n JD
   * @return furtherest ancestor whose type matches the given type. */
  public ASTNode lastFrom(final ASTNode n) {
    ASTNode $ = from(n);
    // TODO: Alex and Dan - fix this empty loop, created by buggy tipper.
    for (ASTNode p = $; p != null; p = from(p), $ = p)
      ;
    return $;
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

  static class ByNodeClass extends searchAncestors {
    private final Class<? extends ASTNode> clazz;

    public ByNodeClass(final Class<? extends ASTNode> clazz) {
      this.clazz = clazz;
    }

    @Override public ASTNode from(final ASTNode ¢) {
      if (¢ != null)
        for (ASTNode $ = ¢; $ != null; $ = $.getParent())
          if ($.getClass().equals(clazz) || clazz.isAssignableFrom($.getClass()))
            return $;
      return null;
    }

    @Override public ASTNode inclusiveFrom(final ASTNode ¢) {
      return ¢ != null && (¢.getClass().equals(clazz) || clazz.isAssignableFrom(¢.getClass())) ? ¢ : from(¢);
    }
  }

  static class ByNodeInstances<N extends ASTNode> extends searchAncestors {
    private final List<N> instances;

    public ByNodeInstances(final List<N> instances) {
      this.instances = instances;
    }

    @Override public ASTNode from(final ASTNode ¢) {
      if (¢ != null)
        for (ASTNode $ = ¢.getParent(); $ != null; $ = $.getParent())
          if (instances.contains($))
            return $;
      return null;
    }

    @Override public ASTNode inclusiveFrom(final ASTNode ¢) {
      return ¢ != null && instances.contains(¢) ? ¢ : from(¢);
    }
  }

  static class ByNodeType extends searchAncestors {
    final int type;

    public ByNodeType(final int type) {
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
}