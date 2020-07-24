// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.strings;

import static fluent.ly.azzert.is;
import static il.org.spartan.strings.RE.anyNumberOfReluctant;
import static il.org.spartan.strings.RE.group;
import static il.org.spartan.strings.RE.ignoreCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.streotypes.Immutable;
import il.org.spartan.streotypes.Instantiable;
import il.org.spartan.utils.Separate;

/** A representation of an HTML tag, capable of wrapping a given {@link String}.
 *
 * @author Yossi Gil, the Technion.
 * @since 11/07/2008 */
@Immutable @Instantiable public class Tag {
  /** A pre-made instance, representing the HTML &lt;strong&gt; tag. */
  public static final Tag strong = new Tag("strong");
  /** A pre-made instance, representing the HTML &lt;em&gt; tag. */
  public static final Tag em = new Tag("em");
  /** A pre-made instance, representing the HTML &lt;pre&gt; tag. */
  public static final Tag pre = new Tag("pre");
  /** A pre-made instance, representing the HTML &lt;tt&gt; tag. */
  public static final Tag tt = new Tag("tt");
  /** A pre-made instance, representing the HTML &lt;p&gt; tag, used for marking a
   * paragraph. */
  public static final Tag p = new Tag("p");
  /** A pre-made instance, representing the HTML &lt;u&gt; tag, used for
   * underlining the wrapped text. */
  public static final Tag u = new Tag("u");
  /** A pre-made instance, representing the HTML &lt;h1&gt; tag. */
  public static final Tag h1 = new Tag("h1");
  /** A pre-made instance, representing the HTML &lt;h2&gt; tag. */
  public static final Tag h2 = new Tag("h2");
  /** A pre-made instance, representing the HTML &lt;h3&gt; tag. */
  public static final Tag h3 = new Tag("h3");
  /** A pre-made instance, representing the HTML &lt;h4&gt; tag. */
  public static final Tag h4 = new Tag("h4");
  /** A pre-made instance, representing the HTML &lt;h5&gt; tag. */
  public static final Tag h5 = new Tag("h5");
  /** A pre-made instance, representing the HTML &lt;h6&gt; tag. */
  public static final Tag h6 = new Tag("h6");

  /** Make a {@link String} of an HTML opening tag with a given name.
   *
   * @param name the name of the given tag.
   * @return the name enclosed in angular brackets. */
  public static String beginTag(final String name) {
    return "<" + name + ">";
  }

  /** Make a {@link String} of an HTML closing tag with a given name.
   *
   * @param name the name of the given tag.
   * @return the name enclosed in angular brackets. */
  public static String endTag(final String name) {
    return beginTag("/" + name);
  }

  public static String remove(final String text, final String tag) {
    return text//
        .replaceAll(ignoreCase() + beginTag(tag), "") //
        .replaceAll(ignoreCase() + endTag(tag), "") //
        .replaceAll(ignoreCase() + selfClosing(tag), "") //
    ;
  }

  public static String replace(final String text, final String from, final String to) {
    return text//
        .replaceAll(ignoreCase() + beginTag(from), beginTag(to)) //
        .replaceAll(ignoreCase() + endTag(from), endTag(to));
  }

  /** Make a self closing {@link String} of an HTML tag with a given name.
   *
   * @param name the name of the given tag.
   * @return the name parameter, followed by slash (/) and enclosed in angular
   *         brackets. */
  public static String selfClosing(final String name) {
    return beginTag(name + " /");
  }

  /** The opening {@link String} of this tag. */
  public final String begin;
  /** The closing {@link String} of this tag. */
  public final String end;

  /** Instantiate a plain tag, i.e., a tag without any inner tags,
   *
   * @param name  the tag name, e.g., "font"
   * @param flags any number of HTML flags */
  public Tag(final String name, final String... flags) {
    assert name != null;
    assert flags != null;
    begin = beginTag(name + (flags.length == 0 ? "" : " " + Separate.by(flags, " ")));
    end = beginTag("/" + name);
  }

  /** Instantiate a tag containing another tag
   *
   * @param inner the inner tag; all instances of the newly created tag will be
   *              around this inner tag
   * @param name  the tag name, e.g., "font"
   * @param flags any number of HTML flags */
  public Tag(final Tag inner, final String name, final String... flags) {
    assert name != null;
    assert flags != null;
    final Tag unnested = new Tag(name, flags);
    begin = unnested.begin + inner.begin;
    end = inner.end + unnested.end;
  }

