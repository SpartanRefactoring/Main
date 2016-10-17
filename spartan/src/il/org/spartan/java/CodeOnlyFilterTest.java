/**
 *
 */
package il.org.spartan.java;

import java.io.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method") public class CodeOnlyFilterTest {
  static TokenFeeder makeFilter(final String ¢) {
    return new TokenFeeder(new Tokenizer(new StringReader(¢)), new CodeOnlyFilter());
  }

  @Test public void content() {
    assert null != makeFilter("Hello, World!\n").processor + "";
  }

  @Test public void creater() {
    assert null != makeFilter("Hello, World!\n");
  }
}
