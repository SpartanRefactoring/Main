package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit tests for {@link AssignmentToPostfixIncrement}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method") public final class Issue107Test {
  // Not provably-not-string.
  @Test public void issue107a() {
    trimmingOf("a+=1;").stays();
  }

  @Test public void issue107b() {
    trimmingOf("for(int c = 0; c < 5; c-=1)\n" + "c*=2;").gives("for(int c = 0; c < 5; c--)" + "c*=2;");
  }

  @Test public void issue107c() {
    trimmingOf("java_is_even_nice+=1+=1;").gives("java_is_even_nice+=1++;");
  }

  @Test public void issue107d() {
    trimmingOf("for(int a ; a<10 ; (--a)+=1){}").gives("for(int a ; a<10 ; (--a)++){}");
  }

  @Test public void issue107e() {
    trimmingOf("for(String a ; a.length()<3 ; (a = \"\")+=1){}").stays();
  }

  @Test public void issue107f() {
    trimmingOf("a+=2;").stays();
  }

  @Test public void issue107g() {
    trimmingOf("a/=1;").stays();
  }

  @Ignore public void issue107h() {
    trimmingOf("a-+=1;").gives("a-++;");
  }

  @Test public void issue107i() {
    trimmingOf("a-=1;").gives("a--;").gives("--a;").stays();
  }

  @Test public void issue107j() {
    trimmingOf("for(int a ; a<10 ; a-=1){}").gives("for(int a ; a<10 ; a--){}");
  }

  @Test public void issue107k() {
    trimmingOf("a-=2;").stays();
  }

  @Test public void issue107l() {
    trimmingOf("while(x-=1){}").gives("while(x--){}");
  }

  @Test public void issue107m() {
    trimmingOf("s = \"hello\"; \n" + "s += 1;").stays();
  }

  @Test public void issue107n() {
    trimmingOf("for(;; (a = 3)+=1){}").gives("for(;; (a = 3)++){}");
  }

  @Test public void issue107o() {
    trimmingOf("for(int a ; a<3 ; a+=1){}").stays();
  }
}
