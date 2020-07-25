package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.junit.Test;

/* Tests {@link CopyCollection}
 *
 * @author orimarco {@code marcovitch.ori@gmail.com}
 *
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class CopyCollectionTest {
  @Test public void a() {
    trimmingOf("StatsAccumulator $=new StatsAccumulator();  $.addAll(values);")//
        .using(new CopyCollection(), ClassInstanceCreation.class)//
        .gives("StatsAccumulator $=Create.from(values);");
  }
  @Test public void b() {
    trimmingOf("StatsAccumulator<N> $=new StatsAccumulator<>();  $.addAll(values);")//
        .using(new CopyCollection(), ClassInstanceCreation.class)//
        .gives("StatsAccumulator<N> $=Create.from(values);");
  }
}
