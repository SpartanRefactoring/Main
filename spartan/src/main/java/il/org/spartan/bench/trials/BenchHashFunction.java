package il.org.spartan.bench.trials;

import il.org.spartan.Log;
import il.org.spartan.bench.BenchingPolicy;
import il.org.spartan.bench.LogBook;
import il.org.spartan.bench.operations.Operation;

/** @author Yossi Gil
 * @since 30/05/2011 */
public class BenchHashFunction {
  private static final int trials = 100;
  public static int size = 9;

  public static void main(final String args[]) throws Exception {
    final LogBook.Mutable l = new LogBook.Mutable(BenchHashFunction.class);
    final Hash h = new Hash();
    Log.deactivate();
    for (int ¢ = 0; ¢ < trials; ++¢)
      BenchingPolicy.go(l, "hash", 1, h);
    System.err.println(l.currentEntry().format("A D I X") + h.a);
  }

  public static class Hash extends Operation {
    private static int hash(final int h) {
      return h ^ h >>> 12 ^ h >>> 20 ^ (h ^ h >>> 12 ^ h >>> 20) >>> 4 ^ (h ^ h >>> 12 ^ h >>> 20) >>> 7;
    }

    int a;

    @Override public Void call() {
      a = hash(++a);
      return null;
    }
  }
}
