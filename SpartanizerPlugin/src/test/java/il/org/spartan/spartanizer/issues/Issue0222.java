package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for centification of a single parameter to a function even if it
 * defines a "$"variable
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0222 {
  @Test public void chocolate1() {
    trimmingOf("L<E> os(I x){if(x==null)return null;int y=x;f(++y); L<E> $=new A<>();$.a(l(x));$.a(r(x));if(x.h()) $.d(s.es(x));return $;}")//
        .stays();
  }

  @Test public void chocolate2() {
    trimmingOf("tc¢(L<F> ifs){for(F $ : fs){int b=f($);f($,b);return g($,b,f());}return true;}")//
        .stays();
  }

  @Test public void chocolate3() {
    trimmingOf("int f(int i){for(int b: fs)return 0;return 1;}").gives("int f(int __){for(int b: fs)return 0;return 1;}")//
        .stays();
  }

  /** Introduced by Yogi on Thu-Mar-30-09:08:47-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_aBaCbIfbNullReturnNullABcNewDcdebcdfbIfbgchijbReturnc() {
    trimmingOf("A<B>a(C b){if(b==null)return null;A<B>c=new D<>();c.d(e(b));c.d(f(b));if(b.g())c.h(i.j(b));return c;}") //
        .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
        .gives("A<B>a(C b){if(b==null)return null;A<B>$=new D<>();$.d(e(b));$.d(f(b));if(b.g())$.h(i.j(b));return $;}") //
        .stays() //
    ;
  }
}
