package il.org.spartan.utils.fluent;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-10 */
public interface zero {
  @SuppressWarnings("unused") static int ignoring(final boolean __) {
    return 0;
  }

  @SuppressWarnings("unused") static int ignoring(final double __) {
    return 0;
  }

  @SuppressWarnings("unused") static int ignoring(final long __) {
    return 0;
  }

  @SuppressWarnings("unused") static int ignoringAll(final Object _1, final Object... _2) {
    return 0;
  }
}
