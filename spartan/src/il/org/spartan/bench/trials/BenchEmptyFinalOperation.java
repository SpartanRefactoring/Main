package il.org.spartan.bench.trials;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.bench.operations.*;

/** @author Yossi Gil
 * @since 30/05/2011 */
public class BenchEmptyFinalOperation {
  private static int trials = 100;

  public static void main(final String args[]) throws Exception {
    final LogBook.Mutable l = new LogBook.Mutable(BenchEmptyFinalOperation.class);
    final Operation o = new Operation() {
      @Override public final Object call() {
        return null;
      }
    };
    Log.deactivate();
    for (int i = 0; i < trials; i++)
      BenchingPolicy.go(l, "empty", 1, o);
    System.err.println(l.currentEntry().format("A D I X"));
  }
}
