package il.org.spartan.java.cfg;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-14 */
public abstract class CFG<T extends CFG<T>> implements Selfie<T> {
  CFGBuilders builders;

  public T register(final CFGBuilders bs) {
    builders = bs;
    return self();
  }
  public void compute(final ASTNode root) {
    if (root == null)
      ;
    else
      root.accept(new ASTVisitor() {
        @Override public boolean preVisit2(ASTNode n) {
          if (!builders.isRoot(n))
            return true;
          acknowledgeRoot(n);
          builders.build(n);
          return false;
        }
      });
  }
  public abstract List<ASTNode> in(final ASTNode n);
  public abstract List<ASTNode> out(final ASTNode n);
  public abstract void acknowledgeRoot(final ASTNode n);
}
