package il.org.spartan.bench.operations;

import il.org.spartan.bench.Stopwatch;

/** @author Yossi Gil
 * @since 26/04/2011 */
public abstract class TimedOperation extends NamedOperation {
  /** Instantiate {@link TimedOperation}.
   *
   * @param name */
  public TimedOperation(final String name) {
    super(name);
  }

  @Override public final Stopwatch call() {
    return run(makeStopWatch());
  }

  @Override public Stopwatch netTime(final Stopwatch netTime) {
    return run(netTime);
  }

  @Override public Stopwatch netTime(final Stopwatch $, final int runs) {
    for (int ¢ = 0; ¢ < runs; ++¢)
      run($);
    return $;
  }

  public abstract Stopwatch run(Stopwatch s);
}
