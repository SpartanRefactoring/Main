package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO:  Doron Meshulam
 please add a description 
 @author Doron Meshulam
 * @since 08-Dec-2016 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0182 {
  @Test public void dollar() {
    trimmingOf("int toString() { int x = 5; System.out.println(x); return x + 7;}")
        .gives("int toString() { int $ = 5; System.out.println($); return $ + 7;}");
  }
}

