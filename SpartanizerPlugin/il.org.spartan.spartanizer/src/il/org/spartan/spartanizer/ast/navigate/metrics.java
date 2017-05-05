package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.java.*;
import il.org.spartan.java.Token.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;

/** Use {@link Recurser} to measure things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06 */
public interface metrics {
  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int bodySize(final ASTNode n) {
    final Int $ = new Int();
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration ¢) {
        if (¢.getBody() != null)
          $.inner += countOf.nodes(¢.getBody());
        return false;
      }
    });
    return $.inner;
  }

  static int condensedSize(final ASTNode ¢) {
    return Trivia.condense(¢).length();
  }

  /** @param n JD
   * @return The total number of distinct kind of nodes in the AST */
  @SuppressWarnings("boxing") static int dexterity(final ASTNode n) {
    if (n == null)
      return 0;
    final Recurser<Integer> $ = new Recurser<>(n, 0);
    final Collection<Integer> nodesTypeSet = new HashSet<>();
    return $.preVisit(λ -> {
      if (nodesTypeSet.contains(λ.getRoot().getNodeType()))
        return λ.getCurrent();
      nodesTypeSet.add(λ.getRoot().getNodeType());
      return λ.getCurrent() + 1;
    });
  }

  /** @param pattern JD
   * @return */
  static Set<String> dictionary(final ASTNode u) {
    final Set<String> $ = new LinkedHashSet<>();
    u.accept(new ASTVisitor(true) {
      @Override public void endVisit(final SimpleName node) {
        $.add(step.identifier(node));
      }
    });
    return $;
  }

  @SuppressWarnings("boxing") static int horizontalComplexity(final int base, final List<Statement> ss) {
    return ss == null ? 0 : ss.stream().map(λ -> base + horizontalComplexity(λ)).reduce((x, y) -> x + y).get();
  }

  static int horizontalComplexity(final int base, final Statement s) {
    return s == null ? 0 : iz.emptyStatement(s) ? 1 : !iz.block(s) ? 13443 : 2 + metrics.horizontalComplexity(base + 1, statements(az.block(s)));
  }

  static int horizontalComplexity(final Statement ¢) {
    return horizontalComplexity(0, ¢);
  }

  static int height(final ASTNode ¢) {
    return 1 + height(Recurser.allChildren(¢));
  }

  static int height(final List<ASTNode> ns) {
    final Int $ = new Int();
    ns.forEach(λ -> $.inner = Integer.max($.inner, height(λ)));
    return $.inner;
  }

  static int height(final List<Statement> ss, @SuppressWarnings("unused") final int x) {
    final Int $ = new Int();
    ss.forEach(λ -> $.inner = Integer.max($.inner, height(λ)));
    return $.inner;
  }

  static int depth(final List<Statement> ss) {
    final Int $ = new Int();
    ss.forEach(λ -> $.inner = Integer.max($.inner, depth(λ)));
    return $.inner;
  }

  @SuppressWarnings("boxing") static int depth(final ASTNode ¢) {
    ¢.accept(new ASTVisitor() {
      @Override public void preVisit(@SuppressWarnings("hiding") final ASTNode ¢) {
        if (¢ != null)
          ¢.setProperty("Depth", Integer.valueOf(1));
      }

      @Override public void postVisit(final ASTNode n) {
        if (n != null && n.getParent() != null && n != ¢)
          n.getParent().setProperty("Depth", Integer.max((Integer) n.getParent().getProperty("Depth"), (Integer) n.getProperty("Depth") + 1));
      }
    });
    return (Integer) ¢.getProperty("Depth");
  }

  /** @param n JD
   * @return The total number of internal nodes in the AST */
  @SuppressWarnings("boxing") static int internals(final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit(λ -> Recurser.children(λ.getRoot()).isEmpty() ? λ.getCurrent() : λ.getCurrent() + 1);
  }

  /** @param pattern JD
   * @return The total number of leaves in the AST */
  static int leaves(final ASTNode ¢) {
    return nodes(¢) - internals(¢);
  }

  static int length(final ASTNode... ns) {
    return Arrays.stream(ns).mapToInt(λ -> (λ + "").length()).reduce((x, y) -> x + y).orElse(0);
  }

  static int literacy(final ASTNode ¢) {
    return literals(¢).size();
  }

  static Set<String> literals(final ASTNode n) {
    final Set<String> $ = new LinkedHashSet<>();
    n.accept(new ASTVisitor(true) {
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
    return n == null ? 0 : new Recurser<>(n, 0).preVisit(λ -> (1 + λ.getCurrent()));
  }

  @SuppressWarnings("boxing") static int nodes(final List<Statement> ss) {
    return ss.stream().map(metrics::nodes).reduce((x, y) -> x + y).get();
  }

  @SuppressWarnings("boxing") static int size(final ASTNode... ns) {
    return Stream.of(ns).map(countOf::nodes).reduce((x, y) -> x + y).get();
  }

  static int tokens(final String s) {
    int $ = 0;
    for (final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null || t == Token.EOF)
        return $;
      if (t.kind != Kind.COMMENT && t.kind != Kind.NONCODE)
        ++$;
    }
  }

  static int vocabulary(final ASTNode u) {
    return dictionary(u).size();
  }

  static int countStatements(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Statement && !(¢ instanceof Block))
          $.step();
      }
    });
    return $.inner;
  }

  @SuppressWarnings("boxing") static int countStatements(final List<Statement> ss) {
    return ss.stream().map(metrics::countStatements).reduce((x, y) -> x + y).get();
  }

  static int countStatementsOfType(final Statement s, final int type) {
    final Int $ = new Int();
    s.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getNodeType() == type)
          $.step();
      }
    });
    return $.get();
  }

  @SuppressWarnings("boxing") static int countStatementsOfType(final List<Statement> ss, final int type) {
    return ss.stream().map(λ -> countStatementsOfType(λ, type)).reduce((x, y) -> x + y).get();
  }

  static int countExpressions(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Expression)
          $.step();
      }
    });
    return $.inner;
  }

  static int countMethods(final ASTNode n) {
    final Int $ = new Int();
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(@SuppressWarnings("unused") final MethodDeclaration __) {
        $.step();
        return true;
      }
    });
    return $.inner;
  }

  /** measures metrics from root to node
   * @param n
   * @return */
  static int nodeUnderstandability(final ASTNode n) {
    final Int depth = new Int();
    final Stack<Int> siblings = new Stack<>();
    siblings.push(new Int());
    final Int $ = Int.valueOf(-1);
    n.getRoot().accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if ($.inner != -1)
          return;
        if (n.equals(¢))
          $.inner = depth.inner + siblings.peek().inner;
        ++depth.inner;
        ++siblings.peek().inner;
        siblings.push(new Int());
      }

      @Override public void postVisit(@SuppressWarnings("unused") final ASTNode __) {
        if ($.inner != -1)
          return;
        --depth.inner;
        siblings.pop();
      }
    });
    return $.inner;
  }

  /** measure the total U in the subtree
   * @param n
   * @return */
  static int subtreeUnderstandability(final ASTNode n) {
    final Int depth = new Int();
    final Stack<Int> siblings = new Stack<>();
    siblings.push(new Int());
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        $.inner += depth.inner + siblings.peek().inner;
        ++depth.inner;
        ++siblings.peek().inner;
        siblings.push(new Int());
      }

      @Override public void postVisit(@SuppressWarnings("unused") final ASTNode __) {
        --depth.inner;
        siblings.pop();
      }
    });
    return $.inner;
  }

  /** measure the total U in the subtree
   * @param n
   * @return */
  static int subtreeUnderstandability2(final ASTNode n) {
    final Int depth = new Int();
    final Stack<Int> variables = new Stack<>();
    variables.push(new Int());
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        ifStatementIncreaseResult(¢);
        unlessBlockIncreaseDepth(¢);
        ifVariableCount(¢);
        ifNotVariableOpenScope(¢);
      }

      @Override public void postVisit(final ASTNode ¢) {
        unlessBlockDecreaseDepth(¢);
        ifNotVariableCloseScope(¢);
      }

      void ifNotVariableOpenScope(final ASTNode ¢) {
        if (!iz.isVariableDeclarationStatement(¢))
          variables.push(new Int());
      }

      void ifVariableCount(final ASTNode ¢) {
        if (iz.variableDeclarationFragment(¢) || iz.singleVariableDeclaration(¢))
          ++variables.peek().inner;
      }

      void unlessBlockIncreaseDepth(final ASTNode ¢) {
        if (!iz.block(¢))
          ++depth.inner;
      }

      void ifStatementIncreaseResult(final ASTNode ¢) {
        if (iz.statement(¢) && !iz.block(¢))
          increaseCostBy(depth.inner + variables.peek().inner);
      }

      void increaseCostBy(final int a) {
        $.inner += a;
      }

      void unlessBlockDecreaseDepth(final ASTNode ¢) {
        if (!iz.block(¢))
          --depth.inner; // depth
      }

      void ifNotVariableCloseScope(final ASTNode ¢) {
        if (!iz.isVariableDeclarationStatement(¢))
          variables.pop(); // variables
      }
    });
    return $.inner;
  }

  static int length(final ASTNode ¢) {
    return (¢ + "").length();
  }
}
