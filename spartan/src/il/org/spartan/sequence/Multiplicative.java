package il.org.spartan.sequence;

import org.jetbrains.annotations.*;

public class Multiplicative extends Sequence {
  private static final double DEFAULT_STEP = 0.1;

  public static void main(final String[] args) {
    for (@NotNull final Sequence ¢ = new Multiplicative(0.16); ¢.current < 150; ¢.advance())
      System.out.print(¢.current() + " ");
    System.out.println();
  }

  private final double step;

  public Multiplicative() {
    this(Sequence.MAX_VALUE, DEFAULT_STEP);
  }
  public Multiplicative(final double step) {
    this(Sequence.MAX_VALUE, step);
  }
  public Multiplicative(final int threshold, final double step) {
    super(threshold);
    reset();
    this.step = Math.pow(2, step);
  }
  @Override @NotNull public Multiplicative advance() {
    final int prev = current;
    current *= step;
    if (current == prev)
      ++current;
    return this;
  }
  @Override @NotNull public Multiplicative reset() {
    current = 1;
    return this;
  }
}
