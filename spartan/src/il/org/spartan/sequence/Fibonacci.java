package il.org.spartan.sequence;

import org.jetbrains.annotations.NotNull;

public class Fibonacci extends Sequence {
  private int previous;

  public Fibonacci() {
  }

  public Fibonacci(final int threshold) {
    super(threshold);
    reset();
  }

  @NotNull
  @Override public Fibonacci advance() {
    final int temp = previous;
    previous = current;
    current += temp;
    return this;
  }

  @NotNull
  @Override public final Fibonacci reset() {
    current = previous = 1;
    return this;
  }
}