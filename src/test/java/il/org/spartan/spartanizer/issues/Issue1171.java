package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Unit test for GitHub issue thus numbered
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-04-03 */
@SuppressWarnings("static-method")
public class Issue1171 {
  @Test public void inlineArrayInitialization2() {
    trimmingOf("public double[] h(){ " //
        + " final S r=new S(true); " //
        + " for(double[] dxx : ps) " //
        + " r.a(d[0], d[1]); " //
        + " final double[] $={ r.getSlope(), r.g()}; " //
        + " return $; " //
        + "}")
            .gives("public double[] h(){ " //
                + " final S r=new S(true); " //
                + " for(double[] dxx : ps) " //
                + " r.a(d[0], d[1]); " //
                + " return new double[] { r.getSlope(), r.g()}; " //
                + "}");
  }
}
