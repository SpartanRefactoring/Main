package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0058 {
  @Test public void a() {
    trimmingOf("X f(List<List<Expression>> expressions){}")//
        .gives("X f(List<List<Expression>> xss){}");
  }
  @Test public void b() {
    trimmingOf("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }
  @Test public void c() {
    trimmingOf("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }
  @Test public void d() {
    trimmingOf("X f(List<Expression>... expressions){}")//
        .gives("X f(List<Expression>... xss){}");
  }
  @Test public void e() {
    trimmingOf("X f(Expression[]... expressions){}")//
        .gives("X f(Expression[]... xss){}");
  }
  @Test public void f() {
    trimmingOf("X f(Expression[][]... expressions){}")//
        .gives("X f(Expression[][]... xsss){}");
  }
  @Test public void g() {
    trimmingOf("X f(List<Expression[][]>... expressions){}")//
        .gives("X f(List<Expression[][]>... xssss){}");
  }
}
