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
  public StopWatch makeStopWatch() {
    return new StopWatch();
  }

  public final StopWatch netTime() {
    return netTime(makeStopWatch());
  }

  public final StopWatch netTime(final int runs) {
    return netTime(makeStopWatch(), runs);
  }

  public StopWatch netTime(final StopWatch netTime) {
    netTime.start();
    call();
    return netTime.stop();
  }

  public StopWatch netTime(final StopWatch netTime, final int runs) {
    netTime.start();
    for (int i = 0; i < runs; i++)
      call();
    return netTime.stop();
  }
}
