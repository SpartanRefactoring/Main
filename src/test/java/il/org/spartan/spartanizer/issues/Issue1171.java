package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** TODO Ori Roth: document class
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-26 */
@SuppressWarnings("static-method")
public class Issue1171 {

  @Test public void inlineArrayInitialization2() {
    trimmingOf("public double[] solve(){ " //
        + " final SimpleRegression regress=new SimpleRegression(true); " //
        + " for(double[] dxx : points) " //
        + " regress.addData(d[0], d[1]); " //
        + " final double[] $={ regress.getSlope(), regress.getIntercept()}; " //
        + " return $; " //
        + "}")
            .gives("public double[] solve(){ " //
                + " final SimpleRegression regress=new SimpleRegression(true); " //
                + " for(double[] dxx : points) " //
                + " regress.addData(d[0], d[1]); " //
                + " return new double[] { regress.getSlope(), regress.getIntercept()}; " //
                + "}");
  }
}
