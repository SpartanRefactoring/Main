/**
 *
 */
package il.org.spartan.bench;

import static il.org.spartan.bench.Unit.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.bench.LogBook.*;
import il.org.spartan.bench.operations.*;
import il.org.spartan.sequence.*;
import il.org.spartan.utils.*;

/** Defines a set of policies for timing an operation.
 * @author Yossi Gil
 * @since 30/05/2011 */
public enum BenchingPolicy {
  ;
  public static long minWarmup = SECOND / 2;
  private static long benchingTime = SECOND / 10;
  public final static long MAX_TIME = MINUTE;
  public final static int MIN_RUNS = 17;
  public final static int MAX_RUNS = 1 << 30;

  public static void after(final Operation after) {
    if (after != null)
      after.call();
  }

  /** Execute a given operation
   * @param o what to execute
   * @return the average time in nanoseconds for execution (always a positive
   *         number) */
  public static double approximateSteadyStateTime(final Operation o) {
    for (int runs = 1;; runs <<= 1) {
      // JVM.gc();
      final StopWatch grossTime = new StopWatch().start();
      final StopWatch netTime = o.netTime(runs);
      grossTime.stop();
      ____.nonnegative(netTime.time());
      if (new RunRecord(runs, netTime, grossTime).ok())
        return (double) netTime.time() / runs;
    }
  }

  public static long gcCylces(final LogBook.Mutable l, final Bencheon b, final int runs) {
    l.set("operation", b.name).set("size", b.size);
    return gcCylces(l, (Operation) b, runs);
  }

  public static long gcCylces(final LogBook.Mutable l, final Operation o, final int runs) {
    final JVM before = new JVM();
    for (int i = 0; i < runs; i++)
      o.call();
    final long $ = new JVM().gcCycles - before.gcCycles;
    l.set("runs", runs).record($);
    return $;
  }

  public static long getBenchingTime() {
    return benchingTime;
  }

  public final static void go(final LogBook.Mutable l, final Bencheon b) {
    go(l, b.size, b);
  }

  public final static void go(final LogBook.Mutable l, final long size, final NamedOperation o) {
    go(l, o.name, size, o);
  }

  public static void go(final LogBook.Mutable l, final String name, final long size, final Operation o) {
    Log.beginStage("Benchmarking", name + ":" + size);
    l.set("operation", name).set("size", size);
    final TimingEstimator e = TimingEstimator.estimator(o);
    calibrate(e);
    int runs = runs(e.estimate());
    if (JVM.hasCompiler())
      runs = warmup(e, runs);
    measure(l, size, o, runs);
    Log.endStage();
  }

  public static void main(final String[] args) {
    final TEST t = new TEST();
    t.compareHash();
  }

  public static void measure(final LogBook.Mutable l, final Bencheon b, final int initialRuns) {
    l.set("operation", b.name).set("size", b.size);
    measure(l, b.size, b, initialRuns);
  }

  public static int runs(final double time) {
    ____.nonnegative(time);
    ____.positive(time);
    return (int) Math.round(benchingTime / time + 0.5);
  }

  public static int runs(final Operation o) {
    return runs(approximateSteadyStateTime(o));
  }

  public static void setBenchingTime(final double benchingTime) {
    setBenchingTime((long) benchingTime);
  }

  public static void setBenchingTime(final long benchingTime) {
    BenchingPolicy.benchingTime = benchingTime;
  }

  public static void setMIN_WARMUP(final float minWarmup) {
    setMIN_WARMUP((long) minWarmup);
  }

  public static void setMIN_WARMUP(final long minWarmup) {
    BenchingPolicy.minWarmup = minWarmup;
  }

  public static int warmup(final TimingEstimator e, final int initialApproximation) {
    Log.ln("Time approximation at warmup begin:", Unit.formatNanoseconds(e.estimate()));
    Log.beginCompoundStage("Warmup", "[" + thousands(initialApproximation), "runs/iteration]");
    int runs = initialApproximation;
    int failures = 0;
    int iteration = 0;
    double totalTime = 0;
    while (totalTime < minWarmup) {
      Log.ln("Warming up with " + thousands(runs) + " runs", "iteration #" + ++iteration);
      final RunRecord r = e.run(runs);
      if (r == null) {
        failures++;
        runs = Math.max(MIN_RUNS, runs / 10 * 9);
        Log.ln("Selecing new warmup run value: " + thousands(runs));
        continue;
      }
      totalTime += r.netTime;
    }
    Log.endCompoundStage("failures:" + failures, //
        "iterations: " + iteration, //
        "final runs/itertion: " + thousands(runs), //
        "total time", Unit.formatNanoseconds(totalTime));
    Log.ln("Time approximation at warmup end:", Unit.formatNanoseconds(e.estimate()));
    return runs;
  }

