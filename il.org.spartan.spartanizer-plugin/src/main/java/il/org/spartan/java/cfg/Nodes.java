package il.org.spartan.java.cfg;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

public class Nodes {
  public final boolean contains(Object ¢) {
    return inner.contains(¢);
  }
  public final Iterator<ASTNode> iterator() {
    return inner.iterator();
  }
  public final boolean add(ASTNode ¢) {
    return inner.add(¢);
  }
  public final boolean addAll(Collection<? extends ASTNode> ¢) {
    return inner.addAll(¢);
  }
  public final boolean remove(ASTNode ¢) {
    return inner.remove(¢);
  }
  public Stream<ASTNode> stream() {
    return inner.stream();
  }
  public int size() {
    return inner.size();
  }
  public Set<ASTNode> asSet() {
    return inner;
  }
  public boolean isEmpty() {
    return inner.isEmpty();
  }
  public static Nodes empty() {
    return new Nodes();
  }
  @Override public String toString() {
    return inner + "";
  }

  private final Set<ASTNode> inner = new LinkedHashSet<>();

  public Nodes clear() {
    inner.clear();
    return this;
  }
}