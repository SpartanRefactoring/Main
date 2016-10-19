package il.org.spartan.java;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class StringLiteralsTest {
  static Token toToken(final String s) {
    try {
      final RawTokenizer J = new RawTokenizer(new StringReader(s));
      final Token $ = J.next();
      azzert.assertThat("", J.next(), is(Token.EOF));
      return $;
    } catch (final IOException E) {
      return Token.EOF;
    }
  }

  @Test public void test_simple_literal() {
    azzert.assertThat("", toToken("\"abcd\""), is(Token.STRING_LITERAL));
  }
}
