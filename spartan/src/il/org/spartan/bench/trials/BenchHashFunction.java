package il.org.spartan.bench.trials;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.bench.operations.*;

/** @author Yossi Gil
 * @since 30/05/2011 */
public class BenchHashFunction {
  private static int trials = 100;
  public static int size = 9;

  public static void main(final String args[]) throws Exception {
    final LogBook.Mutable l = new LogBook.Mutable(BenchHashFunction.class);
    final Hash h = new Hash();
    Log.deactivate();
    for (int i = 0; i < trials; i++)
      BenchingPolicy.go(l, "hash", 1, h);
    System.err.println(l.currentEntry().format("A D I X") + h.a);
  }

  public static class Hash extends Operation {
    private static int hash(final int h) {
      final int $ = h ^ h >>> 12 ^ h >>> 20;
      return $ ^ $ >>> 4 ^ $ >>> 7;
    }

    int a = 0;

    @Override public Void call() {
      a = hash(++a);
      return null;
    }
  }
}
