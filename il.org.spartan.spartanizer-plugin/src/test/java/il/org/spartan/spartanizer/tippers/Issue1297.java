package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;

/** Unit tests for version 2.90
 * @author Yossi Gil
 * @since 2017-04-27 */
@SuppressWarnings("static-method")
public class Issue1297 {
  @Test public void t0() {
    trimmingOf("int a=3;a=f()? 3 : 4;")//
        .gives("int a=f()? 3: 4;");
  }
  @Test public void t1() {
    trimmingOf("int a=2;a=3 * a * b;")//
        .gives("int a=3 * 2 * b;");
    trimmingOf("int a=2;a=3 * a;")//
        .gives("int a=3 * 2;");
    trimmingOf("int a=2;a +=3;")//
        .gives("int a=2 + 3;");
    trimmingOf("int a=2;a +=b;")//
        .gives("int a=2 + b;");
    trimmingOf("int a=2, b=11;a=3 * a * b;")//
        .gives("int a=2;a=3*a*11;")//
        .gives("int a=3*2*11;");//
    trimmingOf("int a=2, b=1;a +=b;")//
        .gives("int a=2;a+=1;")//
        .gives("int a=2+1;");
    trimmingOf("int a=2,b=1;if(b)a=3;")//
        .gives("int a=2;if(1)a=3;")//
        .gives("int a=1?3:2;");
    trimmingOf("int a=2, b=1;return a + 3 * b;")//
        .gives("int b=1;return 2+3*b;");
    trimmingOf("int a=2, b;a=3 * a * b;")//
        .gives("int a=2, b;a *=3 * b;")//
        .stays();
    trimmingOf("int a=2, b;a +=b;")//
        .stays();
    trimmingOf("int a=2, b;return a + 3 * b;")//
        .gives("return 2 + 3*b;");
    trimmingOf("int a=2;if(x)a=3*a;")//
        .gives("int a=x?3*2:2;");
    trimmingOf("int a=2;return 3 * a * a;")//
        .gives("return 3 * 2 * 2;");
    trimmingOf("int a=2;return 3 * a * b;")//
        .gives("return 3 * 2 * b;");
    trimmingOf("int a=2;return a;")//
        .gives("return 2;");
  }
  @Test public void t2() {
    trimmingOf("int a;if(x)a=3;else a++;")//
        .gives("int a;if(x)a=3;else++a;");
    trimmingOf("int b=5,a=2,c=4;return 3 * a * b * c;")//
        .gives("int a=2,c=4;return 3*a*5*c;");
    trimmingOf("int b=5,a=2,c;return 3 * a * b * c;")//
        .gives("int a=2;return 3 * a * 5 * c;");
  }
  @Test public void t3() {
    trimmingOf("int a, b = 2; a = b;") //
        .gives("int a; a =2;") //
        .gives("int a = 2;") //
        .gives("") //
    ;
  }
  /** Introduced by Yossi on Thu-Apr-27-21:36:06-IDT-2017 (code generated
   * automatically by {@link JUnitTestMethodFacotry}) */
  @Test public void t4() {
    trimmingOf("int a, b = 2; a = b;") //
        .using(VariableDeclarationFragment.class, new LocalInitializedStatementTerminatingScope()) //
        .gives("int a;a=2;") //
        .using(VariableDeclarationFragment.class, new LocalUninitializedAssignment()) //
        .gives("int a=2;") //
        .gives("") //
    ;
  }
  @Test public void t5() {
    trimmingOf("String tipper=Z2;tipper=tipper.f(A).f(b)+ tipper.f(c);return(tipper + 3);")
        .gives("String tipper=Z2.f(A).f(b)+ Z2.f(c);return(tipper + 3);");
  }
  @Test public void t6() {
    trimmingOf("int[] a=new int[] {2,3};")//
        .gives("");
  }
  @Test public void t7() {
    trimmingOf("String u=m,foo=GY;print(x);if(u.equals(f())==true){foo=M;int k=2;k=8;S.h(foo);}f();")
        .gives("String u=m,foo=GY;print(x);if(u.equals(f())){foo=M;int k=8;S.h(foo);}f();");
  }
  @Test public void t8() {
    trimmingOf("int a;a=3;")//
        .gives("int a=3;");
    trimmingOf("int a=2;if(b)a=3;")//
        .gives("int a=b ? 3 : 2;");
    trimmingOf("int a=2;a +=3;")//
        .gives("int a=2 + 3;");
    trimmingOf("int a=2;a=3 * a;")//
        .gives("int a=3 * 2;");
    trimmingOf("int a=2;return 3 * a;")//
        .gives("return 3 * 2;");
    trimmingOf("int a=2;return a;")//
        .gives("return 2;");
  }
}
