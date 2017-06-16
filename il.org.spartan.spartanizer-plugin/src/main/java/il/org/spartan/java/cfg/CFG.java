package il.org.spartan.java.cfg;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-14 */
public abstract class CFG<T extends CFG<T>> implements Selfie<T> {
  CFGBuilders builders;

  public T register(final CFGBuilders ¢) {
    builders = ¢;
    return self();
  }
  public void compute(final ASTNode root) {
    if (root != null)
      root.accept(new ASTVisitor() {
        @Override public boolean preVisit2(final ASTNode ¢) {
          if (!builders.isRoot(¢))
            return true;
          acknowledgeRoot(¢);
          builders.build(¢);
          return false;
        }
      });
  }
  public abstract List<ASTNode> in(ASTNode n);
  public abstract List<ASTNode> out(ASTNode n);
  public abstract void acknowledgeRoot(ASTNode n);
}
