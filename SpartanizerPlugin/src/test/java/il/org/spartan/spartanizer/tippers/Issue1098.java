package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;

/** Test case for bug in {@link SwitchWithOneCaseToIf}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-20 */
@SuppressWarnings("static-method")
public class Issue1098 {
  @Test public void t1() {
    trimmingOf("int a() {" + "switch (¢ + \"\") {" + "case \"Object\":" + "case \"java.lang.Object\":" + "return true;" + "default:" + "return false;"
        + "}" + "}").stays();
  }
}
