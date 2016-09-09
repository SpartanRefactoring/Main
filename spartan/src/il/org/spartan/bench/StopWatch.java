/**
 *
 */
package il.org.spartan.bench;

import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since 01/05/2011 */
public final class StopWatch {
  private long time = 0;
  private int runs = 0;
  private long begin;
  private final String name;
  private boolean started = false;

  public StopWatch() {
    this(null);
  }

  /** Instantiate {@link StopWatch}.
   * @param name */
  public StopWatch(final String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  /** @return <code><b>this</b></code> */
  public StopWatch reset() {
    time = runs = 0;
    return this;
  }

  public int runs() {
    return runs;
  }

  /** @param runs
   * @return <code><b>this</b></code> */
  public StopWatch setRuns(final int runs) {
    this.runs = runs;
    return this;
  }

  /** @param time
   * @return <code><b>this</b></code> */
  public StopWatch setTime(final long time) {
    this.time = time;
    return this;
  }

  /** @return <code><b>this</b></code> */
  public StopWatch start() {
    ____.require(!started);
    begin = System.nanoTime();
    started = true;
    return this;
  }

  /** @return <code><b>this</b></code> */
  public StopWatch stop() {
    ____.require(started);
    time += System.nanoTime() - begin;
    runs++;
    started = false;
    return this;
  }

  public long time() {
    return time;
  }

  @Override public String toString() {
    return Unit.format(this);
  }
}