package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link ForRenameInitializerToIt}
 * @author YossiGil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0212 {
  @Test public void chocolate1() {
    trimminKof("for(int $=0;$<a.length;++$)sum +=$;")//
        .stays();
  }

  @Test public void chocolate2() {
    trimminKof("for(int i=0, j=0;i<a.length;++j)sum +=i+j;")//
        .gives("for(int ¢=0, j=0;¢<a.length;++j)sum +=¢+j;")//
        .stays();
  }

  @Test public void vanilla01() {
    trimminKof("for(int i=0;i<a.length;++i)sum+=i;")//
        .gives("for(int ¢=0;¢<a.length;++¢)sum+=¢;")//
        .stays();
  }

  @Test public void vanilla02() {
    trimminKof("for(int i = 2; i <xs.size(); ++i)step.extendedOperands($).add(duplicate.of(xs.get(i)));")
        .gives("for(int ¢ = 2; ¢ <xs.size(); ++¢)step.extendedOperands($).add(duplicate.of(xs.get(¢)));")//
        .stays();
  }
}
