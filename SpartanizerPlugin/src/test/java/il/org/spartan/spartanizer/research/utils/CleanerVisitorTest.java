package il.org.spartan.spartanizer.research.utils;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;

/** Tests of {@link research.util}
 * @author AnnaBel7
 * @author michalcohen
 * @since Nov 14, 2016 */
public class CleanerVisitorTest {
  private class ASTNodeWrapper {
    public LinkedList<ASTNode> inner;

    public ASTNodeWrapper() {
      inner = new LinkedList<>();
    }
  }

  private static ASTNode createAST(final String ¢) {
    return wizard.ast(¢);
  }

  static Predicate<ASTNode> createImportPredicate() {
    return (p) -> p instanceof ImportDeclaration;
  }

  static Predicate<ASTNode> createJavadocPredicate() {
    return (p) -> p instanceof Javadoc;
  }

  private ASTNodeWrapper getChildren(final Predicate<ASTNode> p, final ASTNode n) {
    final ASTNodeWrapper $ = new ASTNodeWrapper();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (p.test(¢))
          $.inner.add(¢);
      }
    });
    return $;
  }

  @Test public void test1() {
    new CleanerVisitor().visit((Javadoc) getChildren(createJavadocPredicate(), createAST("/**banana*/class f { }")).inner.get(0));
  }

  @Test public void test2() {
    new CleanerVisitor().visit((ImportDeclaration) getChildren(createImportPredicate(), createAST("import banana; class f { }")).inner.get(0));
  }
}
