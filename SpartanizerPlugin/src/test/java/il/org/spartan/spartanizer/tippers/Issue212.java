package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link ForRenameInitializerToCent}
 * @author YossiGil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public final class Issue212 {
  @Test @Ignore("Pending Issue") public void chocolate1() {
    trimmingOf("for(int $=0;$<a.length;++$)sum +=$;")//
        .gives("for(int $=0;$<a.length;++$,sum +=$);").stays();
  }

  @Test @Ignore("Pending Issue") public void chocolate2() {
    trimmingOf("for(int i=0, j=0;i<a.length;++j)sum +=i+j;")//
        .gives("for(int i=0, j=0;i<a.length;++j,sum +=i+j);").stays();
  }

  @Test public void vanilla01() {
    trimmingOf("for(int i=0;i<a.length;++i)if(a)sum+=i;")//
        .gives("for(int ¢=0;¢<a.length;++¢)if(a)sum+=¢;")//
        .stays();
  }
}
