package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
@SuppressWarnings("static-method")
public class CopyCollectionTest {
  @Test public void a() {
    trimmingOf("StatsAccumulator $=new StatsAccumulator();  $.addAll(values);")//
        .using(Block.class, new CopyCollection())//
        .gives("StatsAccumulator $=Create.from(values);");
  }
}
