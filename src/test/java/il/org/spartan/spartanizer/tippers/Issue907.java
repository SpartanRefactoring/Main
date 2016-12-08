package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
/** ?? TODO: Yuval Simon which tipper is tested?
 * @author Yuval Simon 
 * @year 2016-12-08 */
@Ignore
@SuppressWarnings("static-method")
public class Issue907 {
  @Test public void issue086_1() {
    trimmingOf("if(false)" + "c();\n" + "int a;").gives("if(false)" + "c();\n").gives("{}").gives("").stays();
  }
}
