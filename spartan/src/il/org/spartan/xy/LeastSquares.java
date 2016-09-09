package il.org.spartan.xy;

public class LeastSquares extends WeightedLeastSquares {
  @Override public void p(final double x, final double y, final double dy) {
    if (!isNumber(x) || !isNumber(y) || !isNumber(dy))
      return;
    xs.record(x, 1);
    ys.record(y, 1);
    xys.record(x * y, 1);
    ++n;
  }
}
