package il.org.spartan.iteration.closures;

import static il.org.spartan.strings.StringUtils.*;
import static fluent.ly.___.*;

import org.jetbrains.annotations.*;

/** An interface supplying a function object pointer, where the function return
 * value is {@link String}. To create such a pointer, create a subclass that
 * implements this interface (typically as an anonymous class), giving an
 * implementation to function {@link #__}, and then pass an instance of this
 * subclass class.
 * @author Yossi Gil.
 * @param <T> type of values that the function takes */
public interface Stringer<T> extends Converter<T, String> {
  @Override @NotNull String __(T t);

  class Default<T> implements Stringer<T> {
    @Override @NotNull public String __(final T ¢) {
      return ¢ + "";
    }
  }

  class DoubleQuoter<T> extends Quoter<T> {
    public DoubleQuoter() {
      super('"');
    }
  }

  class Quoter<T> extends Default<T> {
    public final String quote;

    public Quoter(final char quote) {
      this(quote + "");
    }
    public Quoter(final String quote) {
      this.quote = quote;
    }
    @Override @NotNull public final String __(@Nullable final T ¢) {
      return quote(¢ == null ? "" : super.__(¢));
    }
    @NotNull public final String quote(@NotNull final String ¢) {
      nonnull(¢);
      return wrap(quote, ¢.replaceAll(quote, quote + quote));
    }
  }

  class SingleQuoter<T> extends Quoter<T> {
    public SingleQuoter() {
      super('\'');
    }
  }
}
