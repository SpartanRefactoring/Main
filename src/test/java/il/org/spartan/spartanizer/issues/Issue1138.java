package il.org.spartan.spartanizer.issues;

import static org.junit.Assert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-25 */
@Ignore
public class Issue1138 {
  @Test public void test() {
    trimmingOf("int a = 0; int b;a+=1;").gives("int a=0;int b;a++;");
  }
}
