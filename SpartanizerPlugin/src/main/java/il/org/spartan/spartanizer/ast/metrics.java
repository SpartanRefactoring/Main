package il.org.spartan.spartanizer.ast;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Use {@link Recurser} to measure things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06 */
public interface metrics {
  public static int condensedSize(final ASTNode ¢) {
    return wizard.condense(¢).length();
  }

  public static int size(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode n : ns)
      $ += metrics.nodesCount(n);
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

  /** Counts the number of non-space characters in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int countNonWhites(final ASTNode n) {
    return removeWhites(wizard.body(n)).length();
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

  /** Counts the number of statements in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int lineCount(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode child) {
        if (Statement.class.isAssignableFrom(child.getClass()))
          addWeight($, child);
      }

      /** @param a Accumulator
       * @param ¢ Node to check */
      void addWeight(final Int a, final ASTNode ¢) {
        if (iz.is(¢, BLOCK)) {
          if (extract.statements(¢).size() > 1)
            ++a.inner;
          return;
        }
        if (iz.is(¢, EMPTY_STATEMENT))
          return;
        if (iz.is(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, DO_STATEMENT)) {
          a.inner += 4;
          return;
        }
        if (!iz.is(¢, IF_STATEMENT))
          a.inner += 3;
        else {
          a.inner += 4;
          if (elze(az.ifStatement(¢)) != null)
            ++a.inner;
        }
      }
    });
    return $.inner;
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

  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int nodesCount(final ASTNode n) {
    class Integer {
      int inner = 0;
    }
    final Integer $ = new Integer();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        ++$.inner;
      }
    });
    return $.inner;
  }

  static int vocabulary(final ASTNode u) {
    return dictionary(u).size();
  }
}
