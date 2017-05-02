package il.org.spartan.misc;

/** @author Yossi Gil
 * @since Feb 29, 2012 */
public abstract class ComputeOnce {
  private double value = Double.NaN;

  public final double value() {
    return !Double.isNaN(value) ? value : (value = compute());
  }

  protected abstract double compute();
}
