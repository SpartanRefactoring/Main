package il.org.spartan.java;

import static il.org.spartan.java.Token.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

@SuppressWarnings("static-method")
//
public class TokenizerTest {
  static Token getToken(final String s, final int n) throws IOException {
    final RawTokenizer j = new RawTokenizer(new StringReader(s));
    for (int i = 0; i < n - 1; i++)
      j.next();
    return j.next();
  }

  static String getTokenText(final String s, final int n) throws IOException {
    final RawTokenizer j = new RawTokenizer(new StringReader(s));
    for (int i = 0; i < n; i++)
      j.next();
    return j.text();
  }

  private static Token firstToken(final String s) throws IOException {
    return getToken(s, 1);
  }

  private static String firstTokenText(final String s) throws IOException {
    return getTokenText(s, 1);
  }

  private static Token secondToken(final String s) throws IOException {
    return getToken(s, 2);
  }

  private static String secondTokenText(final String s) throws IOException {
    return getTokenText(s, 2);
  }

  final StringTokenizer t = new StringTokenizer("");

  @Test public void annotation() throws IOException {
    final String text = "@interfac";
    assertThat(firstToken(text), is(ANNOTATION));
    assertEquals(text, firstTokenText(text));
    assertThat(firstToken("@interface__"), is(ANNOTATION));
    assertThat(firstToken("@__interface__"), is(ANNOTATION));
  }

  @Test public void at__intreface() throws IOException {
    final String text = "@interface";
    assertThat(firstToken(text), is(AT__INTERFACE));
    assertEquals(text, firstTokenText(text));
  }

  @Test public void block__comment__empty() throws IOException {
    assertThat(firstToken("/**/"), is(EMPTY__BLOCK__COMMENT));
  }

  @Test public void block__comment__keyword() throws IOException {
    final String text = "/* a */ public\n" + //
        "public";
    reset(text);
    assertThat(t.next(), is(BLOCK__COMMENT));
    assertThat(t.next(), is(SPACE));
    assertThat(t.next(), is(__public));
  }

  @Test public void block__comment__single__line() throws IOException {
    assertThat(firstToken("/* block Comment */"), is(BLOCK__COMMENT));
  }

  @Test public void block__comment__two__lines() throws IOException {
    final String text = "/* first Line \n Second Line */";
    assertThat(getToken(text, 1), is(PARTIAL__BLOCK__COMMENT));
    assertThat(getToken(text, 2), is(NL__BLOCK__COMMENT));
    assertThat(getToken(text, 3), is(BLOCK__COMMENT));
    assertEquals("/* first Line ", getTokenText(text, 1));
    assertEquals("\n", getTokenText(text, 2));
    assertEquals(" Second Line */", getTokenText(text, 3));
  }

  @Test public void character__literal__of__quote() throws IOException {
    assertEquals("'\\''", firstTokenText("'\\''"));
  }

  @Test public void character__literal__of__quote__kind() throws IOException {
    assertThat(firstToken("'\''"), is(CHARACTER__LITERAL));
  }

  @Test public void character__literal__with__double__backslash() throws IOException {
    assertEquals("'\\\\'", firstTokenText("'\\\\'"));
  }

  @Test public void character__literal__with__quote__text() throws IOException {
    assertEquals("'\\a'", firstTokenText("'\\a'"));
  }

  @Test public void character__literal__with__tab() throws IOException {
    assertEquals("'\t'", firstTokenText("'\t'"));
  }

  @Test public void character__literal__with__triple__backslash() throws IOException {
    assertEquals("'\\\\\\''", firstTokenText("'\\\\\\''"));
  }

  @Test public void doc__comment__keyword() throws IOException {
    final String text = "/**\n" + //
        "* A suite of metrics over Java code.\n" + //
        "* \n" + //
        "* @author Yossi Gil <yogi@cs.technion.ac.il> 21/04/2007\n" + //
        "*/\n" + //
        "public";
    reset(text);
    assertThat(t.next(), is(PARTIAL__DOC__COMMENT));
    assertThat(t.next(), is(NL__DOC__COMMENT));
    assertThat(t.next(), is(PARTIAL__DOC__COMMENT));
    assertThat(t.next(), is(NL__DOC__COMMENT));
    assertThat(t.next(), is(PARTIAL__DOC__COMMENT));
    assertThat(t.next(), is(NL__DOC__COMMENT));
    assertThat(t.next(), is(PARTIAL__DOC__COMMENT));
    assertThat(t.next(), is(NL__DOC__COMMENT));
    assertThat(t.next(), is(DOC__COMMENT));
    assertThat(t.next(), is(NL));
    assertThat(t.next(), is(__public));
  }

  @Test public void doc__comment__single__line() throws IOException {
    assertThat(firstToken("/* block Comment */"), is(BLOCK__COMMENT));
  }

