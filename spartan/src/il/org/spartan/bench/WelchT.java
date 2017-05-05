/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.bench;

import il.org.spartan.statistics.*;

/** Ad hoc implementation before we use Apache common
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-09-10 */
// TODO: Yossi, this is an older version, you removed the constructor and that's
// broke the build
public class WelchT {
  public double p = 0;
  private final Statistics s1;
  private final Statistics s2;

  public WelchT(Statistics s1, Statistics s2) {
    this.s1 = s1;
    this.s2 = s2;
  }
}
