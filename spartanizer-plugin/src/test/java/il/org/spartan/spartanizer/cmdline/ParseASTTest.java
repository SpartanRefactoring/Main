/* TODO please add a description
 *
 * @author
 *
 * @since */
package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.ast.navigate.step.extendedModifiers;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.Test;

import fluent.ly.forget;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.step;

public class ParseASTTest {
  public static void main(final String[] args) {
    final ASTNode u = makeAST.COMPILATION_UNIT.from("package test;\nimport static il.org.spartan.plugin.demos.Inline.*;\n"
        + "import  static il.org.spartan.azzert.*; import org.junit.*;\npublic class Test {\n"
        + " @forget(\"comment\") @Test public void testMethod(){\n    int i = 1;\n   assert (i>0);\n }\n}");
    assert u != null;
    // noinspection SameReturnValue
    u.accept(new ASTVisitor(true) {
      boolean hasTestAnnotation(final MethodDeclaration d) {
        return extendedModifiers(d).stream().anyMatch(λ -> λ instanceof MarkerAnnotation && (λ + "").contains("@Test"));
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * AnnotationTypeDeclaration) */
      @Override public boolean visit(final AnnotationTypeDeclaration node) {
        print("node.getName().getIdentifier(): " + node.getName().getIdentifier());
        return true; // super.visit(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * AnonymousClassDeclaration) */
      @Override public boolean visit(final AnnotationTypeMemberDeclaration node) {
        print("AnnotationTypeMemberDeclaration node.getName():" + node.getName());
        return super.visit(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * Assignment) */
      @Override public boolean visit(final Assignment node) {
        print(node.getOperator());
        return super.visit(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * ImportDeclaration) */
      @Override public boolean visit(final ImportDeclaration node) {
        print(node.getName());
        return super.visit(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * MarkerAnnotation) */
      @Override public boolean visit(final MarkerAnnotation node) {
        print("MarkerAnnotation: " + node.getTypeName());
        print("parent: " + node.getParent().getNodeType());
        return super.visit(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * MethodDeclaration) */
      @Override public boolean visit(final MethodDeclaration node) {
        print("MethodDeclaration node: getName(): " + node.getName());
        return !hasTestAnnotation(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * MethodInvocation) */
      @Override public boolean visit(final MethodInvocation node) {
        print(node.getName());
        return super.visit(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * NormalAnnotation) */
      @Override public boolean visit(final NormalAnnotation node) {
        print("NormalAnnotation: " + node.getTypeName());
        return super.visit(node);
      }
      /* (non-Javadoc)
       *
       * @see
       * org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
       * PackageDeclaration) */
      @Override public boolean visit(final PackageDeclaration node) {
        print(node.getName());
        return super.visit(node);
      }
    });
  }
  static void print(final Object ¢) {
    forget.em(new Object[] { ¢ });
  }
  @Test @SuppressWarnings("static-method") public void testStepMethod_01() {
    makeAST.COMPILATION_UNIT
        .from("package test;\nimport static il.org.spartan.plugin.demos.Inline.*;\nimport  static il.org.spartan.azzert.*; import org.junit.*;\n"
            + "public class Test {\n @forget(\"comment\") @Test public void aTestMethod(){\n    int i = 1;\n   assert (i>0);\n"
            + " }\n public void notATestMethod(){\n    int i = 1;\n   assert (i>0);\n }\n}")
        .accept(new ASTVisitor(true) {
          @Override public boolean visit(final MethodDeclaration $) {
            for (final Statement o : statements(step.body($))) {
              print("class: " + o.getClass());
              print("statement: " + o);
              print(step.expression(o));
            }
            print(step.body($));
            return super.visit($);
          }
        });
  }
}
