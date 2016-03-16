package il.org.spartan.refactoring.utils;

import java.util.Iterator;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * An {@link Iterable} yielding all ancestors of a given node.
 *
 * @author Yossi Gil
 * @date 2015-09-21
 */
public class Ancestors implements Iterable<ASTNode> {
  final ASTNode from;
  /**
   * Instantiates this class
   *
   * @param from start iteration from this node
   */
  public Ancestors(final ASTNode from) {
    this.from = from;
  }
  @Override public Iterator<ASTNode> iterator() {
    return new Iterator<ASTNode>() {
      ASTNode current = from;
      @Override public boolean hasNext() {
        return current != null;
      }
      @Override public ASTNode next() {
        final ASTNode $ = current;
        current = current.getParent();
        return $;
      }
      @Override public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
