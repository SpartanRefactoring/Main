package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
 //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0337 {
  @Test public void t18() {
    trimminKof("while(b==q){int i;double tipper; x=tipper+i;}")//
        .stays();
  }
}
