package il.org.spartan.refactoring.ast;

import static il.org.spartan.idiomatic.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** A class to search in the ancestry line of a given node.
 * @author Yossi Gil
 * @since 2015-08-22 */
public abstract class searchAncestors {
  /** Factory method, returning an instance which can search by a node class
   * @param c JD
   * @return a newly created instance
   * @see ASTNode#getNodeType() */
  public static <N extends ASTNode> searchAncestors forClass(final Class<N> c) {
    return new ByNodeClass(c);
  }

  /** Factory method, returning an instance which can search by the integer
   * present on a node.
   * @param type JD
   * @return a newly created instance
   * @see ASTNode#getNodeType() */
  public static searchAncestors forType(final int type) {
    return new ByNodeType(type);
  }

  public static Until until(final ASTNode n) {
    return new Until(n);
  }

  /** @param n JD
   * @return closest ancestor whose type matches the given type. */
  public abstract ASTNode from(final ASTNode n);

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

        @Override public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  static class ByNodeClass extends searchAncestors {
    private final Class<? extends ASTNode> clazz;

    public ByNodeClass(final Class<? extends ASTNode> clazz) {
      this.clazz = clazz;
    }

    @Override public ASTNode from(final ASTNode n) {
      if (n != null)
        for (ASTNode $ = n.getParent(); $ != null; $ = $.getParent())
          if ($.getClass().equals(clazz) || $.getClass().isInstance(clazz))
            return $;
      return null;
    }
  }

  static class ByNodeType extends searchAncestors {
    final int type;

    public ByNodeType(final int type) {
      this.type = type;
    }

    @Override public ASTNode from(final ASTNode n) {
      if (n != null)
        for (ASTNode $ = n.getParent(); $ != null; $ = $.getParent())
          if (type == $.getNodeType())
            return $;
      return null;
    }
  }
}