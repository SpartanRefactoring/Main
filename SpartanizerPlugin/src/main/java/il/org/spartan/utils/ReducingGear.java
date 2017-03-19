package il.org.spartan.utils;

import java.util.*;
import java.util.function.*;

import il.org.spartan.utils.Trivalent.*;

public abstract class ReducingGear<R> extends Reduce<R> {
  @Override public R reduce() {
    return inner.reduce();
  }

  @Override public R reduce(final R r1, final R r2) {
    return inner.reduce(r1, r2);
  }

  public final Reduce<R> inner;

  public ReducingGear(final Reduce<R> inner) {
    this.inner = inner;
  }

  private R reduce(final AND ¢) {
    return reduce(before(¢), reduce(¢.inner), after(¢));
  }

  private R reduce(final List<BooleanSupplier> ss) {
    R $ = reduce();
    for (final BooleanSupplier ¢ : ss)
      $ = reduce($, reduce(¢));
    return $;
  }

  private R reduce(final OR ¢) {
    return reduce(before(¢), reduce(¢.inner), after(¢));
  }

  private R reduce(final NOT ¢) {
    return reduce(before(¢), reduce(¢.inner), after(¢));
  }

  public final R reduce(final BooleanSupplier ¢) {
    return //
    ¢ instanceof NOT ? reduce((NOT) ¢) //
        : ¢ instanceof P ? reduce((P) ¢) //
            : ¢ instanceof AND ? reduce((AND) ¢) //
                : ¢ instanceof OR ? reduce((OR) ¢) //
                    : map(¢);
  }

  protected abstract R map(BooleanSupplier ¢);

  protected R before(@SuppressWarnings("unused") final Trivalent.NOT ¢) {
    return reduce();
  }

  private R reduce(final P ¢) {
    return reduce(before(¢), reduce(¢.inner), after(¢));
  }

  private R after(@SuppressWarnings("unused") final Trivalent.P ¢) {
    return reduce();
  }

  private R before(@SuppressWarnings("unused") final Trivalent.P ¢) {
    return reduce();
  }

  protected R map(@SuppressWarnings("unused") final List<BooleanSupplier> __) {
    return reduce();
  }

  protected R after(@SuppressWarnings("unused") final C __) {
    return reduce();
  }

  protected R before(@SuppressWarnings("unused") final C __) {
    return reduce();
  }
}