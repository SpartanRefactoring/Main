package il.org.spartan.sequence;

import org.jetbrains.annotations.*;

public class Fibonacci extends Sequence {
  private int previous;

  public Fibonacci() {
  }

  public Fibonacci(final int threshold) {
    super(threshold);
    reset();
  }

  @Override @NotNull public Fibonacci advance() {
    final int temp = previous;
    previous = current;
    current += temp;
    return this;
  }

  @Override @NotNull public final Fibonacci reset() {
    current = previous = 1;
    return this;
  }
}