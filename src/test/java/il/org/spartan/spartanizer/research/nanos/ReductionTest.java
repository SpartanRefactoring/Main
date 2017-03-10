package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Reduction}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-01 */
@SuppressWarnings("static-method")
public class ReductionTest {
  @Test public void a() {
    trimmingOf("void foo(){ foo(a);}")//
        .using(MethodInvocation.class, new Reduction())//
        .gives("void foo(){reduce¢(a);}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("void foo(){ return foo(a) + foo(a,b);}")//
        .using(MethodInvocation.class, new Reduction())//
        .gives("void foo(){ return reduce¢(a) + reduce¢(a,b);}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("void foo(){ return foo(a) + foo(a,b) == 7 ? foo() : bar();}")//
        .using(MethodInvocation.class, new Reduction())//
        .gives("void foo(){ return reduce¢(a) + reduce¢(a,b) == 7 ? foo() : bar();}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("void foo(int a){ return foo(a) + foo(a,b) == 7 ? foo() : barar();}")//
        .using(MethodInvocation.class, new Reduction())//
        .gives("void foo(int a){ return foo(a) + reduce¢(a,b) == 7 ? reduce¢() : barar();}")//
        .gives("void foo(int a){ return foo(a) + reduce¢(a,b) != 7 ? barar() : reduce¢();}")//
        .stays();
  }
}
