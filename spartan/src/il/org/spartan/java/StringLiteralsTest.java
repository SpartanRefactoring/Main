package il.org.spartan.java;

import static fluent.ly.azzert.*;

import java.io.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import fluent.ly.*;

@SuppressWarnings("static-method")
public class StringLiteralsTest {
  @Nullable static Token toToken(@NotNull final String s) {
    try {
      @NotNull final RawTokenizer J = new RawTokenizer(new StringReader(s));
      @Nullable final Token $ = J.next();
      azzert.that(J.next(), is(Token.EOF));
      return $;
    } catch (@NotNull final IOException E) {
      return Token.EOF;
    }
  }
  @Test public void test_simple_literal() {
    azzert.that(toToken("\"abcd\""), is(Token.STRING_LITERAL));
  }
}
