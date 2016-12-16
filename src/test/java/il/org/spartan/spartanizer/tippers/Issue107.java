package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit tests for {@link AssignmentToPostfixIncrement}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
@Ignore
@SuppressWarnings("static-method")
public final class Issue107 {
  static class NotWorking {
    @Test public void b() {
      trimmingOf("for(int c = 0; c <5; c-=1)\n" + "c*=2;").gives("for(int c = 0; c <5; c--)\n" + "c*=2;").gives("for(int c = 0; c <5; --c)\n" + "c*=2;")
          .stays();
    }

    @Test public void c() {
      trimmingOf("java_is_even_nice+=1+=1;").gives("java_is_even_nice+=1++;");
    }

    @Test public void d() {
      trimmingOf("for(int a ; a<10 ; (--a)+=1){}").gives("for(int a ; a<10 ; (--a)++){}");
    }
    
    @Test public void i() {
      trimmingOf("a-=1;").gives("a--;").gives("--a;").stays();
    }

    @Test public void j() {
      trimmingOf("for(int a ; a<10 ; a-=1){}").gives("for(int a ; a<10 ; a--){}");
    }
    
    @Test public void l() {
      trimmingOf("while(x-=1){}").gives("while(x--){}");
    }
    
    @Test public void n() {
      trimmingOf("for(;; (a = 3)+=1){}").gives("for(;; (a = 3)++){}");
    }
  }
  // Not provably-not-string.
  @Test public void a() {
    trimmingOf("a+=1;").stays();
  }

  @Test public void e() {
    trimmingOf("for(String a ; a.length()<3 ; (a = \"\")+=1){}").stays();
  }

  @Test public void f() {
    trimmingOf("a+=2;").stays();
  }

  @Test public void g() {
    trimmingOf("a/=1;").stays();
  }

  @Test public void k() {
    trimmingOf("a-=2;").stays();
  }

  @Test public void m() {
    trimmingOf("s = \"hello\"; \n" + "s += 1;").stays();
  }
  
  @Test public void o() {
    trimmingOf("for(int a ; a<3 ; a+=1){}").stays();
  }
}
