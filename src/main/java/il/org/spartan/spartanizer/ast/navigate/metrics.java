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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Use {@link Recurser} to measure things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06 */
public interface metrics {
  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int bodySize(@NotNull final ASTNode n) {
    @NotNull final Int $ = new Int();
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(@NotNull final MethodDeclaration ¢) {
        if (¢.getBody() != null)
          $.inner += count.nodes(¢.getBody());
        return false;
      }
    });
    return $.inner;
  }

  static int condensedSize(final ASTNode ¢) {
    return trivia.condense(¢).length();
  }

  /** @param n JD
   * @return The total number of distinct kind of nodes in the AST */
  @SuppressWarnings("boxing") static int dexterity(@Nullable final ASTNode n) {
    if (n == null)
      return 0;
    @NotNull final Recurser<Integer> $ = new Recurser<>(n, 0);
    @NotNull final Collection<Integer> nodesTypeSet = new HashSet<>();
    return $.preVisit(λ -> {
      if (nodesTypeSet.contains(λ.getRoot().getNodeType()))
        return λ.getCurrent();
      nodesTypeSet.add(λ.getRoot().getNodeType());
      return λ.getCurrent() + 1;
    });
  }

  /** @param pattern JD
   * @return */
  @NotNull
  static Set<String> dictionary(@NotNull final ASTNode u) {
    @NotNull final Set<String> $ = new LinkedHashSet<>();
    u.accept(new ASTVisitor(true) {
      @Override public void endVisit(final SimpleName node) {
        $.add(step.identifier(node));
      }
    });
    return $;
  }

  @SuppressWarnings("boxing") static int horizontalComplexity(final int base, @Nullable final List<Statement> ss) {
    return ss == null ? 0 : ss.stream().map(λ -> base + horizontalComplexity(λ)).reduce((x, y) -> x + y).get();
  }

  static int horizontalComplexity(final int base, @Nullable final Statement s) {
    return s == null ? 0 : iz.emptyStatement(s) ? 1 : !iz.block(s) ? 13443 : 2 + metrics.horizontalComplexity(base + 1, statements(az.block(s)));
  }

  static int horizontalComplexity(final Statement ¢) {
    return horizontalComplexity(0, ¢);
  }

  static int height(final ASTNode ¢) {
    return 1 + height(Recurser.allChildren(¢));
  }

  static int height(@NotNull final List<ASTNode> ns) {
    @NotNull final Int $ = new Int();
    ns.forEach(λ -> $.inner = Integer.max($.inner, height(λ)));
    return $.inner;
  }

  static int height(@NotNull final List<Statement> ss, @SuppressWarnings("unused") final int x) {
    @NotNull final Int $ = new Int();
    ss.forEach(λ -> $.inner = Integer.max($.inner, height(λ)));
    return $.inner;
  }

  /** @param n JD
   * @return The total number of internal nodes in the AST */
  @SuppressWarnings("boxing") static int internals(@Nullable final ASTNode n) {
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

  static int literacy(@NotNull final ASTNode ¢) {
    return literals(¢).size();
  }

  @NotNull
  static Set<String> literals(@NotNull final ASTNode n) {
    @NotNull final Set<String> $ = new LinkedHashSet<>();
    n.accept(new ASTVisitor(true) {
      @Override public void endVisit(final BooleanLiteral node) {
        $.add(node + "");
      }

      @Override public void endVisit(final NullLiteral node) {
        $.add(node + "");
      }

      @Override public void endVisit(@NotNull final NumberLiteral node) {
        $.add(node.getToken());
      }

      @Override public void endVisit(@NotNull final StringLiteral node) {
        $.add(node.getLiteralValue());
      }
    });
    return $;
  }

  /** @param n JD
   * @return The total number of nodes in the AST */
  @SuppressWarnings("boxing") static int nodes(@Nullable final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit(λ -> (1 + λ.getCurrent()));
  }

  @SuppressWarnings("boxing") static int nodes(@NotNull final List<Statement> ss) {
    return ss.stream().map(metrics::nodes).reduce((x, y) -> x + y).get();
  }

  @SuppressWarnings("boxing") static int size(final ASTNode... ns) {
    return Stream.of(ns).map(count::nodes).reduce((x, y) -> x + y).get();
  }

  static int tokens(@NotNull final String s) {
    int $ = 0;
    for (@NotNull final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null || t == Token.EOF)
        return $;
      if (t.kind != Kind.COMMENT && t.kind != Kind.NONCODE)
        ++$;
    }
  }

  static int vocabulary(@NotNull final ASTNode u) {
    return dictionary(u).size();
  }

  static int countStatements(@NotNull final ASTNode n) {
    @NotNull final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Statement && !(¢ instanceof Block))
          $.step();
      }
    });
    return $.inner;
  }

  @SuppressWarnings("boxing") static int countStatements(@NotNull final List<Statement> ss) {
    return ss.stream().map(metrics::countStatements).reduce((x, y) -> x + y).get();
  }

  static int countStatementsOfType(@NotNull final Statement s, final int type) {
    @NotNull final Int $ = new Int();
    s.accept(new ASTVisitor(true) {
      @Override public void preVisit(@NotNull final ASTNode ¢) {
        if (¢.getNodeType() == type)
          $.step();
      }
    });
    return $.get();
  }

  @SuppressWarnings("boxing") static int countStatementsOfType(@NotNull final List<Statement> ss, final int type) {
    return ss.stream().map(λ -> countStatementsOfType(λ, type)).reduce((x, y) -> x + y).get();
  }

  static int countExpressions(@NotNull final ASTNode n) {
    @NotNull final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Expression)
          $.step();
      }
    });
    return $.inner;
  }

  static int countMethods(@NotNull final ASTNode n) {
    @NotNull final Int $ = new Int();
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
  static int nodeUnderstandability(@NotNull final ASTNode n) {
    @NotNull final Int depth = new Int();
    @NotNull final Stack<Int> siblings = new Stack<>();
    siblings.push(new Int());
    @NotNull final Int $ = Int.valueOf(-1);
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
  static int subtreeUnderstandability(@NotNull final ASTNode n) {
    @NotNull final Int depth = new Int();
    @NotNull final Stack<Int> siblings = new Stack<>();
    siblings.push(new Int());
    @NotNull final Int $ = new Int();
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
  static int subtreeUnderstandability2(@NotNull final ASTNode n) {
    @NotNull final Int depth = new Int();
    @NotNull final Stack<Int> variables = new Stack<>();
    variables.push(new Int());
    @NotNull final Int $ = new Int();
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
