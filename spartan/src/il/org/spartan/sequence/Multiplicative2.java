package il.org.spartan.sequence;

import static java.lang.Integer.*;
import static java.lang.Math.max;

import org.jetbrains.annotations.*;

public class Multiplicative2 extends Sequence {
  private static final double DEFAULT_STEP = 0.1;
  private final double step;

  public Multiplicative2() {
    this(Sequence.MAX_VALUE, DEFAULT_STEP);
  }
  public Multiplicative2(final double step) {
    this(Sequence.MAX_VALUE, step);
  }
  public Multiplicative2(final int threshold, final double step) {
    super(threshold);
    reset();
    this.step = Math.pow(2, step);
  }
  @Override @NotNull public Multiplicative2 advance() {
    final int previous = current;
    current *= step;
    current = max(previous + 1, current);
    if (highestOneBit(previous) < highestOneBit(current))
      current = highestOneBit(current);
    return this;
  }
  @Override @NotNull public Multiplicative2 reset() {
    current = 1;
    return this;
  }
}
