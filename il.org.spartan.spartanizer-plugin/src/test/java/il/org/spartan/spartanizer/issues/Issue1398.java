package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Test case for bug in {@link LocalInitializedInlineIntoNext} (occured from {@link step#type} )
 * @author Yuval Simon
 * @since 2017-05-27 */
@SuppressWarnings("static-method")
public class Issue1398 {
  @Test public void t1() {
    trimmingOf("final double vs[]={5,20,40,80,100}; "
        + "Assert.assertEquals(40.0625,correctedSd(vs),1E-4);")
    .gives("Assert.assertEquals(40.0625,correctedSd((new double[]{5,20,40,80,100})),1E-4);");
  }
  @Test public void t2() {
    trimmingOf("final double vs[][] = {{3,4},{5,6}}; "
        + "Assert.assertEquals(40.0625,correctedSd(vs),1E-4);")
    .gives("Assert.assertEquals(40.0625,correctedSd((new double[][]{{3,4},{5,6}})),1E-4);");
  }
  @Test public void t3() {
    trimmingOf("final double vs[][] = {{3,4},{5,6}}; "
        + "Assert.assertEquals(40.0625,correctedSd(vs),1E-4);")
    .gives("Assert.assertEquals(40.0625,correctedSd((new double[][]{{3,4},{5,6}})),1E-4);");
  }
}
