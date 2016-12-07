package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ReturnOldTest {
  @Test public void a() {
    trimmingOf("int $=value;  value=newValue;  return $;").withTipper(Block.class, new ReturnOld()).gives("return update(value).with(newValue).getOld();");
  }
}
