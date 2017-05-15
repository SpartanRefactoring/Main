package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for {@link AssignmentToPostfixIncrement}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method")
public final class Issue0107 {
  @Test public void a() {
    trimmingOf("a+=1;")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("for(int c = 0; c <5; c-=1)\nc*=2;")//
        .gives("for(int c = 0; c <5; --c)\nc*=2;")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("for(String a ; a.length()<3 ; (a = \"\")+=1){}")//
        .gives("for(String a ; a.length()<3 ; (a = \"\")+=1);").stays();
  }
  @Test public void f() {
    trimmingOf("a+=2;")//
        .stays();
  }
  @Test public void g() {
    trimmingOf("a/=1;")//
        .stays();
  }
  @Test public void i() {
    trimmingOf("a-=1;")//
        .gives("--a;")//
        .stays();
  }
  @Test public void j() {
    trimmingOf("for(int a ; a<10 ; a-=1);")//
        .gives("for(int a ; a<10 ; --a);");
  }
  @Test public void k() {
    trimmingOf("a-=2;")//
        .stays();
  }
  @Test public void l() {
    trimmingOf("while((x-=1) > 0);")//
        .gives("while((--x) > 0);");
  }
  @Test public void m() {
    trimmingOf("s = \"hello\"; \ns += 1;")//
        .stays();
  }
  @Test public void o() {
    trimmingOf("for(int a ; a<3 ; a+=1);")//
        .gives("for(int a ; a<3 ; a++);")//
        .gives("for(int a ; a<3 ; ++a);")//
        .stays();
  }
  @Test public void t1() {
    trimmingOf("while((x-=1) >= 1) System.out.println(x);")//
        .gives("while((--x) >= 1) System.out.println(x);");
  }
  @Test public void t2() {
    trimmingOf("i = a += 1;")//
        .stays();
  }
  @Test public void t3() {
    trimmingOf("i += i + 1;")//
        .stays();
  }
  @Test public void t4() {
    trimmingOf("i -= i - 1")//
        .stays();
  }
}
