package il.org.spartan.utils;

/**
 * Provides null value for {@link #reduce()}
 *
 * @param <T> JD
 * @since 2017
 */
public abstract class NullReduce<T> extends Reduce<T> {
  @SuppressWarnings("null") @Override public final T reduce() {
    return null;
  }
}