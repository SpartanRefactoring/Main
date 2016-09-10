package il.org.spartan.java;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

@SuppressWarnings("static-method") public class StringLiteralsTest {
  static Token toToken(final String s) {
    try {
      final RawTokenizer J = new RawTokenizer(new StringReader(s));
      final Token t = J.next();
      assertEquals(Token.EOF, J.next());
      return t;
    } catch (final IOException E) {
      return Token.EOF;
    }
  }

  @Test public void test__simple__literal() {
    final String s = "\"abcd\"";
    final Token t = toToken(s);
    assertEquals(Token.STRING__LITERAL, t);
  }
}
