package il.org.spartan.bench.trials;

import il.org.spartan.Log;
import il.org.spartan.bench.BenchingPolicy;
import il.org.spartan.bench.LogBook;
import il.org.spartan.bench.operations.Bencheon;

/** @author Yossi Gil
 * @since 30/05/2011 */
public class BenchEmptyBencheon {
  private static final int trials = 100;

  public static void main(final String args[]) {
    final LogBook.Mutable l = new LogBook.Mutable(BenchEmptyBencheon.class);
    final Bencheon b = new Bencheon("empty", 1) {
      @Override public Object call() {
        return null;
      }
    };
    Log.deactivate();
    for (int ¢ = 0; ¢ < trials; ++¢)
      BenchingPolicy.go(l, b);
    System.err.println(l.currentEntry().format("A D I X"));
  }
}
