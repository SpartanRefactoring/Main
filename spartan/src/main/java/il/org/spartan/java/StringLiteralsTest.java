package il.org.spartan.java;

import static fluent.ly.azzert.is;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import fluent.ly.azzert;

@SuppressWarnings("static-method")
public class StringLiteralsTest {
  static Token toToken(final String s) {
    try {
      final RawTokenizer J = new RawTokenizer(new StringReader(s));
      final Token $ = J.next();
      azzert.that(J.next(), is(Token.EOF));
      return $;
    } catch (final IOException E) {
      return Token.EOF;
    }
  }
  @Test public void test_simple_literal() {
    azzert.that(toToken("\"abcd\""), is(Token.STRING_LITERAL));
  }
}
