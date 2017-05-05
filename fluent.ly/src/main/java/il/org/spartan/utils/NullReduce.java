package il.org.spartan.utils;

import org.eclipse.jdt.annotation.*;

/** Provides null value for {@link #reduce()}
 * @param <T>
 * @since 2017 */
public abstract class NullReduce< T> extends Reduce<T> {
  @Override public final T reduce() {
    return null;
  }
}