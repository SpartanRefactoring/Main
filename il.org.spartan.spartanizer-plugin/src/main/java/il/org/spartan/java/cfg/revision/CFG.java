package il.org.spartan.java.cfg.revision;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;

/** The main class of the CFG implementation
 * @author Dor Ma'ayan & Ori Roth
 * @since 2017-06-14 */
public abstract class CFG {
  /** Compute the CFG for a given root
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
  public abstract List<ASTNode> in(ASTNode n);
  public abstract List<ASTNode> out(ASTNode n);
}
