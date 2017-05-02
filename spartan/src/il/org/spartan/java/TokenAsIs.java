package il.org.spartan.java;

import static fluent.ly.___.*;
import static il.org.spatan.iteration.Iterables.*;

import java.io.*;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 2011-11-19 */
public class TokenAsIs extends TokenProcessor {
  @NotNull public static String fileToString(@NotNull final File ¢) throws IOException {
    return new TokenFeeder(new Tokenizer(¢), new TokenAsIs()).go().processor + "";
  }

  @NotNull public static String fileToString(final String fileName) throws IOException {
    return new TokenFeeder(new Tokenizer(fileName), new TokenAsIs()).go().processor + "";
  }

  public static void main(@NotNull final String argv[]) throws IOException {
    System.out.println(fileToString(first(argv)));
  }

  @NotNull public static String stringToString(@NotNull final String text) throws IOException {
    return new TokenFeeder(new StringReader(text), new TokenAsIs()).go().processor + "";
  }

  private final StringBuilder $ = new StringBuilder();

  @Override public void process(final Token t, final String text) {
    unused(t);
    $.append(text);
  }

  @Override @NotNull public String toString() {
    return $ + "";
  }
}
