package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ReturnPreviousTest {
  @Test public void a() {
    trimminKof("int $=value;  value=newValue;  return $;")//
        .using(new ReturnPrevious(), ReturnStatement.class)//
        .gives("return update(value).with(newValue).getOld();")//
        .stays();
  }
}
