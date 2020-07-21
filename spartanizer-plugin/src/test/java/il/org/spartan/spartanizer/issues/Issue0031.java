package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/**
 * Unit tests for the GitHub issue thus numbered
 * @author  Yossi Gil
 * @since  2016 
 */
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0031 {
  @Test public void a() {
    trimmingOf("static boolean hasAnnotation(final VariableDeclarationStatement n, int abcd) {\n      return hasAnnotation(now.modifiers());\n    }")
        .gives("static boolean hasAnnotation(final VariableDeclarationStatement s, int abcd) {\n"
            + "      return hasAnnotation(now.modifiers());\n    }");
  }
  @Test public void b() {
    trimmingOf("void f(final VariableDeclarationStatement n, int abc) {}").gives("void f(final VariableDeclarationStatement s, int abc) {}");
  }
  @Test public void c() {
    trimmingOf("void f(final VariableDeclarationAtatement n, int abc) {}").gives("void f(final VariableDeclarationAtatement a, int abc) {}");
  }
  @Test public void d() {
    trimmingOf("void f(final Expression n) {}").gives("void f(final Expression x) {}");
  }
  @Test public void e() {
    trimmingOf("void f(final Exception n) {}").gives("void f(final Exception x) {}");
  }
  @Test public void f() {
    trimmingOf("void f(final Exception exception, Expression expression) {}").gives("void f(final Exception x, Expression expression) {}");
  }
  @Test public void g() {
    trimmingOf("void foo(TestExpression exp,TestAssignment testAssignment){return f(exp,testAssignment);}")
        .gives("void foo(TestExpression x,TestAssignment testAssignment){return f(x,testAssignment);}")
        .gives("void foo(TestExpression x,TestAssignment a){return f(x,a);}");
  }
  @Test public void h() {
    trimmingOf("void f(final Exception n) {}").gives("void f(final Exception x) {}");
  }
  @Test public void i() {
    trimmingOf("void f(final Exception n) {}").gives("void f(final Exception x) {}");
  }
  @Test public void j() {
    trimmingOf("void foo(Exception exception, Assignment assignment)").gives("void foo(Exception x, Assignment assignment)")
        .gives("void foo(Exception __, Assignment assignment)").gives("void foo(Exception __, Assignment a)").stays();
  }
  @Test public void k() {
    trimmingOf("String tellTale(Example example)").gives("String tellTale(Example x)");
  }
  @Test public void l() {
    trimmingOf("String tellTale(Example examp)").gives("String tellTale(Example x)");
  }
  @Test public void m() {
    trimmingOf("String tellTale(ExamplyExamplar lyEx)").gives("String tellTale(ExamplyExamplar x)");
  }
  @Test public void n() {
    trimmingOf("String tellTale(ExamplyExamplar foo)").stays();
  }
}
