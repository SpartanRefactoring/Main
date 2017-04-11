package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Unit tests for {@link AssignmentToPostfixIncrement}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method")
public final class Issue0107 {
  @Test public void a() {
    trimminKof("a+=1;")//
        .stays();
  }

  @Test public void b() {
    trimminKof("for(int c = 0; c <5; c-=1)\nc*=2;")//
        .gives("for(int c = 0; c <5; --c)\nc*=2;")//
        .stays();
  }

  @Test public void e() {
    trimminKof("for(String a ; a.length()<3 ; (a = \"\")+=1){}")//
        .gives("for(String a ; a.length()<3 ; (a = \"\")+=1);").stays();
  }

  @Test public void f() {
    trimminKof("a+=2;")//
        .stays();
  }

  @Test public void g() {
    trimminKof("a/=1;")//
        .stays();
  }

  @Test public void i() {
    trimminKof("a-=1;")//
        .gives("--a;")//
        .stays();
  }

  @Test public void j() {
    trimminKof("for(int a ; a<10 ; a-=1);")//
        .gives("for(int a ; a<10 ; --a);");
  }

  @Test public void k() {
    trimminKof("a-=2;")//
        .stays();
  }

  @Test public void l() {
    trimminKof("while((x-=1) > 0);")//
        .gives("while((--x) > 0);");
  }

  @Test public void m() {
    trimminKof("s = \"hello\"; \ns += 1;")//
        .stays();
  }

  @Test public void o() {
    trimminKof("for(int a ; a<3 ; a+=1);")//
        .gives("for(int a ; a<3 ; a++);")//
        .gives("for(int a ; a<3 ; ++a);")//
        .stays();
  }

  @Test public void t1() {
    trimminKof("while((x-=1) >= 1) System.out.println(x);")//
        .gives("while((--x) >= 1) System.out.println(x);");
  }

  @Test public void t2() {
    trimminKof("i = a += 1;")//
        .stays();
  }

  @Test public void t3() {
    trimminKof("i += i + 1;")//
        .stays();
  }

  @Test public void t4() {
    trimminKof("i -= i - 1")//
        .stays();
  }
}
