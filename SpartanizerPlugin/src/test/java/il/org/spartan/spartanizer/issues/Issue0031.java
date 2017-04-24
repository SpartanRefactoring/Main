package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0031 {
  @Test public void a() {
    trimminKof("static boolean hasAnnotation(final VariableDeclarationStatement n, int abcd) {\n      return hasAnnotation(now.modifiers());\n    }")
        .gives("static boolean hasAnnotation(final VariableDeclarationStatement s, int abcd) {\n"
            + "      return hasAnnotation(now.modifiers());\n    }");
  }

  @Test public void b() {
    trimminKof("void f(final VariableDeclarationStatement n, int abc) {}")//
        .gives("void f(final VariableDeclarationStatement s, int abc) {}");
  }

  @Test public void c() {
    trimminKof("void f(final VariableDeclarationAtatement n, int abc) {}")//
        .gives("void f(final VariableDeclarationAtatement a, int abc) {}");
  }

  @Test public void d() {
    trimminKof("void f(final Expression n) {}")//
        .gives("void f(final Expression x) {}");
  }

  @Test public void e() {
    trimminKof("void f(final Exception n) {}")//
        .gives("void f(final Exception x) {}");
  }

  @Test public void f() {
    trimminKof("void f(final Exception exception, Expression expression) {}")//
        .gives("void f(final Exception x, Expression expression) {}");
  }

  @Test public void g() {
    trimminKof("void foo(TestExpression exp,TestAssignment testAssignment){return f(exp,testAssignment);}")
        .gives("void foo(TestExpression x,TestAssignment testAssignment){return f(x,testAssignment);}")
        .gives("void foo(TestExpression x,TestAssignment a){return f(x,a);}");
  }

  @Test public void h() {
    trimminKof("void f(final Exception n) {}")//
        .gives("void f(final Exception x) {}");
  }

  @Test public void i() {
    trimminKof("void f(final Exception n) {}")//
        .gives("void f(final Exception x) {}");
  }

  @Test public void j() {
    trimminKof("void foo(Exception exception, Assignment assignment)")//
        .gives("void foo(Exception x, Assignment assignment)").gives("void foo(Exception __, Assignment assignment)")//
        .gives("void foo(Exception __, Assignment a)")//
        .stays();
  }

  @Test public void k() {
    trimminKof("String tellTale(Example example)")//
        .gives("String tellTale(Example x)");
  }

  @Test public void l() {
    trimminKof("String tellTale(Example examp)")//
        .gives("String tellTale(Example x)");
  }

  @Test public void m() {
    trimminKof("String tellTale(ExamplyExamplar lyEx)")//
        .gives("String tellTale(ExamplyExamplar x)");
  }

  @Test public void n() {
    trimminKof("String tellTale(ExamplyExamplar foo)")//
        .stays();
  }
}
