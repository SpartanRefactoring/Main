/**
 *
 */
package il.org.spartan.bench.operations;

import il.org.spartan.bench.*;

/** @author Yossi Gil
 * @since 26/04/2011 */
public abstract class TimedOperation extends NamedOperation {
  /** Instantiate {@link TimedOperation}.
   * @param name */
  public TimedOperation(final String name) {
    super(name);
  }

  @Override public final StopWatch call() {
    return run(makeStopWatch());
  }

  @Override public StopWatch netTime(final StopWatch netTime) {
    return run(netTime);
  }

  @Override public StopWatch netTime(final StopWatch netTime, final int runs) {
    for (int i = 0; i < runs; i++)
      run(netTime);
    return netTime;
  }

  public abstract StopWatch run(StopWatch s);
}
