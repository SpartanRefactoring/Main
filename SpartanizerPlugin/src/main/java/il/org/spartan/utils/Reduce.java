package il.org.spartan.utils;

import java.util.*;
import java.util.stream.*;

import org.jetbrains.annotations.*;

/** An abstract reducer
 * @param <R>
 * @since 2017-01-29 */
public abstract class Reduce<@¢ R> {
  public abstract R reduce();

  public final R reduce(final R ¢) {
    return ¢;
  }

  @Nullable
  public abstract R reduce(R r1, R r2);

  public final R reduce(@Nullable final R[] $) {
    return $ == null ? reduce() : Stream.of($).filter(Objects::nonNull).reduce(this::reduce).orElse(reduce());
  }

  @Nullable
  @SafeVarargs public final R reduce(final R r1, final R r2, final R... rs) {
    return reduce(r1, reduce(r2, reduce(rs)));
  }
}
