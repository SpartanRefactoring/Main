package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link ForRenameInitializerToCent}
 * @author YossiGil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue212Test {
  @Test public void vanilla() {
    trimming("for(int i=0;i<a.length;++i)sum +=i;")//
    .to("for(int ¢ = 0;¢<a.length;++¢)sum+=¢;")//
    .stays();
  }
}
