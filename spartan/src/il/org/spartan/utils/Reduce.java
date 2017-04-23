package il.org.spartan.utils;

import java.util.*;
import java.util.stream.*;

/** An abstract reducer of an associative, but not necessarily commutative
 * operation. This operation must also have a neutral element (#{@link Reduce}).
 * @param <R>
 * @since 2017-01-29 */
public abstract class Reduce<R> {
  public abstract R reduce();

  public final R reduce(final R ¢) {
    return ¢;
  }

  public abstract R reduce(R r1, R r2);

  public final R reduce(final R[] $) {
    return $ == null ? reduce() : Stream.of($).filter(Objects::nonNull).reduce(this::reduce).orElse(reduce());
  }

  @SafeVarargs public final R reduce(final R r1, final R r2, final R... rs) {
    return reduce(r1, reduce(r2, reduce(rs)));
  }
}
