package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link OverloadingDelegation}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-01 */
@SuppressWarnings("static-method")
public class OverloadingDelegationTest {
  @Test public void a() {
    trimmingOf("void foo(){ foo(a);}")//
        .using(MethodInvocation.class, new OverloadingDelegation())//
        .gives("void foo(){self(a);}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("void foo(){ return foo(a) + foo(a,b);}")//
        .using(MethodInvocation.class, new OverloadingDelegation())//
        .gives("void foo(){ return self(a) + self(a,b);}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("void foo(){ return foo(a) + foo(a,b) == 7 ? foo() : bar();}")//
        .using(MethodInvocation.class, new OverloadingDelegation())//
        .gives("void foo(){ return self(a) + self(a,b) == 7 ? foo() : bar();}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("void foo(int a){ return foo(a) + foo(a,b) == 7 ? foo() : barar();}")//
        .using(MethodInvocation.class, new OverloadingDelegation())//
        .gives("void foo(int a){ return foo(a) + self(a,b) == 7 ? self() : barar();}")//
        .stays();
  }
}
