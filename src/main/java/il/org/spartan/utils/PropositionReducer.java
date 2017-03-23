package il.org.spartan.utils;

import java.util.function.*;

import il.org.spartan.utils.Proposition.*;

/**
 * TODO Yossi Gil: document class 
 * @param <R>
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19
 */
public abstract class PropositionReducer<R> extends Reduce<R> {
  public final Reduce<R> inner;

  public PropositionReducer(final Reduce<R> inner) {
    this.inner = inner;
    assert inner != this;
  }

  protected R post(@SuppressWarnings("unused") final Proposition.P __) {
    return reduce();
  }
  protected R post(@SuppressWarnings("unused") final Proposition.Not __) {
    return reduce();
  }

  protected R post(@SuppressWarnings("unused") final C __) {
    return reduce();
  }

  protected R ante(@SuppressWarnings("unused") final Proposition.Not __) {
    return reduce();
  }

  protected R ante(@SuppressWarnings("unused") final Proposition.P __) {
    return reduce();
  }

  protected R ante(@SuppressWarnings("unused") final C __) {
    return reduce();
  }

  protected R inter(@SuppressWarnings("unused") final And __) {
    return reduce();
  }

  protected R inter(@SuppressWarnings("unused") final Or __) {
    return reduce();
  }

  protected abstract R map(BooleanSupplier ¢);

  @Override public R reduce() {
    return inner.reduce();
  }

  private R reduce(final And a) {
    R $ = ante(a);
    for (int size = a.inner.size(), ¢ = 0; ¢ < size; ++¢) {
      $ = reduce($, reduce(a.inner.get(¢)));
      if (¢ < size - 1)
        $ = reduce($, inter(a));
    }
    return reduce($, post(a));
  }

  public final R reduce(final BooleanSupplier ¢) {
    return //
    ¢ instanceof Not ? reduce((Not) ¢) //
        : ¢ instanceof P ? reduce((P) ¢) //
            : ¢ instanceof And ? reduce((And) ¢) //
                : ¢ instanceof Or ? reduce((Or) ¢) //
                    : map(¢);
  }

  private R reduce(final Not ¢) {
    return reduce(ante(¢), reduce(¢.inner), post(¢));
  }

  private R reduce(final Or o) {
    R $ = ante(o);
    for (int size = o.inner.size(), ¢ = 0; ¢ < size; ++¢) {
      $ = reduce($, reduce(o.inner.get(¢)));
      if (¢ < size - 1)
        $ = reduce($, inter(o));
    }
    return reduce($, post(o));
  }

  private R reduce(final P ¢) {
    return reduce(ante(¢), reduce(¢.inner), post(¢));
  }

  @Override public R reduce(final R r1, final R r2) {
    return inner.reduce(r1, r2);
  }
}