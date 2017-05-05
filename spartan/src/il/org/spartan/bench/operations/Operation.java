package il.org.spartan.bench.operations;

import java.util.concurrent.*;

import org.jetbrains.annotations.*;

import il.org.spartan.bench.*;

/** A typed procedure with no arguments, which is also suitable for time
 * measurement, i.e., its execution time is sufficiently greater than the time
 * granularity of loop execution, function calls, and the other instructions
 * required for timing. */
public abstract class Operation implements Callable<Object> {
  /** The body of this operation; to be filled in by sub-classes.
   * @return whatever */
  @Override @Nullable public abstract Object call();
  @NotNull @SuppressWarnings("static-method") //
  public Stopwatch makeStopWatch() {
    return new Stopwatch();
  }
  @NotNull public final Stopwatch netTime() {
    return netTime(makeStopWatch());
  }
  @NotNull public final Stopwatch netTime(final int runs) {
    return netTime(makeStopWatch(), runs);
  }
  @NotNull public Stopwatch netTime(@NotNull final Stopwatch netTime) {
    netTime.start();
    call();
    return netTime.stop();
  }
  @NotNull public Stopwatch netTime(@NotNull final Stopwatch $, final int runs) {
    $.start();
    for (int ¢ = 0; ¢ < runs; ++¢)
      call();
    return $.stop();
  }
}