  @Test public void doc__comment__two__lines() throws IOException {
    final String text = "/** first Line \n Second Line */";
    assertThat(getToken(text, 1), is(PARTIAL__DOC__COMMENT));
    assertThat(getToken(text, 2), is(NL__DOC__COMMENT));
    assertThat(getToken(text, 3), is(DOC__COMMENT));
    assertEquals("/** first Line ", getTokenText(text, 1));
    assertEquals("\n", getTokenText(text, 2));
    assertEquals(" Second Line */", getTokenText(text, 3));
  }

  @Test public void empty__character__literal() throws IOException {
    assertEquals("''", firstTokenText("''"));
  }

  @Test public void empty__string__empty__string() throws IOException {
    final String text = "\"\"\"\"";
    assertThat(getToken(text, 1), is(STRING__LITERAL));
    assertThat(getToken(text, 2), is(STRING__LITERAL));
    assertEquals("\"\"", getTokenText(text, 1));
    assertEquals("\"\"", getTokenText(text, 2));
  }

  @Test public void empty__string__id() throws IOException {
    final String text = "\"\"abcd";
    assertThat(getToken(text, 1), is(STRING__LITERAL));
    assertThat(getToken(text, 2), is(IDENTIFIER));
    assertEquals("\"\"", getTokenText(text, 1));
    assertEquals("abcd", getTokenText(text, 2));
  }

  @Test public void empty__string__literal() throws IOException {
    assertEquals("''", firstTokenText("''"));
  }

  @Test public void eof() throws IOException {
    assertThat(firstToken(""), is(EOF));
  }

  @Test public void eof__terminated__character__literal() throws IOException {
    assertThat(firstToken("'m"), is(UNTERMINATED__CHARACTER__LITERAL));
  }

  @Test public void eof__terminated__string__literal() throws IOException {
    assertThat(firstToken("\"m"), is(UNTERMINATED__STRING__LITERAL));
  }

  @Test public void eof__terminated__string__literal__empty() throws IOException {
    assertThat(firstToken("\""), is(UNTERMINATED__STRING__LITERAL));
  }

  @Test public void eof__terminated__string__literal__text() throws IOException {
    assertThat(firstToken("\"m"), is(UNTERMINATED__STRING__LITERAL));
  }

  @Test public void id() throws IOException {
    assertThat(firstToken("m"), is(IDENTIFIER));
  }

  @Test public void id__space() throws IOException {
    assertThat(firstToken("m "), is(IDENTIFIER));
  }

  @Test public void id__space__id() throws IOException {
    final String text = "id1 id2";
    assertThat(getToken(text, 1), is(IDENTIFIER));
    assertThat(getToken(text, 2), is(SPACE));
    assertThat(getToken(text, 3), is(IDENTIFIER));
    assertEquals("id1", getTokenText(text, 1));
    assertEquals(" ", getTokenText(text, 2));
    assertEquals("id2", getTokenText(text, 3));
  }

  @Test public void line__comment() throws IOException {
    assertThat(firstToken("// Comment\n"), is(LINE__COMMENT));
  }

  @Test public void line__comment__eof() throws IOException {
    assertThat(firstToken("// Comment"), is(LINE__COMMENT));
  }

  @Test public void long__character__literal() throws IOException {
    assertThat(firstToken("'masfasdfasdfas'"), is(CHARACTER__LITERAL));
  }

  @Test public void long__character__literal__text() throws IOException {
    assertEquals("'masfasdfasdfas'", firstTokenText("'masfasdfasdfas'"));
  }

  @Test public void long__string__literal() throws IOException {
    assertThat(firstToken("\"masfasdfasdfas\""), is(STRING__LITERAL));
  }

  @Test public void long__string__literal__text() throws IOException {
    assertEquals("'masfasdfasdfas'", firstTokenText("'masfasdfasdfas'"));
  }

  @Test public void nl__string__space__id__popen__integer() throws IOException {
    final String text = "\n\"\" abcd(12";
    assertThat(getToken(text, 1), is(NL));
    assertThat(getToken(text, 2), is(STRING__LITERAL));
    assertThat(getToken(text, 3), is(SPACE));
    assertThat(getToken(text, 4), is(IDENTIFIER));
  }

  /* escaped \ */
  @Test public void no__esc__block__comment() throws IOException {
    assertThat(firstToken("/* block Comment \\*/"), is(BLOCK__COMMENT));
  }

  /* /* No nested comment */
  @Test public void no__nested__block__comment() throws IOException {
    assertThat(firstToken("/*/* block Comment */"), is(BLOCK__COMMENT));
  }

  @Test public void no__nested__block__comment__text() throws IOException {
    assertEquals("/*/* block Comment */", firstTokenText("/*/* block Comment */"));
  }

  @Test public void no__nested__doc__comment__text__firstToken() throws IOException {
    assertEquals("/**/", firstTokenText("/**/** doc Comment */"));
  }

  @Test public void no__nested__doc__comment__text__secondToken() throws IOException {
    assertEquals("/** doc Comment */", secondTokenText("/**//** doc Comment */"));
  }

