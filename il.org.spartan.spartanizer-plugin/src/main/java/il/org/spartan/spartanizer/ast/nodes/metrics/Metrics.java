package il.org.spartan.spartanizer.ast.nodes.metrics;

import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.java.*;
import il.org.spartan.java.Token.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

public class Metrics{

  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  public static int bodySize(final ASTNode n) {
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
  public static int condensedSize(final ASTNode ¢) {
    return Trivia.condense(¢).length();
  }


  public static int countExpressions(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Expression)
          $.step();
      }
    });
    return $.inner;
  }


  public static int countMethods(final ASTNode n) {
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

  public static int countStatements(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Statement && !(¢ instanceof Block))
          $.step();
      }
    });
    return $.inner;
  }

  @SuppressWarnings("boxing")
  public static int countStatements(final List<Statement> ss) {
    return ss.stream().map(Metrics::countStatements).reduce((x, y) -> x + y).get();
  }

  public static int countStatementsOfType(final List<Statement> ss, final int type) {
    return ss.stream().mapToInt(λ -> countStatementsOfType(λ, type)).sum();
  }

  public static int countStatementsOfType(final Statement s, final int type) {
    final Int $ = new Int();
    s.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getNodeType() == type)
          $.step();
      }
    });
    return $.get();
  }


  public static int depth(final ASTNode ¢) {
    ¢.accept(new ASTVisitor() {
      @Override public void postVisit(final ASTNode n) {
        if (n != null && n.getParent() != null && n != ¢)
          property.setInt(n.getParent(),"Depth", Math.max(property.getInt(n.getParent(),"Depth"), property.getInt(n,"Depth") + 1));
      }
      @Override public void preVisit(@SuppressWarnings("hiding") final ASTNode ¢) {
        if (¢ != null)
          ¢.setProperty("Depth", Integer.valueOf(1));
      }
    });
    return (Integer) ¢.getProperty("Depth");
  }

  public static int depth(final List<Statement> ss) {
    final Int $ = new Int();
    ss.forEach(λ -> $.inner = Integer.max($.inner, depth(λ)));
    return $.inner;
  }

  public static int height(final ASTNode ¢) {
    return 1 + height(Recurser.allChildren(¢));
  }

  public static int height(final List<ASTNode> ns) {
    final Int $ = new Int();
    ns.forEach(λ -> $.inner = Integer.max($.inner, height(λ)));
    return $.inner;
  }

  public static int height(final List<Statement> ss, @SuppressWarnings("unused") final int x) {
    final Int $ = new Int();
    ss.forEach(λ -> $.inner = Integer.max($.inner, height(λ)));
    return $.inner;
  }

  public static int horizontalComplexity(final int base, final List<Statement> ss) {
    return ss == null ? 0 : ss.stream().mapToInt(λ -> base + horizontalComplexity(λ)).sum();
  }

  public static int horizontalComplexity(final int base, final Statement s) {
    return s == null ? 0 : iz.emptyStatement(s) ? 1 : !iz.block(s) ? 13443 : 2 + horizontalComplexity(base + 1, step.statements(az.block(s)));
  }

  public static int horizontalComplexity(final Statement ¢) {
    return horizontalComplexity(0, ¢);
  }

  /** @param n JD
   * @return The total number of internal nodes in the AST */
  @SuppressWarnings("boxing")
  public static int internals(final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit(λ -> Recurser.children(λ.getRoot()).isEmpty() ? λ.getCurrent() : λ.getCurrent() + 1);
  }

  /** @param pattern JD
   * @return The total number of leaves in the AST */
  public static int leaves(final ASTNode ¢) {
    return nodes(¢) - internals(¢);
  }

  public static int length(final ASTNode... ns) {
    return Arrays.stream(ns).mapToInt(λ -> (λ + "").length()).reduce((x, y) -> x + y).orElse(0);
  }

  public static int length(final ASTNode ¢) {
    return (¢ + "").length();
  }

  public static int literacy(final ASTNode ¢) {
    return literals(¢).size();
  }


  public static Set<String> literals(final ASTNode n) {
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
  @SuppressWarnings("boxing")
  public static int nodes(final ASTNode n) {
    return n == null ? 0 : new Recurser<>(n, 0).preVisit(λ -> (1 + λ.getCurrent()));
  }

  public static int nodes(final List<Statement> ¢) {
    return ¢.stream().mapToInt(Metrics::nodes).sum();
  }

  /** measures metrics from root to node
   * @param n
   * @return */
  public static int nodeUnderstandability(final ASTNode n) {
    final Int depth = new Int();
    final Stack<Int> siblings = new Stack<>();
    siblings.push(new Int());
    final Int $ = Int.valueOf(-1);
    n.getRoot().accept(new ASTVisitor(true) {
      @Override public void postVisit(@SuppressWarnings("unused") final ASTNode __) {
        if ($.inner != -1)
          return;
        --depth.inner;
        siblings.pop();
      }
      @Override public void preVisit(final ASTNode ¢) {
        if ($.inner != -1)
          return;
        if (n.equals(¢))
          $.inner = depth.inner + siblings.peek().inner;
        ++depth.inner;
        ++siblings.peek().inner;
        siblings.push(new Int());
      }
    });
    return $.inner;
  }
  @SuppressWarnings("boxing")
  public static int size(final ASTNode... ns) {
    return Stream.of(ns).map(countOf::nodes).reduce((x, y) -> x + y).get();
  }

  /** measure the total U in the subtree
   * @param n
   * @return */
  public static int subtreeUnderstandability(final ASTNode n) {
    final Int depth = new Int();
    final Stack<Int> siblings = new Stack<>();
    siblings.push(new Int());
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void postVisit(@SuppressWarnings("unused") final ASTNode __) {
        --depth.inner;
        siblings.pop();
      }
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        $.inner += depth.inner + siblings.peek().inner;
        ++depth.inner;
        ++siblings.peek().inner;
        siblings.push(new Int());
      }
    });
    return $.inner;
  }

  /** measure the total U in the subtree
   * @param n
   * @return */
  public static int subtreeUnderstandability2(final ASTNode n) {
    final Int depth = new Int();
    final Stack<Int> variables = new Stack<>();
    variables.push(new Int());
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void postVisit(final ASTNode ¢) {
        unlessBlockDecreaseDepth(¢);
        ifNotVariableCloseScope(¢);
      }
      @Override public void preVisit(final ASTNode ¢) {
        ifStatementIncreaseResult(¢);
        unlessBlockIncreaseDepth(¢);
        ifVariableCount(¢);
        ifNotVariableOpenScope(¢);
      }
      void ifNotVariableCloseScope(final ASTNode ¢) {
        if (!iz.isVariableDeclarationStatement(¢))
          variables.pop(); // variables
      }
      void ifNotVariableOpenScope(final ASTNode ¢) {
        if (!iz.isVariableDeclarationStatement(¢))
          variables.push(new Int());
      }
      void ifStatementIncreaseResult(final ASTNode ¢) {
        if (iz.statement(¢) && !iz.block(¢))
          increaseCostBy(depth.inner + variables.peek().inner);
      }
      void ifVariableCount(final ASTNode ¢) {
        if (iz.variableDeclarationFragment(¢) || iz.singleVariableDeclaration(¢))
          ++variables.peek().inner;
      }
      void increaseCostBy(final int a) {
        $.inner += a;
      }
      void unlessBlockDecreaseDepth(final ASTNode ¢) {
        if (!iz.block(¢))
          --depth.inner; // depth
      }
      void unlessBlockIncreaseDepth(final ASTNode ¢) {
        if (!iz.block(¢))
          ++depth.inner;
      }
    });
    return $.inner;
  }
public static int tokens(final ASTNode ¢) {
  return tokens(¢);
  }


  public static int tokens(final String s) {
    int $ = 0;
    for (final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null || t == Token.EOF)
        return $;
      if (t.kind != Kind.COMMENT && t.kind != Kind.NONCODE)
        ++$;
    }
  }

  public static int vocabulary(final ASTNode u) {
    return Metric.dictionary(u).size();
  }

  /** @param n JD
   * @return The total number of distinct kind of nodes in the AST */
  @SuppressWarnings("boxing")
  public static int dexterity(final ASTNode n) {
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
  public static int essence(ASTNode λ) {
    return Essence.of(λ + "").length();
  }
  public static int statements(ASTNode λ) {
    return !iz.methodDeclaration(λ) ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size();
  }
  public static int tide(ASTNode λ) {
    return clean(λ + "").length();
  }
}