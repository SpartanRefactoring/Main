/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.bench;

import il.org.spartan.statistics.ImmutableStatistics;
import il.org.spartan.statistics.Statistics;

/** Ad hoc implementation before we use Apache common
 *
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-09-10 */
// TODO: Yossi, this is an older version, you removed the constructor and that's
// broke the build
public class WelchT {
  /** Instantiates this class. @param s1 /** Instantiates this class. @param s2 */
  public WelchT(final ImmutableStatistics s1, final ImmutableStatistics s2) {
    // TODO Auto-generated constructor stub
  }

  /** Instantiates this class. @param s1 /** Instantiates this class. @param s2 */
  public WelchT(final Statistics s1, final Statistics s2) {
    // TODO Auto-generated constructor stub
  }

  public double p;
}
