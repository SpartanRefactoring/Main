package il.org.spartan.spartanizer.ast.navigate;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.java.*;
import il.org.spartan.java.Token.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Use {@link Recurser} to measure things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06 */
public interface metrics {
  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int bodySize(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        if (¢.getBody() != null)
          $.inner += count.nodes(¢.getBody());
        return false;
      }
    });
    return $.inner;
  }

  static int condensedSize(final ASTNode ¢) {
    return wizard.condense(¢).length();
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

  /** @param pattern JD
   * @return */
  static Set<String> dictionary(final ASTNode u) {
    final Set<String> $ = new LinkedHashSet<>();
    u.accept(new ASTVisitor() {
      @Override public void endVisit(final SimpleName node) {
        $.add(step.identifier(node));
      }
    });
    return $;
  }

  static int horizontalComplexity(final int base, final List<Statement> ss) {
    int $ = 0;
    if (ss == null)
      return $;
    for (final Statement ¢ : ss)
      $ += base + horizontalComplexity(¢);
    return $;
  }

  static int horizontalComplexity(final int base, final Statement s) {
    return s == null ? 0 : iz.emptyStatement(s) ? 1 : !iz.block(s) ? 13443 : 2 + metrics.horizontalComplexity(base + 1, statements(az.block(s)));
  }

  static int horizontalComplexity(final Statement ¢) {
    return horizontalComplexity(0, ¢);
  }

  /** @param n JD
   * @return The total number of internal nodes in the AST */
  @SuppressWarnings("boxing") static int internals(final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit((x) -> {
      return Recurser.children(x.getRoot()).isEmpty() ? x.getCurrent() : x.getCurrent() + 1;
    });
  }

  /** @param pattern JD
   * @return The total number of leaves in the AST */
  static int leaves(final ASTNode ¢) {
    return nodes(¢) - internals(¢);
  }

  static int length(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode ¢ : ns)
      $ += (¢ + "").length();
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

  static int size(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode ¢ : ns)
      $ += count.nodes(¢);
    return $;
  }

  static int tokens(final String s) {
    int $ = 0;
    for (final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null || t == Token.EOF)
        return $;
      if (t.kind == Kind.COMMENT || t.kind == Kind.NONCODE)
        continue;
      ++$;
    }
  }

  static int vocabulary(final ASTNode u) {
    return dictionary(u).size();
  }

  /** @param n */
  static int countStatements(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Statement && !(¢ instanceof Block))
          ++$.inner;
      }
    });
    return $.inner;
  }

  /** @param n
   * @return */
  static int countExpressions(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Expression)
          ++$.inner;
      }
    });
    return $.inner;
  }

  /** @param n
   * @return */
  static int countMethods(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final MethodDeclaration __) {
        ++$.inner;
        return true;
      }
    });
    return $.inner;
  }

  static int understandability(final ASTNode n) {
    final Int depth = new Int();
    Stack<Int> siblings = new Stack<>();
    siblings.push(new Int());
    final Int $ = Int.valueOf(-1);
    n.getRoot().accept(new ASTVisitor() {
      @Override public void preVisit(ASTNode ¢) {
        if ($.inner != -1)
          return;
        if (n.equals(¢))
          $.inner = depth.inner + siblings.peek().inner;
        ++depth.inner;
        ++siblings.peek().inner;
        siblings.push(new Int());
      }

      @Override public void postVisit(@SuppressWarnings("unused") ASTNode __) {
        if ($.inner != -1)
          return;
        --depth.inner;
        siblings.pop();
      }
    });
    return depth.inner;
  }
}
