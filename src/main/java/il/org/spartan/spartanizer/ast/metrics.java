package il.org.spartan.spartanizer.ast;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Use {@link Recurser} to measure things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06 */
public interface metrics {
  static int count(final ASTNode root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor() {
      @SuppressWarnings("unused") @Override public void preVisit(final ASTNode ¢) {
        ++$.inner;
      }
    });
    return $.inner;
  }

  /** @param n JD
   * @return The total number of distinct kind of nodes in the AST */
  @SuppressWarnings("boxing") static int dexterity(final ASTNode n) {
    if (n == null)
      return 0;
    final Recurser<Integer> recurse = new Recurser<>(n, 0);
    final Set<Integer> nodesTypeSet = new HashSet<>();
    return recurse.preVisit((x) -> {
      if (nodesTypeSet.contains(x.getRoot().getNodeType()))
        return x.getCurrent();
      nodesTypeSet.add(x.getRoot().getNodeType());
      return x.getCurrent() + 1;
    });
  }

  /** @param n JD
   * @return The total number of internal nodes in the AST */
  @SuppressWarnings("boxing") static int internals(final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit((x) -> {
      return Recurser.children(x.getRoot()).isEmpty() ? x.getCurrent() : x.getCurrent() + 1;
    });
  }

  /** @param n JD
   * @return The total number of leaves in the AST */
  static int leaves(final ASTNode ¢) {
    return nodes(¢) - internals(¢);
  }

  /** @param n JD
   * @return The total number of nodes in the AST */
  @SuppressWarnings("boxing") static int nodes(final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit((x) -> (1 + x.getCurrent()));
  }

  /** @param n JD
   * @return */
  static Set<String> dictionary(final ASTNode u) {
    final Set<String> $ = new LinkedHashSet<>();
    u.accept(new ASTVisitor() {
      @Override public void endVisit(SimpleName node) {
        $.add(node.getIdentifier());
      }
    });
    return $;
  }

  static int vocabulary(final ASTNode u) {
    return dictionary(u).size();
  }

  static Set<String> literals(ASTNode n) {
    final Set<String> $ = new LinkedHashSet<>();
    n.accept(new ASTVisitor() {
      @Override public void endVisit(NumberLiteral node) {
        $.add(node.getToken());
      }

      @Override public void endVisit(BooleanLiteral node) {
        $.add(node + "");
      }
      @Override public void endVisit(NullLiteral node) {
        $.add(node + "");
      }
      @Override public void endVisit(StringLiteral node) {
        $.add(node.getLiteralValue());
      }
    });
    return $;
  }

  static int literacy(ASTNode ¢) {
    return literals(¢).size();
  }
}
