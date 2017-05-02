package il.org.spartan.bench;

import static il.org.spartan.bench.Unit.*;
import static il.org.spartan.strings.StringUtils.*;
import static fluent.ly.___.*;

import java.util.*;

import org.jetbrains.annotations.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.bench.operations.*;

/** Manages estimates on the runtime of {@link Operation}s. Duties include:
 * <ol>
 * <li>computing an initial estimate based on a specified number of runs;
 * <li>updating the estimate based on further runs;
 * <li>implementation of a heuristic to determine whether the last runs indicate
 * that the timing estimate is stable;
 * <li>caching these estimates
 * <ol>
 * @author Yossi Gil
 * @since 2011-08-04 */
public class TimingEstimator {
  private static final IdentityHashMap<Operation, TimingEstimator> estimators = new IdentityHashMap<>();
  private static final double MAX_DEVIATION = 15 * PERCENT;
  private static final int MIN_CONSECUTIVE_STABLE = 5;

  /** Factory method retrieving the {@link TimingEstimator} associated with a
   * given {@link Operation}, creating a new such object when no association was
   * previously done.
   * @param ¢ arbitrary operation
   * @return the {@link TimingEstimator} associated with the parameter */
  @NotNull public static TimingEstimator estimator(final Operation ¢) {
    final TimingEstimator $ = estimators.get(¢);
    return $ != null ? $ : makeEstimator(¢);
  }

  /** Execute a given operation a specified number of times, updating the time
   * estimate associated with it. Garbage collection cycles during the execution
   * are considered failure, in which case, the estimate is not updated.
   * However, JIT execution during execution shall provoke a rerun, until an
   * execution in which class {@link JVM} cannot detect a JIT intervention.
   * @param o an arbitrary operation
   * @param runs number of executions */
  public static void run(final Operation o, final int runs) {
    estimator(o).run(runs);
  }

  @NotNull private static TimingEstimator makeEstimator(final Operation ¢) {
    @NotNull final TimingEstimator $ = new TimingEstimator(¢);
    estimators.put(¢, $);
    return $;
  }

  private final Operation o;
  private final RunRecordAccumulator usefulRuns = new RunRecordAccumulator();
  private final RunRecordAccumulator totalRuns = new RunRecordAccumulator();
  private int consecutiveStable;
  private double estimate = Double.NaN;
  private int n;

  private TimingEstimator(final Operation o) {
    this.o = o;
  }

  /** @return current estimate, measured in nano-seconds, of the runtime of the
   *         underlying operation. */
  public double estimate() {
    return Double.isNaN(estimate) ? 1 : estimate;
  }

  /** Make a candid attempt to execute the underlying operation a specified
   * number of times, updating its current time estimate. Garbage collection
   * cycles during the execution are considered failure, but any JIT execution
   * during the run shall provoke a rerun, until an execution in which class
   * {@link JVM} cannot detect a JIT execution.
   * @param runs how many times should the underlying operation be executed
   * @return <code><b>null</b></code> if the execution failed, i.e., there was a
   *         garbage collection cycle within this execution. Otherwise, a
   *         {@linkplain RunRecord} object. */
  @Nullable public RunRecord run(final int runs) {
    for (;;) {
      Log.print("Running " + thousands(runs) + " times...");
      @NotNull final JVM before = new JVM();
      @NotNull final Stopwatch grossTime = new Stopwatch().start(), netTime = o.netTime(runs);
      grossTime.stop();
      @NotNull final JVM after = new JVM();
      nonnegative(netTime.time());
      @NotNull final RunRecord $ = new RunRecord(runs, grossTime, netTime);
      totalRuns.add($);
      if (!after.equals(before)) {
        Log.print("oops, JVM state changed... " + netTime);
        Log.ln("Before JVM was: " + before);
        Log.ln("After JVM was: " + after);
        if (after.jitChange(before)) {
          Log.ln("There was a jit change, retrying until JIT stabilizes");
          continue;
        }
        Log.ln("GC problem, aborting this run");
        Log.ln(JVM.status());
        return null;
      }
      usefulRuns.add($);
      positive($.estimate());
      consecutiveStable += as.bit(delta(estimate, $.estimate()) < MAX_DEVIATION);
      final double previous = estimate;
      updateEstimate($.estimate());
      Log.f("time estimate=%s (current) %s (overall) %s (previous) %s", //
          Unit.NANOSECONDS.format($.estimate()), //
          Unit.NANOSECONDS.format(estimate), //
          Unit.NANOSECONDS.format(previous), //
          "");
      positive(estimate);
      return $;
    }
  }

  /** Make a candid attempt to execute the underlying operation a specified
   * number of times, updating its current time estimate, while trying to defeat
   * errors due to <i>both</i> garbage collection cycles and JIT meddling.
   * <p>
   * JIT execution during the run shall provoke a rerun, until an execution in
   * which class {@link JVM} cannot detect a JIT execution.
   * <p>
   * Garbage collection cycles detected during execution are also considered
   * failure, and will provoke a re-execution, but the number of such
   * re-executions is bounded by a parameter.
   * <p>
   * Observe that if the number of runs is sufficiently large, a garbage
   * collection cycle will always occur during the execution.
   * @param runs how many times should the underlying operation be executed
   * @param trials number of trials to defeat garbage collection
   * @return <code><b>null</b></code> if the execution failed, i.e., there was a
   *         garbage collection cycle within this execution. Otherwise, a
   *         {@linkplain RunRecord} object. */
  @Nullable public RunRecord run(final int runs, final int trials) {
    for (int ¢ = 0; ¢ < trials; ++¢) {
      @Nullable final RunRecord $ = run(runs);
      if ($ != null)
        return $;
      System.gc();
    }
    return null;
  }

  /** @return <code><b>true</b></code> <i>iff</i> recent runs indicate that the
   *         current estimate is stable. */
  public boolean steady() {
    return consecutiveStable > MIN_CONSECUTIVE_STABLE;
  }

  @Override public String toString() {
    return String.format(//
        "Total:  %s\n" + //
            "Useful: %s\n" + //
            "Efficiency: runs=%s netTime=%s grossTime=%s\n" + //
            "Estimate=%s", //
        totalRuns, //
        usefulRuns, //
        Unit.formatRelative(usefulRuns.runs(), totalRuns.runs()), //
        Unit.formatRelative(usefulRuns.netTime(), totalRuns.netTime()), //
        Unit.formatRelative(usefulRuns.grossTime(), totalRuns.grossTime()), //
        Unit.NANOSECONDS.format(estimate) //
    );
  }

  private void updateEstimate(final double update) {
    ++n;
    if (Double.isNaN(estimate)) {
      estimate = update;
      return;
    }
    final int m = Math.min(n, 5);
    estimate = (update + estimate * (m - 1)) / m;
  }
}