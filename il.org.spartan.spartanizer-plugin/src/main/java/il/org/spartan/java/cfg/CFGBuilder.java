package il.org.spartan.java.cfg;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public abstract class CFGBuilder<N extends ASTNode> {
  private CFG<?> cfg;
  protected ASTNode root;

  public abstract void build(N n);
  @SuppressWarnings("unchecked") public boolean accept(final ASTNode ¢) {
    if (¢ == null)
      return false;
    root = ¢;
    build((N) ¢);
    return true;
  }
  public void register(final CFG<?> ¢) {
    this.cfg = ¢;
  }
  protected List<ASTNode> in(final ASTNode ¢) {
    return cfg.in(¢);
  }
  protected List<ASTNode> out(final ASTNode ¢) {
    return cfg.out(¢);
  }
}
