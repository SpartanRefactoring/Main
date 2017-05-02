package il.org.spartan.bench;

import org.jetbrains.annotations.*;

import fluent.ly.*;

/** @author Yossi Gil
 * @since 01/05/2011 */
public final class Stopwatch {
  private long time;
  private int runs;
  private long begin;
  private final String name;
  private boolean started;

  public Stopwatch() {
    this(null);
  }

  /** Instantiate {@link Stopwatch}.
   * @param name */
  public Stopwatch(final String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  /** @return <code><b>this</b></code> */
  @NotNull public Stopwatch reset() {
    time = runs = 0;
    return this;
  }

  public int runs() {
    return runs;
  }

  /** @param runs
   * @return <code><b>this</b></code> */
  @NotNull public Stopwatch setRuns(final int runs) {
    this.runs = runs;
    return this;
  }

  /** @param time
   * @return <code><b>this</b></code> */
  @NotNull public Stopwatch setTime(final long time) {
    this.time = time;
    return this;
  }

  /** @return <code><b>this</b></code> */
  @NotNull public Stopwatch start() {
    ___.require(!started);
    begin = System.nanoTime();
    started = true;
    return this;
  }

  /** @return <code><b>this</b></code> */
  @NotNull public Stopwatch stop() {
    ___.require(started);
    time += System.nanoTime() - begin;
    ++runs;
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