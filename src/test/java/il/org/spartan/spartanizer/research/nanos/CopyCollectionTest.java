package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.research.nanos.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/* Tests {@link CopyCollection}
 *
 * @author orimarco {@code marcovitch.ori@gmail.com}
 *
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class CopyCollectionTest {
  @Test public void a() {
    trimminKof("StatsAccumulator $=new StatsAccumulator();  $.addAll(values);")//
        .using(new CopyCollection(), ClassInstanceCreation.class)//
        .gives("StatsAccumulator $=Create.from(values);");
  }

  @Test public void b() {
    trimminKof("StatsAccumulator<N> $=new StatsAccumulator<>();  $.addAll(values);")//
        .using(new CopyCollection(), ClassInstanceCreation.class)//
        .gives("StatsAccumulator<N> $=Create.from(values);");
  }
}
