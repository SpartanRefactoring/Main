package il.org.spartan.java;

import static il.org.spartan.strings.StringUtils.*;

import java.io.*;

import org.eclipse.jdt.annotation.*;

import fluent.ly.*;

public class Tokenizer {
   public static Reader reader( final File ¢) throws FileNotFoundException {
    return new FileReader(¢);
  }
   public static Reader reader( final String fileName) throws FileNotFoundException {
    return fileName != null ? reader(new File(fileName)) : new InputStreamReader(System.in);
  }

   private final RawTokenizer inner;
  private final String streamName;
  private final Reader reader;

  /** Instantiate {@link Tokenizer}.
   * @param f read input from this file
   * @throws FileNotFoundException */
  public Tokenizer( final File f) throws FileNotFoundException {
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
    } catch ( final IOException ¢) {
      ¢.printStackTrace();
    }
  }
  public int column() {
    return inner.column();
  }
   public String description( final Token ¢) {
    return location() + ¢ + " / " + ¢.kind + "<" + esc(text()) + "> S=" + state();
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
    } catch ( final IOException ¢) {
      ¢.printStackTrace();
      ___.unreachable();
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
    return inner.yystate() + "";
  }
}
