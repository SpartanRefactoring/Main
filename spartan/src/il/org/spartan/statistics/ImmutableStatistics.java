/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.bench.Unit.*;

import java.util.*;

import il.org.spartan.bench.*;

/** @author Yossi Gil
 * @since 30/04/2011 */
public abstract class ImmutableStatistics extends Statistics implements java.io.Serializable {
  /** A field for identifying a streamed version of objects of this class; we
   * use the values of <code>1L</code> to maintain upward compatibility. */
  private static final long serialVersionUID = 1L;

  private static StringBuilder appendValue(final StringBuilder sb, final String name, final double v, final Unit u) {
    return sb.append(name).append('=').append(u.format(v));
  }

  private static void appendValue(final StringBuilder sb, final String name, final int n) {
    sb.append(name).append('=').append(n);
  }

  protected Unit unit;
  protected int flips = 0;
  protected double[] values = new double[0];

  /** Generate a copy of the set of all recorded values
   * @return an array containing all recorded values */
  public final double[] all() {
    return Arrays.copyOf(values, n);
  }

  public double flipping() {
    return (double) flips / n;
  }

  public String format() {
    return format(unit != null ? unit : Unit.DOUBLE);
  }

  public String format(final Unit u) {
    return n() == 1 ? u.format(mean()) : format(u, "A D R N");
  }

  public String format(final Unit u, final String format) {
    if (format == null)
      return format(u);
    final StringBuilder sb = new StringBuilder();
    for (final char c : format.toCharArray())
      switch (c) {
        case 'A':
          appendValue(sb, "mean", mean(), u);
          appendError(sb, relativeError());
          break;
        case 'a':
          appendValue(sb, "mean", mean(), u);
          break;
        case 'J':
          sb.append(u.format(median()));
          appendError(sb, relativeMedianError());
          break;
        case 'j':
          sb.append(u.format(median()));
          break;
        case 'D':
          appendValue(sb, "median", median(), u);
          appendError(sb, relativeMedianError());
          break;
        case 'd':
          appendValue(sb, "median", median(), u);
          break;
        case 'I':
        case 'i':
          appendValue(sb, "min", min(), u);
          break;
        case 'X':
        case 'x':
          appendValue(sb, "max", max(), u);
          break;
        case 'N':
        case 'n':
          appendValue(sb, "n", n());
          break;
        case 'R':
        case 'r':
          sb.append("range").append('=');
          sb.append(u.format(min()));
          sb.append('⋯');
          sb.append(u.format(max()));
          sb.append("]");
          break;
        default:
          sb.append(c);
          break;
      }
    return sb.toString();
  }

  public final double mad() {
    checkEmpty();
    return mad(all());
  }

  public final double median() {
    checkEmpty();
    return median(all());
  }

  /** Prune the set of values to those in the median +- mad value.
   * @return an array representing these values */
  public double[] prune() {
    return prune(all());
  }

  public final double relativeMedianError() {
    return mad() / Math.abs(median());
  }

  public double relativeMinError() {
    return (median() - min()) / min();
  }

  /** Provides the {@link Unit} of measurement used by values recorded in this
   * instance
   * @return a non-negative integer, giving the number of elements in the
   *         sequence */
  public final Unit unit() {
    return unit;
  }

  private StringBuilder appendError(final StringBuilder sb, final double e) {
    return n() <= 1 ? sb : sb.append('±').append(RELATIVE.format(e));
  }
}
