package il.org.spartan.spartanizer.cmdline;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

/** Test for the GUIBatchLaconizer class
 * @author Matteo Orrù
 * @since 2016 */
public class SpartanizerTest {
  String method = "";
  private final String test1 = "package test;\n" + "import static org.junit.Assert.*;\n" + "import org.junit.*;\n" + "public class Test {\n"
      + " @Ignore(\"comment\") @Test public void aTestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n"
      + " public void notATestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + "}";
  private final String test2 = "package test;\n" + "import static org.junit.Assert.*;\n" + "import org.junit.*;\n" + "public class Test {\n"
      + " @Ignore(\"comment\") @Test public void aTestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n"
      + " public void notATestMethod(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + " public void ASecondNotTestMethod(){\n "
      + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + "}";
  private final String test4 = "package test;\n" + "import static org.junit.Assert.*;\n" + "import org.junit.*;\n" + "public class Test {\n"
      + " public void method1(){\n " + "   int i = 1;\n" + "   assertTrue(i>0);\n" + " }\n" + "}";

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
        System.out.println(MethodDeclaration.class + ": " + node.getName());
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
        System.out.println(AnnotationTypeMemberDeclaration.class + ": " + node.getName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * ImportDeclaration) */
      @Override public boolean visit(final ImportDeclaration node) {
        System.out.println(ImportDeclaration.class + ": " + node.getName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * PackageDeclaration) */
      @Override public boolean visit(final PackageDeclaration node) {
        System.out.println(PackageDeclaration.class + ": " + node.getName());
        return super.visit(node);
      }

      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * MethodInvocation) */
      @Override public boolean visit(final MethodInvocation node) {
        System.out.println(MethodInvocation.class + ": " + node.getName());
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

  // examples from real world
  @SuppressWarnings("static-method") @Test public void testStringMatches_09() {
    assertTrue("/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/test/common/voldemort/VoldemortTestConstants.java"
        .matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_10() {
    assertTrue("/projects/voldemort/test/common/voldemort/VoldemortTestConstants.java".matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_11() {
    assertTrue("/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/test/integration/voldemort/performance/StoreRoutingPlanPerf.java"
        .matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*"));
  }

  @SuppressWarnings("static-method") @Test public void testStringMatches_12() {
    assertTrue("/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/contrib/ec2-testing/src/java/voldemort/utils/impl/RsyncDeployer.java"
        .matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*"));
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

  @SuppressWarnings("static-method") boolean countMethods() {
    ++nMethods;
    return false;
  }

  @Test public void testSpartanizerCheckMethod_01() {
    System.out.println(test1);
    final ASTNode u = makeAST.COMPILATION_UNIT.from(test2);
    System.out.println(u.getClass());
    assert u != null;
  }

  @Test public void testSpartanizerCheckMethod_02() {
    System.out.println(test1);
    final ASTNode u = makeAST.COMPILATION_UNIT.from(test2);
    assert u != null;
    u.accept(new ASTVisitor() {
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * AnnotationTypeDeclaration) */
      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        System.out.println(AnnotationTypeDeclaration.class);
        // assertTrue("AnnotationTypeDeclaration is not included",
        // gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }

      @Override public boolean visit(final MethodDeclaration ¢) {
        // assertFalse("MethodDeclaration is not included",
        // gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        // assertTrue("TypeDeclaration is not included",
        // !gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        // assertFalse("FieldDeclaration is not included",
        // !gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }
    });
  }

  @Test public void testSpartanizerCheckMethod_03() {
    System.out.println(test4);
    final ASTNode u = makeAST.COMPILATION_UNIT.from(test4);
    assert u != null;
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        return storeMethodName(¢.getName());
      }

      boolean storeMethodName(final SimpleName ¢) {
        method = ¢ + "";
        return false;
      }
    });
    assertTrue("method1".equals(method));
  }
}