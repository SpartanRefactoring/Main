package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-17 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1067 {
  @Test public void t1() {
    trimmingOf("int zero = 0, result = 8 / zero;" + "++result;").stays();
    // int zero = 1, result = 8;
    // ++result;
    // int a =0, res = 8;
    // f(res);
  }

  @Test public void t2() {
    trimmingOf("int zero = 1, result = 8;" + "++result;").stays();
  }
}
