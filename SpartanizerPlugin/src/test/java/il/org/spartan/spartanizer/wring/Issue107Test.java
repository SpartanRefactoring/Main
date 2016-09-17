package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;

import il.org.spartan.spartanizer.wrings.*;

/** Unit tests for {@link AssignmentToPostfixIncrement}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method") public class Issue107Test {
  // Not provably-not-string.
  @Test public void issue107a() {
    trimming("a+=1;").stays();
  }

  @Test public void issue107b() {
    trimming("for(int c = 0; c < 5; c-=1)\n" + "c*=2;").to("for(int c = 0; c < 5; c--)" + "c*=2;");
  }

  @Test public void issue107c() {
    trimming("java_is_even_nice+=1+=1;").to("java_is_even_nice+=1++;");
  }

  @Test public void issue107d() {
    trimming("for(int a ; a<10 ; (--a)+=1){}").to("for(int a ; a<10 ; (--a)++){}");
  }

  @Test public void issue107e() {
    trimming("for(String a ; a.length()<3 ; (a = \"\")+=1){}").stays();
  }

  @Test public void issue107f() {
    trimming("a+=2;").stays();
  }

  @Test public void issue107g() {
    trimming("a/=1;").stays();
  }

  @Ignore public void issue107h() {
    trimming("a-+=1;").to("a-++;");
  }

  @Test public void issue107i() {
    trimming("a-=1;").to("a--;").to("--a;").stays();
  }

  @Test public void issue107j() {
    trimming("for(int a ; a<10 ; a-=1){}").to("for(int a ; a<10 ; a--){}");
  }

  @Test public void issue107k() {
    trimming("a-=2;").stays();
  }

  @Test public void issue107l() {
    trimming("while(x-=1){}").to("while(x--){}");
  }

  @Test public void issue107m() {
    trimming("s = \"hello\"; \n" + "s += 1;").stays();
  }

  @Test public void issue107n() {
    trimming("for(;; (a = 3)+=1){}").to("for(;; (a = 3)++){}");
  }

  @Test public void issue107o() {
    trimming("for(int a ; a<3 ; a+=1){}").stays();
  }
}
