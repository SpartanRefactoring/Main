package il.org.spartan.bench.operations;

import java.util.concurrent.*;

import il.org.spartan.bench.*;

/** A typed procedure with no arguments, which is also suitable for time
 * measurement, i.e., its execution time is sufficiently greater than the time
 * granularity of loop execution, function calls, and the other instructions
 * required for timing. */
public abstract class Operation implements Callable<Object> {
  /** The body of this operation; to be filled in by sub-classes.
   * @return whatever */
  @Override public abstract Object call();
  @SuppressWarnings("static-method") //
  public Stopwatch makeStopWatch() {
    return new Stopwatch();
  }
  public final Stopwatch netTime() {
    return netTime(makeStopWatch());
  }
  public final Stopwatch netTime(final int runs) {
    return netTime(makeStopWatch(), runs);
  }
  public Stopwatch netTime(final Stopwatch netTime) {
    netTime.start();
    call();
    return netTime.stop();
  }
  public Stopwatch netTime(final Stopwatch $, final int runs) {
    $.start();
    for (int ¢ = 0; ¢ < runs; ++¢)
      call();
    return $.stop();
  }
}
