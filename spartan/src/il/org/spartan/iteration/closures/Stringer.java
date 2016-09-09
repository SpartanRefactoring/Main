package il.org.spartan.iteration.closures;

import static il.org.spartan.strings.StringUtils.*;
import static il.org.spartan.utils.____.*;

/** An interface supplying a function object pointer, where the function return
 * value is {@link String}. To create such a pointer, create a subclass that
 * implements this interface (typically as an anonymous class), giving an
 * implementation to function {@link #_}, and then pass an instance of this
 * subclass class.
 * @author Yossi Gil.
 * @param <T> type of values that the function takes */
public interface Stringer<T> extends Converter<T, String> {
  /** {@inheritDoc}
   * @see Converter#_ */
  @Override String _(T t);

  public static class Default<T> implements Stringer<T> {
    @Override public String _(final T t) {
      return t.toString();
    }
  }

  public static class DoubleQuoter<T> extends Quoter<T> {
    public DoubleQuoter() {
      super('"');
    }
  }

  public static class Quoter<T> extends Default<T> {
    public final String quote;

    public Quoter(final char quote) {
      this("" + quote);
    }

    public Quoter(final String quote) {
      this.quote = quote;
    }

    @Override public final String _(final T t) {
      return quote(t == null ? "" : super._(t));
    }

    public final String quote(final String s) {
      nonnull(s);
      return wrap(quote, s.replaceAll(quote, quote + quote));
    }
  }

  public static class SingleQuoter<T> extends Quoter<T> {
    public SingleQuoter() {
      super('\'');
    }
  }
}
