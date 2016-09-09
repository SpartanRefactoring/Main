/**
 *
 */
package il.org.spartan.bench.operations;

import il.org.spartan.bench.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since 26/04/2011 */
public abstract class Bencheon extends NamedOperation {
  public final int size;

  /** Instantiate {@link Bencheon}.
   * @param name name of this object
   * @param size size parameter of this object */
  public Bencheon(final String name, final int size) {
    super(name);
    this.size = size;
  }

  public void beforeAllRuns() {
    // Empty
  }

  public void beforeEachRun() {
    // Empty
  }

  public long netRunTime(final int runs) {
    final long beforeEachMethodTime = beforeEachMethodTime(runs);
    final long begin = System.nanoTime();
    for (int i = 0; i < runs; i++) {
      beforeEachRun();
      call();
    }
    return System.nanoTime() - begin - beforeEachMethodTime;
  }

  @Override public final StopWatch netTime(final StopWatch netTime) {
    beforeAllRuns();
    beforeEachRun();
    netTime.start();
    call();
    netTime.stop();
    return netTime;
  }

  @Override public final StopWatch netTime(final StopWatch netTime, final int runs) {
    beforeAllRuns();
    long netRunTime;
    do
      netRunTime = netRunTime(runs);
    while (netRunTime <= 0);
    return netTime.setTime(netRunTime).setRuns(runs);
  }

  /** @param runs */
  public final void run(final int runs) {
    for (int i = 0; i < runs; i++) {
      beforeEachRun();
      call();
    }
  }

  long beforeEachMethodTime(final int runs) {
    final long beginBefore = System.nanoTime();
    for (int i = 0; i < runs; i++)
      beforeEachRun();
    final long initTime = System.nanoTime() - beginBefore;
    return initTime;
  }

  public static abstract class Core extends Bencheon {
    public Core(final String name, final int size) {
      super(name, size);
    }

    @Override public final void beforeAllRuns() {
      // Empty
    }

    @Override public final void beforeEachRun() {
      // Empty
    }

    @Override public long netRunTime(final int runs) {
      final long begin = System.nanoTime();
      for (int i = 0; i < runs; i++)
        call();
      return System.nanoTime() - begin;
    }

    @Override final long beforeEachMethodTime(@SuppressWarnings("unused") final int runs) {
      return 0;
    }
  }

  public static class Empty extends Bencheon {
    /** Instantiate {@link Empty}. */
    public Empty() {
      super("Empty", 1);
    }

    @Override public Void call() {
      return null;
    }
  }

  /** A dummy {@link Bencheon}, which does nothing but wait for the specified
   * period of times.
   * @author Yossi Gil
   * @since 31/05/2011 */
  public static final class Exact extends Bencheon {
    /** @param beforeAllRuns2 */
    private static void sleep(final int sleep) {
      for (final long start = System.nanoTime(); System.nanoTime() - start < sleep;)
        ____.nothing();
    }

    public final int beforeAllRuns;
    public final int beforeEachRun;
    public final int run;

    /** Instantiate {@link Exact}.
     * @param beforeAllRuns
     * @param beforeEachRun
     * @param run */
    public Exact(final int beforeAllRuns, final int beforeEachRun, final int run) {
      super("Exact_" + beforeAllRuns + "_" + beforeEachRun + "_" + run, 1);
      this.beforeAllRuns = beforeAllRuns;
      this.beforeEachRun = beforeEachRun;
      this.run = run;
    }

    @Override public void beforeAllRuns() {
      sleep(beforeAllRuns);
    }

    @Override public void beforeEachRun() {
      sleep(beforeEachRun);
    }

    @Override public Void call() {
      sleep(run);
      return null;
    }
  }

  public static class Hash extends Bencheon {
    static int hash(final int h) {
      final int $ = h ^ h >>> 12 ^ h >>> 20;
      return $ ^ $ >>> 4 ^ $ >>> 7;
    }

    int a = 0;

    /** Instantiate {@link Hash}. */
    public Hash() {
      super("Hash", 1);
    }

    @Override public Void call() {
      a = hash(++a);
      return null;
    }
  }
}
