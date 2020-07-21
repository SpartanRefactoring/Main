package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;

/** Test for our test classes and methods pruner
 * @author Matteo Orrù
 * @since 2016 */
@SuppressWarnings("static-method")
public class SpartanizerTest {
  private static int nMethods;

  public static void main(final String[] args) {
    final ASTNode u = makeAST.COMPILATION_UNIT.from("package test;import static il.org.spartan.plugin.demos.Inline.*;"
        + "import fluent.ly.*;import static fluent.ly.azzert.*;import org.junit.*;public class Test{"
        + " @forget(\"comment\") @Test public void testMethod(){int i = 1;assert (i>0);} }");
    assert u != null;
    // noinspection SameReturnValue
    u.accept(new ASTVisitor(true) {
      boolean hasTestAnnotation(final MethodDeclaration d) {
        final List<?> $ = modifiers(d);
        return $.stream().anyMatch(λ -> λ instanceof MarkerAnnotation && (λ + "").contains("@Test"));
      }
      @Override public boolean visit(final AnnotationTypeDeclaration node) {
        print("node.getName().getIdentifier(): " + node.getName().getIdentifier());
        return true;// super.visit(node);
      }
      @Override public boolean visit(final AnnotationTypeMemberDeclaration node) {
        print(AnnotationTypeMemberDeclaration.class + ": " + node.getName());
        return super.visit(node);
      }
      @Override public boolean visit(final Assignment node) {
        print(node.getOperator());
        return super.visit(node);
      }
      @Override public boolean visit(final ImportDeclaration node) {
        print(ImportDeclaration.class + ": " + node.getName());
        return super.visit(node);
      }
      @Override public boolean visit(final MarkerAnnotation node) {
        print("MarkerAnnotation: " + node.getTypeName());
        print("parent: " + node.getParent().getNodeType());
        return super.visit(node);
      }
      @Override public boolean visit(final MethodDeclaration node) {
        print(MethodDeclaration.class + ": " + node.getName());
        return !hasTestAnnotation(node);
      }
      @Override public boolean visit(final MethodInvocation node) {
        print(MethodInvocation.class + ": " + node.getName());
        return super.visit(node);
      }
      @Override public boolean visit(final NormalAnnotation node) {
        print("NormalAnnotation: " + node.getTypeName());
        return super.visit(node);
      }
      @Override public boolean visit(final PackageDeclaration node) {
        print(PackageDeclaration.class + ": " + node.getName());
        return super.visit(node);
      }
    });
  }
  static void print(final Object ¢) {
    forget.em(new Object[] { ¢ });
  }

  String method = "";
  private final String test1 = "package test;import static il.org.spartan.plugin.demos.Inline.*;"
      + "import fluent.ly.*;import static fluent.ly.azzert.*;import org.junit.*;public class Test{"
      + " @forget(\"comment\") @Test public void aTestMethod(){int i = 1;assert (i>0);} "
      + " public void notATestMethod(){int i = 1;assert (i>0);} }";
  private final String test2 = "package test;import static il.org.spartan.plugin.demos.Inline.*;"
      + "import fluent.ly.*;import static fluent.ly.azzert.*;import org.junit.*;public class Test{"
      + " @forget(\"comment\") @Test public void aTestMethod(){int i = 1;assert (i>0);} "
      + " public void notATestMethod(){int i = 1;assert (i>0);} public void ASecondNotTestMethod(){ int i = 1;assert (i>0);} }";

