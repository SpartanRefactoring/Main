package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
/** ?? TODO: Yuval Simon which tipper is tested?
 * @author Yuval Simon 
 * @year 2016-12-08 */
@Ignore
@SuppressWarnings("static-method")
public class Issue906 {
  @Test public void issue075h() {
    trimmingOf("int i; i = +0;").gives("int i = +0;").gives("");
  }
}
