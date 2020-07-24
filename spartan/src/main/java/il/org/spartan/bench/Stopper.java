// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.bench;

import il.org.spartan.streotypes.Instantiable;

/** A class serving as stopwatch for measuring runtime of programs. To start a
 * stop watch, create an instance of this class. Method {@link #peep()} can be
 * used to determine the time elapsed since creation, while method
 * {@link #stop()} can be used to stop the timer. Measurement is carried out in
 * milliseconds.
 * <p>
 * This class also provides the means for repeated measurements of the
 * phenomenon. To do that, call {@link #nextCase()} at the end of every
 * occurrence of the phenomenon. Function {@link #average()} then gives the
 * average time of each such occurrence.
 *
 * @author Yossi Gil,
 * @since 18/06/2008 */
@Instantiable public class Stopper {
  private static boolean mute;

  /** Silence all stoppers from now on. */
  public static void mute() {
    mute = true;
  }

  /** Allow stoppers to print their output from now on. */
  public static void unmute() {
    mute = false;
  }

  private static void out(final String m) {
    if (!mute)
      System.err.println(m);
  }

  private long begin;
  private int cases = 1;
  private long time;
  private final String what;

  /** Create a new instance */
  public Stopper() {
    this(null);
  }

  /** Create a new instance distinguishable by a descriptive string, and print a
   * log message.
   *
   * @param what a textual description of this instance, used in printouts */
  public Stopper(final String what) {
    this.what = what;
    if (what != null)
      out("Started " + what);
    begin = System.nanoTime();
  }

  /** @return the time since creation, per cases. */
  public String average() {
    return peep() / 1E9 / cases + " sec";
  }

  /** @return the number of cases recorded so far. */
  public int cases() {
    return cases;
  }

  /** Used for measuring multiple (similar) events with the same stopper, this
   * method records a new case. */
  public void nextCase() {
    ++cases;
  }

  /** @return the time elapsed since creation. */
  public long peep() {
    return System.nanoTime() - begin;
  }

  public void reset() {
    if (what != null)
      out("Restarted " + what);
    begin = System.nanoTime();
  }

  /** Stop the timer, and print a log message with the time elapsed since
   * creation.
   *
   * @return <code><b>this</b></code> */
  public Stopper stop() {
    time = System.nanoTime() - begin;
    out("Finished " + what + ": " + time + "ns");
    return this;
  }

  /** Stop the stopper (if it was not stopped already), and return the time, i
   * milliseconds recorded on it.
   *
   * @return the time recorded on the stopper when it was stopped. */
  public long time() {
    if (time == 0)
      time = System.nanoTime() - begin;
    return time;
  }

  @Override public String toString() {
    return peep() / 1E9 + " sec";
  }
}
