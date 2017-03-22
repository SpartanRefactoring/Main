package il.org.spartan.spartanizer.research.utils;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;

/** Tests of {@link research.util}
 * @author AnnaBel7
 * @author michalcohen
 * @since Nov 14, 2016 */
public class CleanerVisitorTest {
  @Nullable private static ASTNode createAST(@NotNull final String ¢) {
    return wizard.ast(¢);
  }

  private static Predicate<ASTNode> createImportPredicate() {
    return ImportDeclaration.class::isInstance;
  }

  private static Predicate<ASTNode> createJavadocPredicate() {
    return Javadoc.class::isInstance;
  }

  @NotNull @SuppressWarnings("static-method") private ASTNodeWrapper getChildren(@NotNull final Predicate<ASTNode> p, @NotNull final ASTNode n) {
    @NotNull final ASTNodeWrapper $ = new ASTNodeWrapper();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (p.test(¢))
          $.inner.add(¢);
      }
    });
    return $;
  }

  @Test public void test1() {
    new CleanerVisitor().visit((Javadoc) first(getChildren(createJavadocPredicate(), createAST("/**banana*/class f { }")).inner));
  }

  @Test public void test2() {
    new CleanerVisitor().visit((ImportDeclaration) first(getChildren(createImportPredicate(), createAST("import banana; class f { }")).inner));
  }

  private static class ASTNodeWrapper {
    @NotNull public final ArrayList<ASTNode> inner;

    ASTNodeWrapper() {
      inner = new ArrayList<>();
    }
  }
}
