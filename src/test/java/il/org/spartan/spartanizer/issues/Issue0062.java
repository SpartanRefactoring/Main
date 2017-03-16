package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit test for the GitHub issue thus numbered. case of inlining into the
 * expression of an enhanced for
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-16 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0062 {
  @Test public void a() {
    trimmingOf("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;i+=1) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;i++) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;++i) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;++i){} return i; }")//
        .stays();
  }
}
