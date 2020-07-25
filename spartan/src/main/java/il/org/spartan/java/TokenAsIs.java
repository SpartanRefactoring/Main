package il.org.spartan.java;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import fluent.ly.forget;
import fluent.ly.the;

/** @author Yossi Gil
 * @since 2011-11-19 */
public class TokenAsIs extends TokenProcessor {
  public static String fileToString(final File ¢) throws IOException {
    return new TokenFeeder(new Tokenizer(¢), new TokenAsIs()).go().processor + "";
  }

  public static String fileToString(final String fileName) throws IOException {
    return new TokenFeeder(new Tokenizer(fileName), new TokenAsIs()).go().processor + "";
  }

  public static void main(final String argv[]) throws IOException {
    System.out.println(fileToString(the.first(argv)));
  }

  public static String stringToString(final String text) {
    return new TokenFeeder(new StringReader(text), new TokenAsIs()).go().processor + "";
  }

  private final StringBuilder $ = new StringBuilder();

  @Override public void process(final Token t, final String text) {
    forget.em(t);
    $.append(text);
  }

  @Override public String toString() {
    return $ + "";
  }
}
