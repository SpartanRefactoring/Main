package il.org.spartan.bench.operations;

import fluent.ly.*;
import org.jetbrains.annotations.*;

import il.org.spartan.bench.*;

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
    final long $ = beforeEachMethodTime(runs), begin = System.nanoTime();
    for (int ¢ = 0; ¢ < runs; ++¢) {
      beforeEachRun();
      call();
    }
    return System.nanoTime() - $ - begin;
  }
  @Override @NotNull public final Stopwatch netTime(@NotNull final Stopwatch netTime) {
    beforeAllRuns();
    beforeEachRun();
    netTime.start();
    call();
    netTime.stop();
    return netTime;
  }
  @Override public final Stopwatch netTime(@NotNull final Stopwatch netTime, final int runs) {
    beforeAllRuns();
    long $;
    do
      $ = netRunTime(runs);
    while ($ <= 0);
    return netTime.setTime($).setRuns(runs);
  }
  public final void run(final int runs) {
    for (int ¢ = 0; ¢ < runs; ++¢) {
      beforeEachRun();
      call();
    }
  }
  long beforeEachMethodTime(final int runs) {
    final long $ = System.nanoTime();
    for (int ¢ = 0; ¢ < runs; ++¢)
      beforeEachRun();
    return System.nanoTime() - $;
  }

  public abstract static class Core extends Bencheon {
    public Core(final String name, final int size) {
      super(name, size);
    }
    @Override public final void beforeAllRuns() {
      //
    }
    @Override public final void beforeEachRun() {
      //
    }
    @Override public long netRunTime(final int runs) {
      final long $ = System.nanoTime();
      for (int ¢ = 0; ¢ < runs; ++¢)
        call();
      return System.nanoTime() - $;
    }
    @Override final long beforeEachMethodTime(final int runs) {
      return 0;
    }
  }

  public static class Empty extends Bencheon {
    /** Instantiate {@link Empty}. */
    public Empty() {
      super("Empty", 1);
    }
    @Override @Nullable public Void call() {
      return null;
    }
  }

  /** A dummy {@link Bencheon}, which does nothing but wait for the specified
   * period of times.
   * @author Yossi Gil
   * @since 31/05/2011 */
  public static final class Exact extends Bencheon {
    private static void sleep(final int sleep) {
      for (final long start = System.nanoTime(); System.nanoTime() - start < sleep;)
        ___.nothing();
    }

    public final int beforeAllRuns;
    public final int beforeEachRun;
    public final int run;

    /** Instantiate {@link Exact}.
     * @param beforeAllRuns
     * @param beforeEachRun
     * @param run */
    public Exact(final int beforeAllRuns, final int beforeEachRun, final int run) {
      super("Exact__" + beforeAllRuns + "__" + beforeEachRun + "__" + run, 1);
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
      return h ^ h >>> 12 ^ h >>> 20 ^ (h ^ h >>> 12 ^ h >>> 20) >>> 4 ^ (h ^ h >>> 12 ^ h >>> 20) >>> 7;
    }

    int a;

    /** Instantiate {@link Hash}. */
    public Hash() {
      super("Hash", 1);
    }
    @Override @Nullable public Void call() {
      a = hash(++a);
      return null;
    }
  }
}
