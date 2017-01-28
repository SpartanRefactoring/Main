package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class CopyCollectionTest {
  @Test public void a() {
    trimmingOf("StatsAccumulator $=new StatsAccumulator();  $.addAll(values);")//
        .using(Block.class, new CopyCollection())//
        .gives("StatsAccumulator $=Create.from(values);");
  }

  @Test public void b() {
    trimmingOf("StatsAccumulator<N> $=new StatsAccumulator<>();  $.addAll(values);")//
        .using(Block.class, new CopyCollection())//
        .gives("StatsAccumulator<N> $=Create.from(values);");
  }
}
