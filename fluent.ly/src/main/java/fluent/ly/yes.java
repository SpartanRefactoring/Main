package fluent.ly;

/**
 * TODO Yossi Gil: document class
 *
 * @author Yossi Gil
 * @since 2017-04-10
 */
public interface yes {
  interface On<T, R> {
    R on(T t);
  }

  static boolean forgetting(final Runnable ¢) {
    ¢.run();
    return true;
  }

  @SuppressWarnings("unused") static boolean forgetting(final Object _1, final Object... _2) {
    return true;
  }

  @SuppressWarnings("unused") static boolean ignoring(final boolean __) {
    return true;
  }

  @SuppressWarnings("unused") static boolean ignoring(final double __) {
    return true;
  }

  @SuppressWarnings("unused") static boolean ignoring(final long __) {
    return true;
  }
}
