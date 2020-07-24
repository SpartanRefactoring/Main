package il.org.spartan.bench.trials;

import il.org.spartan.Log;
import il.org.spartan.bench.BenchingPolicy;
import il.org.spartan.bench.LogBook;
import il.org.spartan.bench.operations.Operation;

/** @author Yossi Gil
 * @since 30/05/2011 */
public class BenchEmptyFinalOperation {
  private static final int trials = 100;

  public static void main(final String args[]) throws Exception {
    final LogBook.Mutable l = new LogBook.Mutable(BenchEmptyFinalOperation.class);
    final Operation o = new Operation() {
      @Override public Object call() {
        return null;
      }
    };
    Log.deactivate();
    for (int ¢ = 0; ¢ < trials; ++¢)
      BenchingPolicy.go(l, "empty", 1, o);
    System.err.println(l.currentEntry().format("A D I X"));
  }
}