  static void measure(final LogBook.Mutable l, final long size, final Operation o, final int initialRuns) {
    int runs = initialRuns;
    for (;;) {
      Log.print("Silence, measuring " + Unit.INTEGER.format(runs) + " runs ... ");
      // JVM.gc();
      final JVM before = new JVM();
      final long time = o.netTime(runs).time();
      final JVM after = new JVM();
      if (before.equals(after)) {
        l.recordNanoseconds(time, size * runs);
        Log.print(Unit.formatNanoseconds(1.0 * time / (size * runs)) + "\n");
        Log.ln("Measurement: " + Unit.formatNanoseconds(time));
        break;
      }
      Log.print("oops, JVM state changed (time was " + Unit.formatNanoseconds(time) + ")\n");
      if (before.jitChange(after)) {
        Log.ln("JIT changed.. retrying");
        continue;
      }
      runs = Math.max(MIN_RUNS, runs * 9 / 10);
      Log.ln("Probably GC change, using new run", thousands(runs));
      Log.ln("Before JVM was: " + before);
      Log.ln("After JVM was: " + after);
    }
  }

  private static double approximateSteadyStateTime(final TimingEstimator e) {
    for (final Sequence s = new Multiplicative(0.3);; s.advance()) {
      final RunRecord r = e.run(s.current(), 5);
      if (r == null || r.ok() || e.steady())
        return tuneupSteadyState(e);
    }
  }

  private static void calibrate(final TimingEstimator e) {
    Log.beginCompoundStage("Calibration");
    approximateSteadyStateTime(e);
    Log.endCompoundStage();
  }

  private static double tuneupSteadyState(final TimingEstimator e) {
    for (int estimatedRuns = runs(e.estimate()); estimatedRuns > 1; estimatedRuns /= 2)
      if (e.run(estimatedRuns) != null)
        break;
    return e.estimate();
  }

  public static class TEST {
    @Test public void benchBencheon0_0_10() {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      for (int i = 0; i < 20; i++)
        go(l, new Bencheon.Exact(0, 0, 10));
      l.printBy(Consolidation.LIST);
    }

    @Test public void benchBencheon0_0_1000() {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      final Bencheon b = new Bencheon.Exact(0, 0, 1000);
      for (int i = 0; i < 1000; i++)
        b.call();
      for (int i = 0; i < 20; i++)
        go(l, b);
      l.printBy(Consolidation.LIST);
    }

    @Test public void compareEmptyHeavyInit() {
      timeBencheon(new Bencheon.Exact(1000, 1000, 0));
    }

    @Test public void compareHash() {
      timeBencheon(new Bencheon.Hash());
      pure(new Bencheon.Hash());
    }

    @Test public void measureBencheon0_0_10() {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      final Bencheon b = new Bencheon.Exact(0, 0, 10);
      for (int i = 0; i < 20; i++) {
        final StopWatch s = new StopWatch();
        s.start();
        b.call();
        s.stop();
        l.set("Run", i).record(s.time());
      }
      l.printBy(Consolidation.LIST);
    }

    @Test public void measureEmpty() {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      for (int i = 0; i < 20; i++) {
        final StopWatch s = new StopWatch();
        s.start();
        s.stop();
        l.set("Type", "Empty").set("Run", i).record(s.time());
      }
      l.printBy(Consolidation.LIST);
    }

    @Test public void measureNothingFunction() {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      for (int i = 0; i < 20; i++) {
        final StopWatch s = new StopWatch();
        s.start();
        ____.nothing();
        s.stop();
        l.set("Type", "Nothing").set("Run", i).record(s.time());
      }
      l.printBy(Consolidation.LIST);
    }

    @Test public void timeExact() {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      l.set("Type", "Exponential Runs");
      final Bencheon.Exact b = new Bencheon.Exact(0, 0, 100);
      for (int j = 0; j < 1000; j++)
        b.call();
      for (int runs = 1; runs < 1 << 20; runs <<= 1) {
        l.set("Runs", runs);
        final StopWatch s = new StopWatch();
        s.start();
        b.run(runs);
        s.stop();
        l.set("Total", s.time());
        l.set("Bencheon", b.name);
        l.set("Mode", "Average time");
        l.recordNanoseconds(s.time() / runs);
        l.set("Mode", "Error");
        l.recordRelative(s.time(), b.run);
        // if (s.time() > 10 * MAX_TIME)
        // break;
      }
      l.printBy(Consolidation.LIST);
    }

    @Test public void timeHash() {
      timeBencheon(new Bencheon.Hash());
    }

    void pure(final Bencheon b) {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      l.set("Type", "BENCH");
      go(l, b);
      l.printBy(Consolidation.LIST);
    }

    void timeBencheon(final Bencheon b) {
      final LogBook.Mutable l = new LogBook.Mutable(this);
      l.set("Type", "Exponential Runs");
      for (int runs = 1; runs < 1 << 20; runs <<= 1) {
        l.set("Runs", runs);
        final StopWatch s = new StopWatch();
        s.start();
        b.run(runs);
        s.stop();
        l.set("Total", s.time());
        l.set("Bencheon", b.name);
        l.set("Mode", "Average time");
        l.recordNanoseconds(s.time() / runs);
        l.set("Mode", "Error");
        // if (s.time() > 10 * MAX_TIME)
        // break;
      }
      l.printBy(Consolidation.LIST, "Mode");
    }
  }
}