  boolean countMethods() {
    ++nMethods;
    return false;
  }
  @Test public void testFileName_01() {
    assert "fooTest.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_02() {
    assert "test.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_03() {
    assert "Test.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_04() {
    assert "Testfoo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_05() {
    assert "testfoo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_06() {
    assert "foo1Testfoo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_07() {
    assert "foo1testfoo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_08() {
    assert "test_foo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_09() {
    assert "foo1_Test_foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_10() {
    assert "foo1_test_foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_11() {
    assert "test-foo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_12() {
    assert "foo1-Test-foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testFileName_13() {
    assert "foo1-test-foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }
  @Test public void testMethodWithAnnotation_01() {
    final ASTNode u1 = makeAST.COMPILATION_UNIT.from(test1), u2 = makeAST.COMPILATION_UNIT.from(test2);
    assert u1 != null;
    assert u2 != null;
    visitASTNode(u1);
    assert nMethods == 1;
    visitASTNode(u2);
    assert nMethods == 3;
  }
  @Test public void testSpartanizerCheckMethod_01() {
    print(test1);
    final ASTNode u = makeAST.COMPILATION_UNIT.from(test2);
    print(u.getClass());
    assert u != null;
  }
  @Test public void testSpartanizerCheckMethod_02() {
    print(test1);
    final ASTNode u = makeAST.COMPILATION_UNIT.from(test2);
    assert u != null;
    u.accept(new ASTVisitor(true) {
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * AnnotationTypeDeclaration) */
      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        print(AnnotationTypeDeclaration.class);
        // assert ("AnnotationTypeDeclaration is not included",
        return super.visit(¢);
      }
      @Override public boolean visit(final FieldDeclaration ¢) {
        // assert !("FieldDeclaration is not included",
        return super.visit(¢);
      }
      @Override public boolean visit(final MethodDeclaration ¢) {
        // assert !("MethodDeclaration is not included",
        return super.visit(¢);
      }
      @Override public boolean visit(final TypeDeclaration ¢) {
        // assert ("TypeDeclaration is not included",
        return super.visit(¢);
      }
    });
  }
  @Test public void testSpartanizerCheckMethod_03() {
    final String test4 = "package test;import static il.org.spartan.plugin.demos.Inline.*;"
        + "import fluent.ly.*;import static fluent.ly.azzert.*;import org.junit.*;public class Test{public void method1(){int i = 1; assert (i>0);} }";
    print(test4);
    final ASTNode u = makeAST.COMPILATION_UNIT.from(test4);
    assert u != null;
    // noinspection SameReturnValue
    u.accept(new ASTVisitor(true) {
      boolean storeMethodName(final SimpleName ¢) {
        method = ¢ + "";
        return false;
      }
      @Override public boolean visit(final MethodDeclaration ¢) {
        return storeMethodName(¢.getName());
      }
    });
    assert "method1".equals(method);
  }
  @Test public void testStringMatches_01() {
    assert "/basedir/test".matches("[/A-Za-z0-9]*[/]test[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_02() {
    assert "/basedir/test/".matches("[/A-Za-z0-9]*[/]test[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_03() {
    assert "/basedir/test/dir".matches("[/A-Za-z0-9]*[/]test[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_04() {
    assert "basedir/test".matches("[/A-Za-z0-9]*[/]test[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_05() {
    assert "basedir/test/".matches("[/A-Za-z0-9]*[/]test[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_06() {
    assert "basedir/test/dir".matches("[/A-Za-z0-9]*[/]test[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_07() {
    assert "/matteo/test".matches("[/A-Za-z0-9]*[\\-/]test[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_08() {
    assert !"/matteo/test".matches("[/A-Za-z0-9]*[\\-/]test1[/A-Za-z0-9]*");
  }
  @Test public void testStringMatches_09() {
    assert "/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/test/common/voldemort/VoldemortTestConstants.java"
        .matches("[/A-Za-z0-9-_.]*test[/A-Za-z0-9-_.]*");
  }
  @Test public void testStringMatches_10() {
    assert "/projects/voldemort/test/common/voldemort/VoldemortTestConstants.java".matches("[/A-Za-z0-9-_.]*test[/A-Za-z0-9-_.]*");
  }
  @Test public void testStringMatches_11() {
    assert "/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/test/integration/voldemort/performance/StoreRoutingPlanPerf.java"
        .matches("[/A-Za-z0-9-_.]*test[/A-Za-z0-9-_.]*");
  }
  @Test public void testStringMatches_12() {
    assert "/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/contrib/ec2-testing/src/java/voldemort/utils/impl/RsyncDeployer.java"
        .matches("[/A-Za-z0-9-_.]*test[/A-Za-z0-9-_.]*");
  }
  private void visitASTNode(final ASTNode u1) {
    u1.accept(new ASTVisitor(true) {
      boolean hasTestAnnotation(final MethodDeclaration d) {
        final List<?> $ = modifiers(d);
        return $.stream().anyMatch(λ -> λ instanceof MarkerAnnotation && (λ + "").contains("@Test"));
      }
      @Override public boolean visit(final MethodDeclaration node) {
        print("MethodDeclaration node: getName(): " + node.getName());
        return !hasTestAnnotation(node) && countMethods();
      }
    });
  }
}