package il.org.spartan.spartanizer.ast;

import static il.org.spartan.spartanizer.engine.ExpressionComparator.*;

import java.lang.Integer;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Use {@link Recurser} to measure things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06 */
public interface metrics {
  public static int size(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode n : ns)
      $ += nodesCount(n);
    return $;
  }

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
   * @return */
  static Set<String> dictionary(final ASTNode u) {
    final Set<String> $ = new LinkedHashSet<>();
    u.accept(new ASTVisitor() {
      @Override public void endVisit(final SimpleName node) {
        $.add(node.getIdentifier());
      }
    });
    return $;
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

  static int length(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode n : ns)
      $ += (n + "").length();
    return $;
  }

  static int literacy(final ASTNode ¢) {
    return literals(¢).size();
  }

  static Set<String> literals(final ASTNode n) {
    final Set<String> $ = new LinkedHashSet<>();
    n.accept(new ASTVisitor() {
      @Override public void endVisit(final BooleanLiteral node) {
        $.add(node + "");
      }

      @Override public void endVisit(final NullLiteral node) {
        $.add(node + "");
      }

      @Override public void endVisit(final NumberLiteral node) {
        $.add(node.getToken());
      }

      @Override public void endVisit(final StringLiteral node) {
        $.add(node.getLiteralValue());
      }
    });
    return $;
  }

  /** @param n JD
   * @return The total number of nodes in the AST */
  @SuppressWarnings("boxing") static int nodes(final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit((x) -> (1 + x.getCurrent()));
  }

  static int vocabulary(final ASTNode u) {
    return dictionary(u).size();
  }
}
