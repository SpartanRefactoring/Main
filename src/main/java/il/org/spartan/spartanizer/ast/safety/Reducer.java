package il.org.spartan.spartanizer.ast.safety;

import java.util.Objects;
import java.util.stream.*;

/** TODO Yossi Gil: document class {@link }
 * @param <R>
 * @since 2017-01-29 */
public abstract class Reducer<R> {
  public R reduce() {
    return null;
  }

  public final R reduce(final R ¢) {
    return ¢;
  }

  public abstract R reduce(R t1, R t2);

  public final R reduce(final R[] $) {
    return $ == null ? reduce() : Stream.of($).filter(Objects::nonNull).reduce(this::reduce).orElse(reduce());
  }

  @SafeVarargs public final R reduce(final R t1, final R t2, final R... rs) {
    return reduce(t1, reduce(t2, reduce(rs)));
  }
}
