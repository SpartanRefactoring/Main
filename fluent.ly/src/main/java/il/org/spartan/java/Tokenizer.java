package il.org.spartan.java;

import fluent.ly.___;
import static il.org.spartan.strings.StringUtils.*;

import java.io.*;

import org.jetbrains.annotations.*;

import il.org.spartan.utils.*;

public class Tokenizer {
  @NotNull public static Reader reader(@NotNull final File ¢) throws FileNotFoundException {
    return new FileReader(¢);
  }

  @NotNull public static Reader reader(@Nullable final String fileName) throws FileNotFoundException {
    return fileName != null ? reader(new File(fileName)) : new InputStreamReader(System.in);
  }

  @NotNull private final RawTokenizer inner;
  private final String streamName;
  private final Reader reader;

  /** Instantiate {@link Tokenizer}.
   * @param f read input from this file
   * @throws FileNotFoundException */
  public Tokenizer(@NotNull final File f) throws FileNotFoundException {
    this(f.getPath(), reader(f));
  }

  public Tokenizer(final Reader in) {
    this("", in);
  }

  /** Instantiate {@link Tokenizer}.
   * @param streamName read input from this file
   * @throws FileNotFoundException */
  public Tokenizer(final String streamName) throws FileNotFoundException {
    this(streamName, reader(streamName));
  }

  public Tokenizer(final String streamName, final Reader reader) {
    inner = new RawTokenizer(this.reader = reader);
    this.streamName = streamName;
  }

  public void closeReader() {
    try {
      reader.close();
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
    }
  }

  public int column() {
    return inner.column();
  }

  @NotNull public String description(@NotNull final Token ¢) {
    return location() + ¢ + " / " + ¢.kind + "<" + esc(text()) + "> S=" + state();
  }

  public int line() {
    return inner.line();
  }

  @NotNull public String location() {
    return inner.location();
  }

  @Nullable public Token next() {
    try {
      return inner.next();
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
      ___.unreachable();
      return null;
    }
  }

  public String streamName() {
    return streamName;
  }

  @NotNull public String text() {
    return inner.text();
  }

  @NotNull protected String state() {
    return inner.yystate() + "";
  }
}
