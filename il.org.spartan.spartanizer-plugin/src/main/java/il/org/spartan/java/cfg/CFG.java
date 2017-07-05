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
  public class Nodes {
    public final boolean contains(Object o) {
      return inner.contains(o);
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

    private final Set<ASTNode> inner = an.empty.set();
  }

  public class InNodes extends Nodes {}

  public class OutNodes extends Nodes {}

  /** Compute the CFG for a given root /** Compute the CFG for a given root
   * @param root method declaration */
  public void compute(final MethodDeclaration root) {
    if (root != null)
      root.accept(new ASTVisitor() {
        @Override public boolean visit(final MethodDeclaration ¢) {
          MethodBuilder.of(CFG.this, ¢);
          return true;
        }
      });
  }
  public static Nodes in(ASTNode n) {
    if (property.obtain(Nodes.class).from(n) == null)
      fill(n);
    return property.obtain(Nodes.class).from(n);
  }
  public static Nodes out(ASTNode n) {
    if (property.obtain(Nodes.class).from(n) == null)
      fill(n);
    return property.obtain(Nodes.class).from(n);
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
