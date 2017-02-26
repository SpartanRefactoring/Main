package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/** Tests {@link CopyCollection}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class CopyCollectionTest {
  @Test public void a() {
    trimmingOf("StatsAccumulator $=new StatsAccumulator();  $.addAll(values);")//
        .using(MethodInvocation.class, new CopyCollection())//
        .gives("StatsAccumulator $=Create.from(values);");
  }

  @Test public void b() {
    trimmingOf("StatsAccumulator<N> $=new StatsAccumulator<>();  $.addAll(values);")//
        .using(MethodInvocation.class, new CopyCollection())//
        .gives("StatsAccumulator<N> $=Create.from(values);");
  }
}
