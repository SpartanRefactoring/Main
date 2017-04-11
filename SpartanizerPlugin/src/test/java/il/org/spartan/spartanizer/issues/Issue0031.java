package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0031 {
  @Test public void a() {
    topDownTrimming("static boolean hasAnnotation(final VariableDeclarationStatement n, int abcd) {\n      return hasAnnotation(now.modifiers());\n    }")
        .gives("static boolean hasAnnotation(final VariableDeclarationStatement s, int abcd) {\n"
            + "      return hasAnnotation(now.modifiers());\n    }");
  }

  @Test public void b() {
    topDownTrimming("void f(final VariableDeclarationStatement n, int abc) {}")//
        .gives("void f(final VariableDeclarationStatement s, int abc) {}");
  }

  @Test public void c() {
    topDownTrimming("void f(final VariableDeclarationAtatement n, int abc) {}")//
        .gives("void f(final VariableDeclarationAtatement a, int abc) {}");
  }

  @Test public void d() {
    topDownTrimming("void f(final Expression n) {}")//
        .gives("void f(final Expression x) {}");
  }

  @Test public void e() {
    topDownTrimming("void f(final Exception n) {}")//
        .gives("void f(final Exception x) {}");
  }

  @Test public void f() {
    topDownTrimming("void f(final Exception exception, Expression expression) {}")//
        .gives("void f(final Exception x, Expression expression) {}");
  }

  @Test public void g() {
    topDownTrimming("void foo(TestExpression exp,TestAssignment testAssignment){return f(exp,testAssignment);}")
        .gives("void foo(TestExpression x,TestAssignment testAssignment){return f(x,testAssignment);}")
        .gives("void foo(TestExpression x,TestAssignment a){return f(x,a);}");
  }

  @Test public void h() {
    topDownTrimming("void f(final Exception n) {}")//
        .gives("void f(final Exception x) {}");
  }

  @Test public void i() {
    topDownTrimming("void f(final Exception n) {}")//
        .gives("void f(final Exception x) {}");
  }

  @Test public void j() {
    topDownTrimming("void foo(Exception exception, Assignment assignment)")//
        .gives("void foo(Exception x, Assignment assignment)").gives("void foo(Exception __, Assignment assignment)")//
        .gives("void foo(Exception __, Assignment a)")//
        .stays();
  }

  @Test public void k() {
    topDownTrimming("String tellTale(Example example)")//
        .gives("String tellTale(Example x)");
  }

  @Test public void l() {
    topDownTrimming("String tellTale(Example examp)")//
        .gives("String tellTale(Example x)");
  }

  @Test public void m() {
    topDownTrimming("String tellTale(ExamplyExamplar lyEx)")//
        .gives("String tellTale(ExamplyExamplar x)");
  }

  @Test public void n() {
    topDownTrimming("String tellTale(ExamplyExamplar foo)")//
        .stays();
  }
}