  /** A factory function, creating a new tag, containing this one. Typical use
   * demonstrates tag containment. The expression
   *
   * <pre>
   * new Tag(&quot;strong&quot;).inside(&quot;tt&quot;)
   * </pre>
   *
   * returns a newly created nested {@link Tag} composed of a <tt>strong</tt>
   * within a <tt>tt</tt> tag.
   *
   * @param name  the name of the newly created tag, e.g., "font"
   * @param flags any number of HTML flags to be used with the newly created tag
   * @return A newly created tag with the specified name and flags, containing
   *         this tag */
  public Tag inside(final String name, final String... flags) {
    return new Tag(this, name, flags);
  }

  /** Make a {@link Matcher} of a given text, to capture the opening and closing
   * tag together with the enclosed content in this text.
   *
   * @param ¢ where to look for this text?
   * @return {@link Matcher} of the parameter to capture the tag and its content.
   *         The content is in group number 1. */
  public Matcher makeMatcher(final String ¢) {
    return makePattern().matcher(¢);
  }

  /** Make a {@link Pattern} to capture the opening and closing tag together with
   * the enclosed content.
   *
   * @return a regular expression to capture the tag and its content. The content
   *         is in group number 1. */
  public Pattern makePattern() {
    return Pattern.compile(makeRegularExpression());
  }

  /** Make a regular expression to capture the opening and closing tag together
   * with the enclosed content.
   *
   * @return a regular expression to capture the tag and its content. The content
   *         is in group number 1. */
  public String makeRegularExpression() {
    return ignoreCase() + begin + group(anyNumberOfReluctant(".|[\r\n]")) + end;
  }

  /** Wrap a given string within this tag.
   *
   * @param ¢ a non-<code><b>null</b></code> representing the string to wrap
   * @return the string <code>s</code> wrapped with the tag, e.g., if
   *         <code>s</code> is <code>"Hello"</code> and the tag name is
   *         <code>"b"</code> then <code>"<b>Hello"</b>"</code> is returned. */
  public String wrap(final String ¢) {
    assert ¢ != null;
    return ¢.length() == 0 ? ¢ : begin + ¢ + end;
  }

  /** Wrap a given string within newline characters and then within this tag.
   *
   * @param ¢ a non-<code><b>null</b></code> representing the string to wrap
   * @return the string <code>s</code> wrapped with the tag, e.g., if
   *         <code>s</code> is <code>"Hello"</code> and the tag name is
   *         <code>"b"</code> then the string <code>"<b>\nHello\n</b>"</code> is
   *         returned. */
  public String wrapNL(final String ¢) {
    assert ¢ != null;
    return wrap("\n" + ¢ + "\n");
  }

  @SuppressWarnings("static-method") public static class TEST {
    private static final String tagRegularExpression = new Tag("dummy").makeRegularExpression();

    @Test public void testCRLFinPre() {
      azzert.that("A<dummy>\r\n</dummy>C".replaceFirst(tagRegularExpression, "B"), is("ABC"));
    }

    @Test public void testDummyInContext() {
      azzert.that(
          "\t /**\r\n\t  * BEFORE\r\n\t  * <dummy>\r\n\t  * text\r\n\t  * </dummy>\r\n\t  * AFTER\r\n\t  */"
              .replaceFirst(tagRegularExpression, "Content"),
          is("\t /**\r\n\t  * BEFORE\r\n\t  * Content\r\n\t  * AFTER\r\n\t  */"));
    }

    @Test public void testEmptyPre() {
      azzert.that("A<dummy></dummy>C".replaceFirst(tagRegularExpression, "B"), is("ABC"));
    }

    @Test public void testLFinPre() {
      azzert.that("A<dummy>\n</dummy>C".replaceFirst(tagRegularExpression, "B"), is("ABC"));
    }

    @Test public void testMiXeDCaSeTag() {
      azzert.that("A<DuMmY>a\nb\r\nABCDE</dUmMy>C".replaceFirst(tagRegularExpression, "B"), is("ABC"));
    }

    @Test public void testSeveralLinesInPre() {
      azzert.that("A<dummy>a\nb\r\nABCDE</dummy>C".replaceFirst(tagRegularExpression, "B"), is("ABC"));
    }

    @Test public void testSimplePre() {
      azzert.that("A<dummy>X</dummy>C".replaceFirst(tagRegularExpression, "B"), is("ABC"));
    }

    @Test public void testUpperCaseTag() {
      azzert.that("A<DUMMY>a\nb\r\nABCDE</DUMMY>C".replaceFirst(tagRegularExpression, "B"), is("ABC"));
    }
  }
}
