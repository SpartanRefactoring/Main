package il.org.spartan.java;

import static il.org.spartan.java.Token.*;

import java.io.*;

import org.jetbrains.annotations.*;

/** Forwards all tokens from a {@link RawTokenizer} to a {@link TokenProcessor}
 * @author Yossi Gil
 * @since 2011-11-19 */
public final class TokenFeeder {
  public final Tokenizer tokenizer;
  public final TokenProcessor processor;

  public TokenFeeder(@NotNull final File f, final TokenProcessor processor) throws FileNotFoundException {
    this(new Tokenizer(f), processor);
  }

  public TokenFeeder(final Reader r, final TokenProcessor processor) {
    this(new Tokenizer(r), processor);
  }

  public TokenFeeder(final Tokenizer tokenizer, final TokenProcessor processor) {
    this.tokenizer = tokenizer;
    this.processor = processor;
  }

  @NotNull public TokenFeeder go() {
    processor.before();
    for (@Nullable Token ¢ = tokenizer.next(); ¢ != EOF; ¢ = tokenizer.next())
      processor.process(¢, tokenizer.text());
    processor.after();
    return this;
  }
}
