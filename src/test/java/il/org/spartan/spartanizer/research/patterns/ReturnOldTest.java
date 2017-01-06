package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ReturnOldTest {
  @Test public void a() {
    trimmingOf("int $=value;  value=newValue;  return $;")//
        .using(Block.class, new ReturnOld())//
        .gives("return update(value).with(newValue).getOld();")//
        .stays();
  }
}
