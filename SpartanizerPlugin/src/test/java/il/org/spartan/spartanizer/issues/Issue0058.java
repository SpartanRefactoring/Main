package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0058 {
  @Test public void a() {
    trimminKof("X f(List<List<Expression>> expressions){}")//
        .gives("X f(List<List<Expression>> xss){}");
  }

  @Test public void b() {
    trimminKof("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }

  @Test public void c() {
    trimminKof("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }

  @Test public void d() {
    trimminKof("X f(List<Expression>... expressions){}")//
        .gives("X f(List<Expression>... xss){}");
  }

  @Test public void e() {
    trimminKof("X f(Expression[]... expressions){}")//
        .gives("X f(Expression[]... xss){}");
  }

  @Test public void f() {
    trimminKof("X f(Expression[][]... expressions){}")//
        .gives("X f(Expression[][]... xsss){}");
  }

  @Test public void g() {
    trimminKof("X f(List<Expression[][]>... expressions){}")//
        .gives("X f(List<Expression[][]>... xssss){}");
  }
}
