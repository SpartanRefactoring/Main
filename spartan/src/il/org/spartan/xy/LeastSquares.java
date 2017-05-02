package il.org.spartan.xy;

public class LeastSquares extends WeightedLeastSquares {
  @Override public void p(final double x, final double y, final double dy) {
    if (isMissing(x) || isMissing(y) || isMissing(dy))
      return;
    xs.record(x, 1);
    ys.record(y, 1);
    xys.record(x * y, 1);
    ++n;
  }
}
