package il.org.spartan.spartanizer.utils;

/** Default implementation of an intercepting {@link Rule}, equipped with a
 * {@link Listener}
 * @param <T>
 * @param <R>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-13 */
@SuppressWarnings("static-method")
public class ThinInterceptor<T, R> implements Rule<T, R> {
  public final Rule<T, R> inner;

  public ThinInterceptor(final ThinInterceptor<T, R> inner) {
    this.inner = inner;
  }

  public Void before(@SuppressWarnings("unused") final String key, @SuppressWarnings("unused") final Object... arguments) {
    return null;
  }

  @Override public boolean check(T t) {
    return false;
  }

  @Override public T object() {
    return inner.object();
  }

  @Override public R apply(T ¢) {
    return inner.apply(¢); 
  }
}