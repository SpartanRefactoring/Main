package il.org.spartan.spartanizer.cmdline;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

/** Test for the Spartanizer class
 * @author Matteo Orrù
 * @since 2016 */
public class SpartanizerTest {
  public static void main(final String[] args) {
    final String test = "package test;\n" + "import static org.junit.Assert.*;\n" + "import org.junit.*;\n" + "public class Test {\n"
        + " @Ignore(\"comment\") @Test public void testMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + "}";
    final ASTNode u = makeAST.COMPILATION_UNIT.from(test);
    assert u != null;
    u.accept(new ASTVisitor() {
      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * AnnotationTypeDeclaration) */
      @Override public boolean visit(final AnnotationTypeDeclaration node) {
        System.out.println("node.getName().getIdentifier(): " + node.getName().getIdentifier());
        return true; // super.visit(node);
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * MethodDeclaration) */
      @Override public boolean visit(final MethodDeclaration node) {
        System.out.println("MethodDeclaration node: getName(): " + node.getName());
        return !hasTestAnnotation(node);
      }

      boolean hasTestAnnotation(final MethodDeclaration d) {
        final List<?> modifiers = d.modifiers();
        for (int ¢ = 0; ¢ < modifiers.size(); ++¢)
          if (modifiers.get(¢) instanceof MarkerAnnotation && (modifiers.get(¢) + "").contains("@Test") && (modifiers.get(¢) + "").contains("@Test"))
            return true;
        return false;
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * AnonymousClassDeclaration) */
      @Override public boolean visit(final AnnotationTypeMemberDeclaration node) {
        System.out.println("AnnotationTypeMemberDeclaration node.getName():" + node.getName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * ImportDeclaration) */
      @Override public boolean visit(final ImportDeclaration node) {
        System.out.println(node.getName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * PackageDeclaration) */
      @Override public boolean visit(final PackageDeclaration node) {
        System.out.println(node.getName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * MethodInvocation) */
      @Override public boolean visit(final MethodInvocation node) {
        System.out.println(node.getName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * Assignment) */
      @Override public boolean visit(final Assignment node) {
        System.out.println(node.getOperator());
        return super.visit(node);
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * NormalAnnotation) */
      @Override public boolean visit(final NormalAnnotation node) {
        System.out.println("NormalAnnotation: " + node.getTypeName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       * 
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * MarkerAnnotation) */
      @Override public boolean visit(final MarkerAnnotation node) {
        System.out.println("MarkerAnnotation: " + node.getTypeName());
        final ASTNode parent = node.getParent();
        System.out.println("parent: " + parent.getNodeType());
        return super.visit(node);
      }
    });
  }

  private static int nMethods;

  // testing how the matches method works
  @SuppressWarnings("static-method") @Test public void testStringMatches_01() {
    assertTrue("/basedir/test".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_02() {
    assertTrue("/basedir/test/".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_03() {
    assertTrue("/basedir/test/dir".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_04() {
    assertTrue("basedir/test".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_05() {
    assertTrue("basedir/test/".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_06() {
    assertTrue("basedir/test/dir".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_07() {
    assertTrue("/matteo/test".matches("[\\/A-Za-z0-9]*[\\-/]test[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_08() {
    assertFalse("/matteo/test".matches("[\\/A-Za-z0-9]*[\\-/]test1[\\/A-Za-z0-9]*"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_01() {
    assertTrue("fooTest.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_02() {
    assertTrue("test.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_03() {
    assertTrue("Test.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_04() {
    assertTrue("Testfoo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_05() {
    assertTrue("testfoo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_06() {
    assertTrue("foo1Testfoo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_07() {
    assertTrue("foo1testfoo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_08() {
    assertTrue("test_foo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_09() {
    assertTrue("foo1_Test_foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_10() {
    assertTrue("foo1_test_foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_11() {
    assertTrue("test-foo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_12() {
    assertTrue("foo1-Test-foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @SuppressWarnings("static-method") @Test public void testFileName_13() {
    assertTrue("foo1-test-foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java"));
  }

  @Test public void testMethodWithAnnotation_01() {
    final String test1 = "package test;\n" + "import static org.junit.Assert.*;\n" + "import org.junit.*;\n" + "public class Test {\n"
        + " @Ignore(\"comment\") @Test public void aTestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n"
        + " public void notATestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + "}";
    final String test2 = "package test;\n" + "import static org.junit.Assert.*;\n" + "import org.junit.*;\n" + "public class Test {\n"
        + " @Ignore(\"comment\") @Test public void aTestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n"
        + " public void notATestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + " public void ASecondNotTestMethod(){\n "
        + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + "}";
    final ASTNode u1 = makeAST.COMPILATION_UNIT.from(test1);
    final ASTNode u2 = makeAST.COMPILATION_UNIT.from(test2);
    assert u1 != null;
    assert u2 != null;
    visitASTNode(u1);
    assertTrue(nMethods == 1);
    visitASTNode(u2);
    assertTrue(nMethods == 3);
  }

  /** @param u1 */
  private void visitASTNode(final ASTNode u1) {
    u1.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration node) {
        System.out.println("MethodDeclaration node: getName(): " + node.getName());
        return !hasTestAnnotation(node) && countMethods();
      }

      boolean hasTestAnnotation(final MethodDeclaration d) {
        final List<?> modifiers = d.modifiers();
        for (int ¢ = 0; ¢ < modifiers.size(); ++¢)
          if (modifiers.get(¢) instanceof MarkerAnnotation && (modifiers.get(¢) + "").contains("@Test") && (modifiers.get(¢) + "").contains("@Test"))
            return true;
        return false;
      }
    });
  }

  private boolean countMethods() {
    ++nMethods;
    return false;
  }
}
