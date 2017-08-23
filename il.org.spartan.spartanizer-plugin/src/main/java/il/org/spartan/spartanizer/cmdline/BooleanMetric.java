package il.org.spartan.spartanizer.cmdline;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

public class BooleanMetric<R> implements ToIntFunction<R> {
  final ToIntFunction<R> function;
  final String name;

  public BooleanMetric(final String name, final ToIntFunction<R> function) {
    this.name = name;
    this.function = function;
  }
  public int apply(final R ¢) {
    return applyAsInt(¢);
  }
  @Override public int applyAsInt(final R ¢) {
    return function.applyAsInt(¢);
  }
  public ToIntFunction<R> function() {
    return function;
  }
  public String name() {
    return name;
  }
  public static BooleanMetric<ASTNode> m(final String name, final ToIntFunction<ASTNode> n) {
    return new BooleanMetric<>(name, n);
  }
}