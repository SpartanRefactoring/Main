package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/* Tests {@link CopyCollection}
 *
 * @author orimarco {@code marcovitch.ori@gmail.com}
 *
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class CopyCollectionTest {
  @Test public void a() {
    topDownTrimming("StatsAccumulator $=new StatsAccumulator();  $.addAll(values);")//
        .using(ClassInstanceCreation.class, new CopyCollection())//
        .gives("StatsAccumulator $=Create.from(values);");
  }

  @Test public void b() {
    topDownTrimming("StatsAccumulator<N> $=new StatsAccumulator<>();  $.addAll(values);")//
        .using(ClassInstanceCreation.class, new CopyCollection())//
        .gives("StatsAccumulator<N> $=Create.from(values);");
  }
}
