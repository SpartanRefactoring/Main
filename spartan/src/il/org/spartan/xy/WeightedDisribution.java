package il.org.spartan.xy;

import org.eclipse.jdt.annotation.*;

/** @author Yossi Gil
 * @since February 22, 2012 */
public class WeightedDisribution {
  private double sum;
  private double sum2;
  private double sumw;
  private double sumwlogw;

  public void clear() {
    sum = sum2 = sumwlogw = 0;
  }
  public double entropy() {
    return sumw * Math.log(sumw) - sumwlogw;
  }
  public double mean() {
    return sum / sumw;
  }
  public double mean2() {
    return sum2 / sumw;
  }
   public WeightedDisribution record(final double x, final double w) {
    sumw += w;
    sum += w * x;
    sum2 += w * x * x;
    sumwlogw += w * Math.log(w);
    return this;
  }
  public double var() {
    return mean2() - mean() * mean();
  }
}
