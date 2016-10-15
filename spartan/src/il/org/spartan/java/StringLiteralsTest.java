package il.org.spartan.java;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class StringLiteralsTest {
  static Token toToken(final String s) {
    try {
      final RawTokenizer J = new RawTokenizer(new StringReader(s));
      final Token t = J.next();
      azzert.that(J.next(), is(Token.EOF));
      return t;
    } catch (final IOException E) {
      return Token.EOF;
    }
  }

  @Test public void test_simple_literal() {
    final String s = "\"abcd\"";
    final Token t = toToken(s);
    azzert.that(t, is(Token.STRING_LITERAL));
  }
}
