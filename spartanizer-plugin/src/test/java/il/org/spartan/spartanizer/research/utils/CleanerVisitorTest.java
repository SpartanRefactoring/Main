package il.org.spartan.spartanizer.research.utils;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.junit.Test;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.research.util.CleanerVisitor;

/** Tests of {@link research.util}
 * @author AnnaBel7
 * @author michalcohen
 * @since Nov 14, 2016 */
public class CleanerVisitorTest {
  private static ASTNode createAST(final String ¢) {
    return make.ast(¢);
  }
  private static Predicate<ASTNode> createImportPredicate() {
    return ImportDeclaration.class::isInstance;
  }
  private static Predicate<ASTNode> createJavadocPredicate() {
    return Javadoc.class::isInstance;
  }
  @SuppressWarnings("static-method") private ASTNodeWrapper getChildren(final Predicate<ASTNode> p, final ASTNode n) {
    final ASTNodeWrapper $ = new ASTNodeWrapper();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (p.test(¢))
          $.inner.add(¢);
      }
    });
    return $;
  }
  @Test public void test1() {
    new CleanerVisitor().visit((Javadoc) the.firstOf(getChildren(createJavadocPredicate(), createAST("/**banana*/class f { }")).inner));
  }
  @Test public void test2() {
    new CleanerVisitor().visit((ImportDeclaration) the.firstOf(getChildren(createImportPredicate(), createAST("import banana; class f { }")).inner));
  }

  private static class ASTNodeWrapper {
    public final List<ASTNode> inner;

    ASTNodeWrapper() {
      inner = an.empty.list();
    }
  }
}
