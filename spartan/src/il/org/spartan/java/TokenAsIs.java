/**
 *
 */
package il.org.spartan.java;

import static il.org.spartan.utils.____.*;
import static il.org.spatan.iteration.Iterables.*;

import java.io.*;

/** @author Yossi Gil
 * @since 2011-11-19 */
public class TokenAsIs extends TokenProcessor {
  public static String fileToString(final File f) throws IOException, FileNotFoundException {
    return new TokenFeeder(new Tokenizer(f), new TokenAsIs()).go().processor.toString();
  }

  public static String fileToString(final String fileName) throws IOException, FileNotFoundException {
    return new TokenFeeder(new Tokenizer(fileName), new TokenAsIs()).go().processor.toString();
  }

  public static void main(final String argv[]) throws IOException {
    final String s = fileToString(first(argv));
    System.out.println(s);
  }

  public static String stringToString(final String text) throws IOException, FileNotFoundException {
    return new TokenFeeder(new StringReader(text), new TokenAsIs()).go().processor.toString();
  }

  private final StringBuilder $ = new StringBuilder();

  @Override public void process(final Token t, final String text) {
    unused(t);
    $.append(text);
  }

  @Override public String toString() {
    return $.toString();
  }
}
