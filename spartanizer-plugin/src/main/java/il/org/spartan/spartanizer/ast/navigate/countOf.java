package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.removeWhites;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static org.eclipse.jdt.core.dom.ASTNode.BLOCK;
import static org.eclipse.jdt.core.dom.ASTNode.DO_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EMPTY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.ENHANCED_FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.IF_STATEMENT;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.utils.Int;

/** Various metrics, which can be written fluent API style with this __'s name
 * prefix.
 * @author Yossi Gil
 * @since 2016 */
public interface countOf {
  static int imports(final CompilationUnit u) {
    final Int $ = new Int();
    u.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getClass().equals(ImportDeclaration.class))
          $.step();
      }
    });
    return $.get();
  }
  static int lines(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
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
   * @param nodeTypeHolder JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int nodes(final ASTNode root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor(true) {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        $.step();
      }
    });
    return $.get();
  }
  static int nodesOfClass(final ASTNode n, final Class<? extends ASTNode> c) {
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getClass().equals(c))
          $.step();
      }
    });
    return $.get();
  }
  static int noimports(final CompilationUnit root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (!¢.getClass().equals(ImportDeclaration.class))
          $.step();
      }
    });
    return $.get();
  }
  /** Exclude comments and import package statement from the content.
   * @param root
   */
  static int noImportsNoComments(final ASTNode root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (!¢.getClass().equals(ImportDeclaration.class) || !¢.getClass().equals(Comment.class))
          $.step();
      }
    });
    return $.get();
  }
  
  /** Counts the number of non-space characters in a tree rooted at a given node
   * @param pattern JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int nonWhiteCharacters(final ASTNode ¢) {
    return removeWhites(Trivia.cleanForm(¢)).length();
  }
  
  
  static int specialCharacters(final ASTNode ¢) {
    String res = removeWhites(Trivia.cleanForm(¢));
    res = res.replaceAll("[a-zA-Z0-9]", "");
    return res.length();
  }
  
  
  
  /** Counts the number of nodes in a tree rooted at a given node
   * @param nodeTypeHolder JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  static int statements(final ASTNode root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        $.add(as.bit(iz.statement(¢)));
      }
    });
    return $.get();
  }
}