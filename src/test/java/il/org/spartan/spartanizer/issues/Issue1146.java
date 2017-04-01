package il.org.spartan.spartanizer.issues;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

/** see Github issue thus numbered for more info unit tests for
 * {@link InitializerIncDecrementInline}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
@SuppressWarnings("static-method")
@Ignore("in developments")
public class Issue1146 {
  @Test public void a() {
    trimmingOf("int i = 1; ++i;")//
        .gives("int i = 1 + 1;")//
    ;
  }

  @Test public void b() {
    trimmingOf("int count = packed.charAt(i++), value = packed.charAt(i++);--value;f(count++);")//
        .gives("int count = packed.charAt(i++), value = packed.charAt(i++)-1;f(count++);")//
    ;
  }
  
  @Test public void c() {
    trimmingOf("int x = 1, y = x; ++x;")//
    .stays();
  }
}
