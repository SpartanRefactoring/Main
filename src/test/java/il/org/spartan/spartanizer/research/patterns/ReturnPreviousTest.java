package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ReturnPreviousTest {
  @Test public void a() {
    trimmingOf("int $=value;  value=newValue;  return $;")//
        .using(Block.class, new ReturnPrevious())//
        .gives("return update(value).with(newValue).getOld();")//
        .stays();
  }
}
