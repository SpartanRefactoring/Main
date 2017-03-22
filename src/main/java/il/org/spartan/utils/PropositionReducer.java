package il.org.spartan.utils;

import java.util.function.*;

import il.org.spartan.utils.Proposition.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Yossi Gil: document class
 * @param <R>
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
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

  @NotNull
  protected abstract R map(BooleanSupplier ¢);

  @Override public R reduce() {
    return inner.reduce();
  }

  @Nullable
  private R reduce(@NotNull final And a) {
    @Nullable R $ = ante(a);
    for (int size = a.inner.size(), ¢ = 0; ¢ < size; ++¢) {
      $ = reduce($, reduce(a.inner.get(¢)));
      if (¢ < size - 1)
        $ = reduce($, inter(a));
    }
    return reduce($, post(a));
  }

  @NotNull
  public final R reduce(final BooleanSupplier ¢) {
    return //
    ¢ instanceof Not ? reduce((Not) ¢) //
        : ¢ instanceof P ? reduce((P) ¢) //
            : ¢ instanceof And ? reduce((And) ¢) //
                : ¢ instanceof Or ? reduce((Or) ¢) //
                    : map(¢);
  }

  @Nullable
  private R reduce(@NotNull final Not ¢) {
    return reduce(ante(¢), reduce(¢.inner), post(¢));
  }

  @Nullable
  private R reduce(@NotNull final Or o) {
    @Nullable R $ = ante(o);
    for (int size = o.inner.size(), ¢ = 0; ¢ < size; ++¢) {
      $ = reduce($, reduce(o.inner.get(¢)));
      if (¢ < size - 1)
        $ = reduce($, inter(o));
    }
    return reduce($, post(o));
  }

  @Nullable
  private R reduce(@NotNull final P ¢) {
    return reduce(ante(¢), reduce(¢.inner), post(¢));
  }

  @Nullable
  @Override public R reduce(final R r1, final R r2) {
    return inner.reduce(r1, r2);
  }
}