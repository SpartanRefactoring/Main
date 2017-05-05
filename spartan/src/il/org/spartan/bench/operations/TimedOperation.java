package il.org.spartan.bench.operations;

import org.jetbrains.annotations.*;

import il.org.spartan.bench.*;

/** @author Yossi Gil
 * @since 26/04/2011 */
public abstract class TimedOperation extends NamedOperation {
  /** Instantiate {@link TimedOperation}.
   * @param name */
  public TimedOperation(final String name) {
    super(name);
  }
  @Override @NotNull public final Stopwatch call() {
    return run(makeStopWatch());
  }
  @Override @NotNull public Stopwatch netTime(final Stopwatch netTime) {
    return run(netTime);
  }
  @Override @NotNull public Stopwatch netTime(final Stopwatch $, final int runs) {
    for (int ¢ = 0; ¢ < runs; ++¢)
      run($);
    return $;
  }
  @NotNull public abstract Stopwatch run(Stopwatch s);
}
