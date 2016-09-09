package il.org.spartan.java;

import static il.org.spartan.strings.StringUtils.*;

import java.io.*;

import il.org.spartan.utils.*;

public class Tokenizer {
  public static Reader reader(final File f) throws FileNotFoundException {
    return new FileReader(f);
  }

  public static Reader reader(final String fileName) throws FileNotFoundException {
    return fileName != null ? reader(new File(fileName)) : new InputStreamReader(System.in);
  }

  private final RawTokenizer inner;
  private final String streamName;
  private final Reader reader;

  /** Instantiate {@link Tokenizer}.
   * @param f read input from this file
   * @throws FileNotFoundException */
  public Tokenizer(final File f) throws FileNotFoundException {
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
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public int column() {
    return inner.column();
  }

  public String description(final Token t) {
    return location() + t + " / " + t.kind + "<" + esc(text()) + "> S=" + state();
  }

  public int line() {
    return inner.line();
  }

  public String location() {
    return inner.location();
  }

  public Token next() {
    try {
      return inner.next();
    } catch (final IOException e) {
      e.printStackTrace();
      ____.unreachable();
      return null;
    }
  }

  public String streamName() {
    return streamName;
  }

  public String text() {
    return inner.text();
  }

  protected String state() {
    return "" + inner.yystate();
  }
}
