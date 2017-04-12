package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** Tests {@link MyName}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-01 */
@SuppressWarnings("static-method")
public class MyNameTest {
  @Test public void a() {
    trimminKof("void foo(){ foo(a);}")//
        .using(new MyName(), MethodInvocation.class)//
        .gives("void foo(){reduce¢(a);}")//
        .stays();
  }

  @Test public void b() {
    trimminKof("void foo(){ return foo(a) + foo(a,b);}")//
        .using(new MyName(), MethodInvocation.class)//
        .gives("void foo(){ return reduce¢(a) + reduce¢(a,b);}")//
        .stays();
  }

  @Test public void c() {
    trimminKof("void foo(){ return foo(a) + foo(a,b) == 7 ? foo() : bar();}")//
        .using(new MyName(), MethodInvocation.class)//
        .gives("void foo(){ return reduce¢(a) + reduce¢(a,b) == 7 ? foo() : bar();}")//
        .stays();
  }

  @Test public void d() {
    trimminKof("void foo(int a){ return foo(a) + foo(a,b) == 7 ? foo() : barar();}")//
        .using(new MyName(), MethodInvocation.class)//
        .gives("void foo(int a){ return foo(a) + reduce¢(a,b) == 7 ? reduce¢() : barar();}")//
        .gives("void foo(int a){ return foo(a) + reduce¢(a,b) != 7 ? barar() : reduce¢();}")//
        .stays();
  }
}
