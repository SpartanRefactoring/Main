package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Test class for @link{ForWithEndingBreakToDoWhile}
 * @author Doron Mehsulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-03-26 */
@Ignore("Doron Meshuleam - please fix the tests")
@SuppressWarnings("static-method")
public class Issue1122 {
  @Test public void a() {
    trimmingOf("for(int i=0;i<10;){x = x+5; if(i > 5 && i < 9) break;}")//
        .gives("do{x = x+5;} while(i <= 5 || i>=9);");
  }

  @Test public void b() {
    trimmingOf("for(int i=0;i<10;++i){y=y*x; x = x+5; if(doSomething()) break;}")//
        .gives("do{y=y*x; x = x+5;} while(!doSomething());");
  }

  @Test public void c() {
    trimmingOf("for(int i=0;i<10;++i) if(doSomething()) break;")//
        .gives("do{} while(!doSomething());");
  }

  @Test public void d() {
    trimmingOf("for (int a = 0; a < 10; ++a) if (b()) return;")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("for (int a = 0; a < 10; ++a) if (b()) { return; }")//
        .gives("for(int a=0;a<10;++a)if(b())return;")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("for(int i=0;i<10;++i) if(doSomething()) { break; }")//
        .stays();
  }
}
