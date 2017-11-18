package il.org.spartan.java.cfg;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

/** A class which describe and allows basic operations over Nodes of the CFG
 * @author Ori Roth
 * @author Dor Ma'ayan
 * @since 2017-11-12 */
class Nodes {
  public static Nodes empty() {
    return new Nodes();
  }

  private final Set<ASTNode> inner = new LinkedHashSet<>();

  public final boolean add(ASTNode ¢) {
    return inner.add(¢);
  }
  public final boolean addAll(Collection<? extends ASTNode> ¢) {
    return inner.addAll(¢);
  }
  public Set<ASTNode> asSet() {
    return inner;
  }
  public Nodes clear() {
    inner.clear();
    return this;
  }
  public final boolean contains(Object ¢) {
    return inner.contains(¢);
  }
  public boolean isEmpty() {
    return inner.isEmpty();
  }
  public final Iterator<ASTNode> iterator() {
    return inner.iterator();
  }
  public final boolean remove(ASTNode ¢) {
    return inner.remove(¢);
  }
  public int size() {
    return inner.size();
  }
  public Stream<ASTNode> stream() {
    return inner.stream();
  }
  @Override public String toString() {
    return inner + "";
  }
}