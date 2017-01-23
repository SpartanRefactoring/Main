package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO:  Yossi Gil <tt>yossi.gil@gmail.com</tt>
 please add a description 
 @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-23 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0997 {
  @Test public void a0() {
    trimmingOf("@interface A{final int a=4;}")//
        .gives("@interface A{int a=4;}")//
        .stays();
  }

  @Test public void a1() {
    trimmingOf("@interface A{final int a=4;final char c;}")//
        .gives("@interface A{int a=4;char c;}")//
        .stays();
  }
}

