package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Ignore;
import org.junit.Test;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-25 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1138 {
  @Test public void test() {
    trimmingOf("int a = 0; int b;a+=1;").gives("int a=0;int b;a++;");
  }
}
