package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.concurrent.atomic.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** Various metrics, which can be written fluent API style with this type's name
 * prefix.
 * @author Yossi Gil
 * @since 2016 */
public interface count {
  static int imports(final CompilationUnit u) {
    final AtomicInteger $ = new AtomicInteger();
    u.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getClass().equals(ImportDeclaration.class))
          $.getAndIncrement();
      }
    });
    return $.get();
  }
  static int lines(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode child) {
        if (Statement.class.isAssignableFrom(child.getClass()))
          addWeight($, child);
      }
      /** @param a Accumulator
       * @param ¢ Node to check */
      void addWeight(final Int a, final ASTNode ¢) {
        if (iz.nodeTypeEquals(¢, BLOCK)) {
          if (extract.statements(¢).size() > 1)
            ++a.inner;
        } else if (!iz.nodeTypeEquals(¢, EMPTY_STATEMENT))
          if (iz.nodeTypeIn(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, DO_STATEMENT))
            a.inner += 4;
          else if (!iz.nodeTypeEquals(¢, IF_STATEMENT))
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
  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int nodes(final ASTNode root) {
    final AtomicInteger $ = new AtomicInteger();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        $.getAndIncrement();
      }
    });
    return $.get();
  }
  static int nodesOfClass(final ASTNode n, final Class<? extends ASTNode> cl) {
    final AtomicInteger $ = new AtomicInteger();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getClass().equals(cl))
          $.getAndIncrement();
      }
    });
    return $.get();
  }
  static int noimports(final CompilationUnit root) {
    final AtomicInteger $ = new AtomicInteger();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (!¢.getClass().equals(ImportDeclaration.class))
          $.getAndIncrement();
      }
    });
    return $.get();
  }
  /** Exclude comments and import package statement from the content.
   * @param root
   * @return */
  static int noImportsNoComments(final ASTNode root) {
    final AtomicInteger $ = new AtomicInteger();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (!¢.getClass().equals(ImportDeclaration.class) || !¢.getClass().equals(Comment.class))
          $.getAndIncrement();
      }
    });
    return $.get();
  }
  /** Counts the number of non-space characters in a tree rooted at a given node
   * @param pattern JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int nonWhiteCharacters(final ASTNode ¢) {
    return removeWhites(wizard.body(¢)).length();
  }
  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int statements(final ASTNode root) {
    final AtomicInteger $ = new AtomicInteger();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        $.addAndGet(as.bit(iz.statement(¢)));
      }
    });
    return $.get();
  }
}