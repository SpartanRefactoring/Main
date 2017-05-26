package il.org.spartan.bench.operations;

import il.org.spartan.bench.*;

/** @author Yossi Gil
 * @since 30/05/2011 */
public abstract class NamedOperation extends Operation {
  public final String name;

  /** Instantiate {@link NamedOperation}.
   * @param name name of this object */
  public NamedOperation(final String name) {
    this.name = name;
  }
  @Override public final Stopwatch makeStopWatch() {
    return new Stopwatch(name);
  }
}