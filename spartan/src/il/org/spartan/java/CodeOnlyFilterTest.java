/**
 *
 */
package il.org.spartan.java;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method") public class CodeOnlyFilterTest {
  static TokenFeeder makeFilter(final String s) {
    return new TokenFeeder(new Tokenizer(new StringReader(s)), new CodeOnlyFilter());
  }

  @Test public void content() {
    assertNotNull("Hello,World!", makeFilter("Hello, World!\n").processor.toString());
  }

  @Test public void creater() {
    assertNotNull(makeFilter("Hello, World!\n"));
  }
}
