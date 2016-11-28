package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ParseASTTest {
  /** @param args */
  private final String test1 = "package test;\n" + "import static il.org.spartan.plugin.demos.Inline.*;\n"
      + "import  static il.org.spartan.azzert.*; import org.junit.*;\n" + "public class Test {\n"
      + " @Ignore(\"comment\") @Test public void aTestMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n"
      + " public void notATestMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n" + "}";

  public static void main(final String[] args) {
    final ASTNode u = makeAST.COMPILATION_UNIT.from("package test;\n" + "import static il.org.spartan.plugin.demos.Inline.*;\n" + "import  static il.org.spartan.azzert.*; import org.junit.*;\n"
        + "public class Test {\n" + " @Ignore(\"comment\") @Test public void testMethod(){\n " + "   int i = 1;\n" + "   assert (i>0);\n" + " }\n"
        + "}");
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
        System.out.println("parent: " + node.getParent().getNodeType());
        return super.visit(node);
      }
    });
  }

  @Test public void testStepMethod_01() {
    makeAST.COMPILATION_UNIT.from(test1).accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration node) {
        for (final Statement o : step.statements(step.body(node))) {
          System.out.println("class: " + o.getClass());
          System.out.println("statement: " + o);
          System.out.println(step.expression(o));
        }
        System.out.println(step.body(node));
        return super.visit(node);
      }
    });
  }
}
