package il.org.spartan.utils;

/** Provides null value for {@link #reduce()}
 * @param <T>
 * @since 2017 */
public abstract class NullReduce<T> extends Reduce<T> {
  @Override public final T reduce() {
    return null;
  }
}