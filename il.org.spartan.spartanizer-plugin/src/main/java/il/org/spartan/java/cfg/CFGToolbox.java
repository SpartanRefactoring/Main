package il.org.spartan.java.cfg;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.java.cfg.builders.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public class CFGToolbox {
  @SuppressWarnings("unchecked") private final List<CFGBuilder<? extends ASTNode>>[] implementation = (List<CFGBuilder<? extends ASTNode>>[]) new List<?>[2
      * ASTNode.TYPE_METHOD_REFERENCE];

  public void initialize(final CFG<?> ¢) {
    add(¢, METHOD_DECLARATION, new MethodBuilder());
  }
  public static CFGToolbox all(final CFG<?> ¢) {
    final CFGToolbox $ = new CFGToolbox();
    $.initialize(¢);
    return $;
  }
  public boolean build(final ASTNode n) {
    return implementation[n.getNodeType()].stream().anyMatch(λ -> λ.accept(n));
  }
  public final boolean isRoot(final ASTNode n) {
    final List<CFGBuilder<? extends ASTNode>> bs = implementation[n.getNodeType()];
    return bs != null && !bs.isEmpty();
  }
  @SafeVarargs private final void add(final CFG<?> g, final int type, final CFGBuilder<? extends ASTNode>... ns) {
    Arrays.stream(ns).forEach(λ -> λ.register(g));
    if (implementation[type] == null)
      implementation[type] = an.empty.list();
    Collections.addAll(implementation[type], ns);
  }
}
