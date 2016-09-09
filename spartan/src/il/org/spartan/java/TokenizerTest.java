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
    assertThat(firstToken("@interface_"), is(ANNOTATION));
    assertThat(firstToken("@_interface_"), is(ANNOTATION));
  }

  @Test public void at_intreface() throws IOException {
    final String text = "@interface";
    assertThat(firstToken(text), is(AT_INTERFACE));
    assertEquals(text, firstTokenText(text));
  }

  @Test public void block_comment_empty() throws IOException {
    assertThat(firstToken("/**/"), is(EMPTY_BLOCK_COMMENT));
  }

  @Test public void block_comment_keyword() throws IOException {
    final String text = "/* a */ public\n" + //
        "public";
    reset(text);
    assertThat(t.next(), is(BLOCK_COMMENT));
    assertThat(t.next(), is(SPACE));
    assertThat(t.next(), is(_public));
  }

  @Test public void block_comment_single_line() throws IOException {
    assertThat(firstToken("/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void block_comment_two_lines() throws IOException {
    final String text = "/* first Line \n Second Line */";
    assertThat(getToken(text, 1), is(PARTIAL_BLOCK_COMMENT));
    assertThat(getToken(text, 2), is(NL_BLOCK_COMMENT));
    assertThat(getToken(text, 3), is(BLOCK_COMMENT));
    assertEquals("/* first Line ", getTokenText(text, 1));
    assertEquals("\n", getTokenText(text, 2));
    assertEquals(" Second Line */", getTokenText(text, 3));
  }

  @Test public void character_literal_of_quote() throws IOException {
    assertEquals("'\\''", firstTokenText("'\\''"));
  }

  @Test public void character_literal_of_quote_kind() throws IOException {
    assertThat(firstToken("'\''"), is(CHARACTER_LITERAL));
  }

  @Test public void character_literal_with_double_backslash() throws IOException {
    assertEquals("'\\\\'", firstTokenText("'\\\\'"));
  }

  @Test public void character_literal_with_quote_text() throws IOException {
    assertEquals("'\\a'", firstTokenText("'\\a'"));
  }

  @Test public void character_literal_with_tab() throws IOException {
    assertEquals("'\t'", firstTokenText("'\t'"));
  }

  @Test public void character_literal_with_triple_backslash() throws IOException {
    assertEquals("'\\\\\\''", firstTokenText("'\\\\\\''"));
  }

  @Test public void doc_comment_keyword() throws IOException {
    final String text = "/**\n" + //
        "* A suite of metrics over Java code.\n" + //
        "* \n" + //
        "* @author Yossi Gil <yogi@cs.technion.ac.il> 21/04/2007\n" + //
        "*/\n" + //
        "public";
    reset(text);
    assertThat(t.next(), is(PARTIAL_DOC_COMMENT));
    assertThat(t.next(), is(NL_DOC_COMMENT));
    assertThat(t.next(), is(PARTIAL_DOC_COMMENT));
    assertThat(t.next(), is(NL_DOC_COMMENT));
    assertThat(t.next(), is(PARTIAL_DOC_COMMENT));
    assertThat(t.next(), is(NL_DOC_COMMENT));
    assertThat(t.next(), is(PARTIAL_DOC_COMMENT));
    assertThat(t.next(), is(NL_DOC_COMMENT));
    assertThat(t.next(), is(DOC_COMMENT));
    assertThat(t.next(), is(NL));
    assertThat(t.next(), is(_public));
  }

  @Test public void doc_comment_single_line() throws IOException {
    assertThat(firstToken("/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void doc_comment_two_lines() throws IOException {
    final String text = "/** first Line \n Second Line */";
    assertThat(getToken(text, 1), is(PARTIAL_DOC_COMMENT));
    assertThat(getToken(text, 2), is(NL_DOC_COMMENT));
    assertThat(getToken(text, 3), is(DOC_COMMENT));
    assertEquals("/** first Line ", getTokenText(text, 1));
    assertEquals("\n", getTokenText(text, 2));
    assertEquals(" Second Line */", getTokenText(text, 3));
  }

  @Test public void empty_character_literal() throws IOException {
    assertEquals("''", firstTokenText("''"));
  }

  @Test public void empty_string_empty_string() throws IOException {
    final String text = "\"\"\"\"";
    assertThat(getToken(text, 1), is(STRING_LITERAL));
    assertThat(getToken(text, 2), is(STRING_LITERAL));
    assertEquals("\"\"", getTokenText(text, 1));
    assertEquals("\"\"", getTokenText(text, 2));
  }

  @Test public void empty_string_id() throws IOException {
    final String text = "\"\"abcd";
    assertThat(getToken(text, 1), is(STRING_LITERAL));
    assertThat(getToken(text, 2), is(IDENTIFIER));
    assertEquals("\"\"", getTokenText(text, 1));
    assertEquals("abcd", getTokenText(text, 2));
  }

  @Test public void empty_string_literal() throws IOException {
    assertEquals("''", firstTokenText("''"));
  }

  @Test public void eof() throws IOException {
    assertThat(firstToken(""), is(EOF));
  }

  @Test public void eof_terminated_character_literal() throws IOException {
    assertThat(firstToken("'m"), is(UNTERMINATED_CHARACTER_LITERAL));
  }

  @Test public void eof_terminated_string_literal() throws IOException {
    assertThat(firstToken("\"m"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void eof_terminated_string_literal_empty() throws IOException {
    assertThat(firstToken("\""), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void eof_terminated_string_literal_text() throws IOException {
    assertThat(firstToken("\"m"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void id() throws IOException {
    assertThat(firstToken("m"), is(IDENTIFIER));
  }

  @Test public void id_space() throws IOException {
    assertThat(firstToken("m "), is(IDENTIFIER));
  }

  @Test public void id_space_id() throws IOException {
    final String text = "id1 id2";
    assertThat(getToken(text, 1), is(IDENTIFIER));
    assertThat(getToken(text, 2), is(SPACE));
    assertThat(getToken(text, 3), is(IDENTIFIER));
    assertEquals("id1", getTokenText(text, 1));
    assertEquals(" ", getTokenText(text, 2));
    assertEquals("id2", getTokenText(text, 3));
  }

  @Test public void line_comment() throws IOException {
    assertThat(firstToken("// Comment\n"), is(LINE_COMMENT));
  }

  @Test public void line_comment_eof() throws IOException {
    assertThat(firstToken("// Comment"), is(LINE_COMMENT));
  }

  @Test public void long_character_literal() throws IOException {
    assertThat(firstToken("'masfasdfasdfas'"), is(CHARACTER_LITERAL));
  }

  @Test public void long_character_literal_text() throws IOException {
    assertEquals("'masfasdfasdfas'", firstTokenText("'masfasdfasdfas'"));
  }

  @Test public void long_string_literal() throws IOException {
    assertThat(firstToken("\"masfasdfasdfas\""), is(STRING_LITERAL));
  }

  @Test public void long_string_literal_text() throws IOException {
    assertEquals("'masfasdfasdfas'", firstTokenText("'masfasdfasdfas'"));
  }

  @Test public void nl_string_space_id_popen_integer() throws IOException {
    final String text = "\n\"\" abcd(12";
    assertThat(getToken(text, 1), is(NL));
    assertThat(getToken(text, 2), is(STRING_LITERAL));
    assertThat(getToken(text, 3), is(SPACE));
    assertThat(getToken(text, 4), is(IDENTIFIER));
  }

  /* escaped \ */
  @Test public void no_esc_block_comment() throws IOException {
    assertThat(firstToken("/* block Comment \\*/"), is(BLOCK_COMMENT));
  }

  /* /* No nested comment */
  @Test public void no_nested_block_comment() throws IOException {
    assertThat(firstToken("/*/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void no_nested_block_comment_text() throws IOException {
    assertEquals("/*/* block Comment */", firstTokenText("/*/* block Comment */"));
  }

  @Test public void no_nested_doc_comment_text_firstToken() throws IOException {
    assertEquals("/**/", firstTokenText("/**/** doc Comment */"));
  }

  @Test public void no_nested_doc_comment_text_secondToken() throws IOException {
    assertEquals("/** doc Comment */", secondTokenText("/**//** doc Comment */"));
  }

  @Test public void no_nested_doc_commentFirstToken() throws IOException {
    assertThat(firstToken("/**/** doc Comment */"), is(EMPTY_BLOCK_COMMENT));
  }

  @Test public void no_nested_doc_commentSecondToken() throws IOException {
    assertThat(secondToken("/**/** doc Comment */"), is(MULT));
  }

  // ===================================
  @Test public void one_char_string_literal() throws IOException {
    assertThat(firstToken("\"m\""), is(STRING_LITERAL));
  }

  @Test public void short_doc_comment_keyword() throws IOException {
    final String text = "/** a */ public\n" + //
        "public";
    reset(text);
    assertThat(t.next(), is(DOC_COMMENT));
    assertThat(t.next(), is(SPACE));
    assertThat(t.next(), is(_public));
  }

  @Test public void simple_character_literal() throws IOException {
    assertThat(firstToken("'m'"), is(CHARACTER_LITERAL));
  }

  @Test public void simple_character_literal_text() throws IOException {
    assertEquals("'m'", firstTokenText("'m'"));
  }

  @Test public void simple_string_literal_text() throws IOException {
    assertEquals("\"abc m'\"", firstTokenText("\"abc m'\""));
  }

  @Test public void something_after_emtpy_comment() throws IOException {
    assertThat(secondToken("/**/something"), is(IDENTIFIER));
  }

  @Test public void space_id_space() throws IOException {
    assertThat(getToken(" m ", 2), is(IDENTIFIER));
  }

  @Test public void string_id_string() throws IOException {
    final String text = "\"str1\"xid\"str2\"";
    assertThat(getToken(text, 1), is(STRING_LITERAL));
    assertThat(getToken(text, 2), is(IDENTIFIER));
    assertThat(getToken(text, 3), is(STRING_LITERAL));
    assertEquals("\"str1\"", getTokenText(text, 1));
    assertEquals("xid", getTokenText(text, 2));
    assertEquals("\"str2\"", getTokenText(text, 3));
  }

  @Test public void string_keyword() throws IOException {
    final String text = "\" \"public";
    reset(text);
    assertThat(t.next(), is(STRING_LITERAL));
    assertEquals("\" \"", t.text());
    assertThat(t.next(), is(_public));
    assertEquals("public", t.text());
  }

  @Test public void string_literal_of_quote() throws IOException {
    assertEquals("'\\''", firstTokenText("'\\''"));
  }

  @Test public void string_literal_of_quote_kind() throws IOException {
    assertThat(firstToken("\"\\\"\""), is(STRING_LITERAL));
  }

  @Test public void string_literal_with_double_backslash() throws IOException {
    assertEquals("'\\\\'", firstTokenText("'\\\\'"));
  }

  @Test public void string_literal_with_quote_text() throws IOException {
    assertEquals("\"\\\"\"", firstTokenText("\"\\\"\""));
  }

  @Test public void string_literal_with_tab() throws IOException {
    assertEquals("'\t'", firstTokenText("'\t'"));
  }

  @Test public void string_literal_with_triple_backslash() throws IOException {
    assertEquals("'\\\\\\''", firstTokenText("'\\\\\\''"));
  }

  @Test public void string_space_keyword() throws IOException {
    final String text = "\" \" public\n";
    reset(text);
    assertThat(t.next(), is(STRING_LITERAL));
    assertThat(t.next(), is(SPACE));
    assertThat(t.next(), is(_public));
  }

  @Test public void unterminated_block_comment() throws IOException {
    assertThat(firstToken("/*/* block Comment"), is(UNTERMINATED_BLOCK_COMMENT));
  }

  @Test public void unterminated_block_comment_text() throws IOException {
    assertEquals("/*/* block Comment", firstTokenText("/*/* block Comment"));
  }

  @Test public void unterminated_character_literal() throws IOException {
    assertEquals(firstToken("'m\n"), UNTERMINATED_CHARACTER_LITERAL);
  }

  @Test public void unterminated_doc_comment() throws IOException {
    assertThat(firstToken("/** doc Comment"), is(UNTERMINATED_DOC_COMMENT));
  }

  @Test public void unterminated_doc_comment_text() throws IOException {
    assertEquals("/*/* block Comment", firstTokenText("/*/* block Comment"));
  }

  @Test public void unterminated_string_literal() throws IOException {
    assertThat(firstToken("\"masfasdfasdf\n"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void unterminated_string_literal_text() throws IOException {
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
