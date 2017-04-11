package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0058 {
  @Test public void a() {
    topDownTrimming("X f(List<List<Expression>> expressions){}")//
        .gives("X f(List<List<Expression>> xss){}");
  }

  @Test public void b() {
    topDownTrimming("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }

  @Test public void c() {
    topDownTrimming("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }

  @Test public void d() {
    topDownTrimming("X f(List<Expression>... expressions){}")//
        .gives("X f(List<Expression>... xss){}");
  }

  @Test public void e() {
    topDownTrimming("X f(Expression[]... expressions){}")//
        .gives("X f(Expression[]... xss){}");
  }

  @Test public void f() {
    topDownTrimming("X f(Expression[][]... expressions){}")//
        .gives("X f(Expression[][]... xsss){}");
  }

  @Test public void g() {
    topDownTrimming("X f(List<Expression[][]>... expressions){}")//
        .gives("X f(List<Expression[][]>... xssss){}");
  }
}
