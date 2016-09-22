package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link ForRenameInitializerToCent}
 * @author YossiGil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue212 {
  @Test public void chocolate1() {
    trimmingOf("for(int $=0;$<a.length;++$)sum +=$;")//
        .stays();
  }

  @Test public void chocolate2() {
    trimmingOf("for(int i=0, j=0;i<a.length;++j)sum +=i+j;")//
        .stays();
  }

  @Test public void vanilla01() {
    trimmingOf("for(int i=0;i<a.length;++i)sum+=i;")//
        .gives("for(int ¢=0;¢<a.length;++¢)sum+=¢;")//
        .stays();
  }

  @Test public void vanilla02() {
    trimmingOf(//
        " for (int i = 2; i < xs.size(); ++i)\n" + //
            "    step.extendedOperands($).add(duplicate.of(xs.get(i)));"//
    ).gives(//
        " for (int ¢ = 2; ¢ < xs.size(); ++¢)\n" + //
            "    step.extendedOperands($).add(duplicate.of(xs.get(¢)));"//
    ).stays();
  }
}
