package il.org.spartan.java.cfg;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The main class of the CFG implementation
 * @author Dor Ma'ayan & Ori Roth
 * @since 2017-06-14 */
public abstract class CFG {
  public static final String keyIn = "in";
  public static final String keyOut = "out";

  public static class Nodes {
    public final boolean contains(Object ¢) {
      return inner.contains(¢);
    }
    public final Iterator<ASTNode> iterator() {
      return inner.iterator();
    }
    public final boolean add(ASTNode ¢) {
      return inner.add(¢);
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

    private final Set<ASTNode> inner = an.empty.set();
  }

  public static class InNodes extends Nodes {}

  public static class OutNodes extends Nodes {}

  public static Nodes in(ASTNode ¢) {
    if (!property.has(¢, keyIn))
      fill(¢);
    return property.get(¢, keyIn);
  }
  public static Nodes out(ASTNode ¢) {
    if (!property.has(¢, keyOut))
      fill(¢);
    return property.get(¢, keyOut);
  }
  private static void fill(ASTNode n) {
    BodyDeclaration root = containing.bodyDeclaration(n);
    if (root != null)
      traverse(root);
  }
  private static void traverse(BodyDeclaration root) {
    computeCFG(root);
  }
  private static void computeCFG(BodyDeclaration root) {
    compute.cfg(root);
  }
}