  @Test public void no__nested__doc__commentFirstToken() throws IOException {
    assertThat(firstToken("/**/** doc Comment */"), is(EMPTY__BLOCK__COMMENT));
  }

  @Test public void no__nested__doc__commentSecondToken() throws IOException {
    assertThat(secondToken("/**/** doc Comment */"), is(MULT));
  }

  // ===================================
  @Test public void one__char__string__literal() throws IOException {
    assertThat(firstToken("\"m\""), is(STRING__LITERAL));
  }

  @Test public void short__doc__comment__keyword() throws IOException {
    final String text = "/** a */ public\n" + //
        "public";
    reset(text);
    assertThat(t.next(), is(DOC__COMMENT));
    assertThat(t.next(), is(SPACE));
    assertThat(t.next(), is(__public));
  }

  @Test public void simple__character__literal() throws IOException {
    assertThat(firstToken("'m'"), is(CHARACTER__LITERAL));
  }

  @Test public void simple__character__literal__text() throws IOException {
    assertEquals("'m'", firstTokenText("'m'"));
  }

  @Test public void simple__string__literal__text() throws IOException {
    assertEquals("\"abc m'\"", firstTokenText("\"abc m'\""));
  }

  @Test public void something__after__emtpy__comment() throws IOException {
    assertThat(secondToken("/**/something"), is(IDENTIFIER));
  }

  @Test public void space__id__space() throws IOException {
    assertThat(getToken(" m ", 2), is(IDENTIFIER));
  }

  @Test public void string__id__string() throws IOException {
    final String text = "\"str1\"xid\"str2\"";
    assertThat(getToken(text, 1), is(STRING__LITERAL));
    assertThat(getToken(text, 2), is(IDENTIFIER));
    assertThat(getToken(text, 3), is(STRING__LITERAL));
    assertEquals("\"str1\"", getTokenText(text, 1));
    assertEquals("xid", getTokenText(text, 2));
    assertEquals("\"str2\"", getTokenText(text, 3));
  }

  @Test public void string__keyword() throws IOException {
    final String text = "\" \"public";
    reset(text);
    assertThat(t.next(), is(STRING__LITERAL));
    assertEquals("\" \"", t.text());
    assertThat(t.next(), is(__public));
    assertEquals("public", t.text());
  }

  @Test public void string__literal__of__quote() throws IOException {
    assertEquals("'\\''", firstTokenText("'\\''"));
  }

  @Test public void string__literal__of__quote__kind() throws IOException {
    assertThat(firstToken("\"\\\"\""), is(STRING__LITERAL));
  }

  @Test public void string__literal__with__double__backslash() throws IOException {
    assertEquals("'\\\\'", firstTokenText("'\\\\'"));
  }

  @Test public void string__literal__with__quote__text() throws IOException {
    assertEquals("\"\\\"\"", firstTokenText("\"\\\"\""));
  }

  @Test public void string__literal__with__tab() throws IOException {
    assertEquals("'\t'", firstTokenText("'\t'"));
  }

  @Test public void string__literal__with__triple__backslash() throws IOException {
    assertEquals("'\\\\\\''", firstTokenText("'\\\\\\''"));
  }

  @Test public void string__space__keyword() throws IOException {
    final String text = "\" \" public\n";
    reset(text);
    assertThat(t.next(), is(STRING__LITERAL));
    assertThat(t.next(), is(SPACE));
    assertThat(t.next(), is(__public));
  }

  @Test public void unterminated__block__comment() throws IOException {
    assertThat(firstToken("/*/* block Comment"), is(UNTERMINATED__BLOCK__COMMENT));
  }

  @Test public void unterminated__block__comment__text() throws IOException {
    assertEquals("/*/* block Comment", firstTokenText("/*/* block Comment"));
  }

  @Test public void unterminated__character__literal() throws IOException {
    assertEquals(firstToken("'m\n"), UNTERMINATED__CHARACTER__LITERAL);
  }

  @Test public void unterminated__doc__comment() throws IOException {
    assertThat(firstToken("/** doc Comment"), is(UNTERMINATED__DOC__COMMENT));
  }

  @Test public void unterminated__doc__comment__text() throws IOException {
    assertEquals("/*/* block Comment", firstTokenText("/*/* block Comment"));
  }

  @Test public void unterminated__string__literal() throws IOException {
    assertThat(firstToken("\"masfasdfasdf\n"), is(UNTERMINATED__STRING__LITERAL));
  }

  @Test public void unterminated__string__literal__text() throws IOException {
    assertEquals("\"mabc", firstTokenText("\"mabc\n"));
  }

  void reset(final String text) {
    t.reset(text);
  }

  public static class StringTokenizer {
    private final RawTokenizer inner;

    public StringTokenizer(final String text) {
      inner = new RawTokenizer(new StringReader(text));
    }

    public Token next() throws IOException {
      return inner.next();
    }

    public void reset(final String text) {
      inner.yyreset(new StringReader(text));
    }

    public String text() {
      return inner.text();
    }
  }
}
