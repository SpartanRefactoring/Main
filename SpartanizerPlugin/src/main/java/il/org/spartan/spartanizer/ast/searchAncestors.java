package il.org.spartan.spartanizer.ast;

import static il.org.spartan.idiomatic.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** A class to search in the ancestry line of a given node.
 * @author Yossi Gil
 * @since 2015-08-22 */
public abstract class searchAncestors {
  /** Factory method, returning an instance which can search by a node class
   * @param n JD
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

  public static Until until(final ASTNode ¢) {
    return new Until(¢);
  }

  /** @param n JD
   * @return closest ancestor whose type matches the given type. */
  public abstract ASTNode from(final ASTNode n);
  
  // TODO Yossi: please confirm, written by Ori, can replace/be merged with from (see
  // lastFrom below)
  /** @param n JD
   * @return closest ancestor whose type matches the given type. */
  public abstract ASTNode inclusiveFrom(final ASTNode n);
  
  // TODO Yossi: default implementation using from function, please confirm, written by Ori
  /** @param n JD
   * @return furtherest ancestor whose type matches the given type. */
  public ASTNode lastFrom(final ASTNode n) {
    ASTNode $ = from(n);
    for (ASTNode p = $ ; p != null ; p = from(p))
      $ = p;
    return $;
  }
  
  // TODO Yossi: default implementation using from function, please confirm, written by Ori
  /** @param n JD
   * @return furtherest ancestor whose type matches the given type. */
  public ASTNode inclusiveLastFrom(final ASTNode n) {
    ASTNode $ = inclusiveFrom(n);
    for (ASTNode p = $ ; p != null ; p = from(p))
      $ = p;
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

    // TODO Yossi: replaced
    // $.getClass().isInstance(clazz) -> clazz.isAssignableFrom($.getClass()))
    // please confirm, changed by Ori
    @Override public ASTNode from(final ASTNode ¢) {
      if (¢ != null)
        for (ASTNode $ = ¢.getParent(); $ != null; $ = $.getParent())
          if ($.getClass().equals(clazz) || clazz.isAssignableFrom($.getClass()))
            return $;
      return null;
    }

    @Override public ASTNode inclusiveFrom(ASTNode n) {
      return n != null && (n.getClass().equals(clazz) || clazz.isAssignableFrom(n.getClass())) ? n : from(n);
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

    @Override public ASTNode inclusiveFrom(ASTNode n) {
      return n != null && type == n.getNodeType() ? n : from(n);
    }
  }
}