package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

/** Test for the GUIBatchLaconizer class
 * @author Matteo Orrù
 * @since 2016 */
public class SpartanizerTest {
  String method = "";
  private final String test1 = "package test;\n" + "import static il.org.spartan.plugin.demos.Inline.*;\n"
      + "import  static il.org.spartan.azzert.*; import org.junit.*;\n" + "public class Test {\n"
      + " @Ignore(\"comment\") @Test public void aTestMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n"
      + " public void notATestMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n" + "}";
  private final String test2 = "package test;\n" + "import static il.org.spartan.plugin.demos.Inline.*;\n"
      + "import  static il.org.spartan.azzert.*; import org.junit.*;\n" + "public class Test {\n"
      + " @Ignore(\"comment\") @Test public void aTestMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n"
      + " public void notATestMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n" + " public void ASecondNotTestMethod(){\n "
      + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n" + "}";

  public static void main(final String[] args) {
    final ASTNode u = makeAST.COMPILATION_UNIT.from("package test;\n" + "import static il.org.spartan.plugin.demos.Inline.*;\n"
        + "import  static il.org.spartan.azzert.*; import org.junit.*;\n" + "public class Test {\n"
        + " @Ignore(\"comment\") @Test public void testMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n" + "}");
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
        System.out.println("parent: " + node.getParent().getNodeType());
        return super.visit(node);
      }
    });
  }

  private static int nMethods;

  @Test @SuppressWarnings("static-method") public void testStringMatches_01() {
    assert "/basedir/test".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_02() {
    assert "/basedir/test/".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_03() {
    assert "/basedir/test/dir".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_04() {
    assert "basedir/test".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_05() {
    assert "basedir/test/".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_06() {
    assert "basedir/test/dir".matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_07() {
    assert "/matteo/test".matches("[\\/A-Za-z0-9]*[\\-/]test[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_08() {
    assert !"/matteo/test".matches("[\\/A-Za-z0-9]*[\\-/]test1[\\/A-Za-z0-9]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_09() {
    assert "/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/test/common/voldemort/VoldemortTestConstants.java"
        .matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_10() {
    assert "/projects/voldemort/test/common/voldemort/VoldemortTestConstants.java".matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_11() {
    assert "/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/test/integration/voldemort/performance/StoreRoutingPlanPerf.java"
        .matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*");
  }

  @Test @SuppressWarnings("static-method") public void testStringMatches_12() {
    assert "/home/matteo/MUTATION_TESTING/GL-corpus/projects/voldemort/contrib/ec2-testing/src/java/voldemort/utils/impl/RsyncDeployer.java"
        .matches("[\\/A-Za-z0-9-_.]*test[\\/A-Za-z0-9-_.]*");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_01() {
    assert "fooTest.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_02() {
    assert "test.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_03() {
    assert "Test.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_04() {
    assert "Testfoo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_05() {
    assert "testfoo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_06() {
    assert "foo1Testfoo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_07() {
    assert "foo1testfoo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_08() {
    assert "test_foo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_09() {
    assert "foo1_Test_foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_10() {
    assert "foo1_test_foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_11() {
    assert "test-foo.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_12() {
    assert "foo1-Test-foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test @SuppressWarnings("static-method") public void testFileName_13() {
    assert "foo1-test-foo2.java".matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java");
  }

  @Test public void testMethodWithAnnotation_01() {
    final ASTNode u1 = makeAST.COMPILATION_UNIT.from(test1);
    final ASTNode u2 = makeAST.COMPILATION_UNIT.from(test2);
    assert u1 != null;
    assert u2 != null;
    visitASTNode(u1);
    assert nMethods == 1;
    visitASTNode(u2);
    assert nMethods == 3;
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
        // assert ("AnnotationTypeDeclaration is not included",
        // gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }

      @Override public boolean visit(final MethodDeclaration ¢) {
        // assert !("MethodDeclaration is not included",
        // gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        // assert ("TypeDeclaration is not included",
        // !gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        // assert !("FieldDeclaration is not included",
        // !gUIBatchLaconizer.check(¢));
        return super.visit(¢);
      }
    });
  }

  @Test public void testSpartanizerCheckMethod_03() {
    final String test4 = "package test;\n" + "import static il.org.spartan.plugin.demos.Inline.*;\n"
        + "import  static il.org.spartan.azzert.*; import org.junit.*;\n" + "public class Test {\n" + " public void method1(){\n " + "   int i = 1;\n"
        + "   assert (i>0);\n" + " }\n" + "}";
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
    assert "method1".equals(method);
  }
}