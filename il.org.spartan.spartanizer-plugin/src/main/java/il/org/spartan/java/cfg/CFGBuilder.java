package il.org.spartan.java.cfg;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
@SuppressWarnings("hiding")
public abstract class CFGBuilder<N extends ASTNode> {
  private CFG<?> cfg;
  protected ASTNode root;

  public abstract void build(N n);
  @SuppressWarnings("unchecked") public boolean accept(final ASTNode n) {
    if (n == null)
      return false;
    root = n;
    build((N) n);
    return true;
  }
  public void register(final CFG<?> cfg) {
    this.cfg = cfg;
  }
  protected List<ASTNode> in(final ASTNode n) {
    return cfg.in(n);
  }
  protected List<ASTNode> out(final ASTNode n) {
    return cfg.out(n);
  }
}
