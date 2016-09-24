package il.org.spartan.spartanizer.ast;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.java.*;
import il.org.spartan.java.Token.*;
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
          $.inner += nodesCount(¢.getBody());
        return false;
      }
    });
    return $.inner;
  }

  static int condensedSize(final ASTNode ¢) {
    return wizard.condense(¢).length();
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

  static int countImports(final CompilationUnit u) {
    final Int $ = new Int();
    u.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getClass().equals(ImportDeclaration.class))
          ++$.inner;
      }
    });
    return $.inner;
  }

  static int countNoImport(final CompilationUnit root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (!¢.getClass().equals(ImportDeclaration.class))
          ++$.inner;
      }
    });
    return $.inner;
  }

  /** Exclude comments and import package statement from the content.
   * @param root
   * @return */
  static int countNoImportNoComments(final ASTNode root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        // System.out.println(¢.getClass().toString());
        // System.out.println(¢.IMPORT_DECLARATION);
        if (!¢.getClass().equals(ImportDeclaration.class) || !¢.getClass().equals(Comment.class))
          ++$.inner;
      }
    });
    return $.inner;
  }

  /** Counts the number of non-space characters in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int countNonWhites(final ASTNode ¢) {
    return removeWhites(wizard.body(¢)).length();
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
        $.add(step.identifier(node));
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
    for (final ASTNode ¢ : ns)
      $ += (¢ + "").length();
    return $;
  }

  /** Counts the number of sideEffects in a tree rooted at a given node
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
        } else if (!iz.is(¢, EMPTY_STATEMENT))
          if (iz.is(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, DO_STATEMENT))
            a.inner += 4;
          else if (!iz.is(¢, IF_STATEMENT))
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
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        ++$.inner;
      }
    });
    return $.inner;
  }

  static int size(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode ¢ : ns)
      $ += metrics.nodesCount(¢);
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

  static int horizontalComplexity(Object object) {
    return 0;
  }
}
