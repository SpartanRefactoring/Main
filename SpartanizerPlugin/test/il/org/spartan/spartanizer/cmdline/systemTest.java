package il.org.spartan.spartanizer.cmdline;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;

/** TODO Sharon Kuninin please add a description
 * @author Sharon Kuninin
 * @author Yarden Lev
 * @since Nov 12, 2016 */
@SuppressWarnings({ "static-method", "deprecation" })
public class systemTest {
  @Test public void blockCommentIsRemoved() {
    azzert.that(system.essence("int a = 0; /* I am a block comment */"), is("int a=0;"));
  }

  @Test public void blockCommentIsRemoved2() {
    azzert.that(system.essence("++a; /** Another block comment **/"), is("++a;"));
  }

  @Test public void blockCommentIsRemoved3() {
    azzert.that(system.essence("double a = 0.0; /* block comment **/"), is("double a=0.0;"));
  }

  @Test public void blockCommentIsRemoved4() {
    azzert.that(system.essence("c = a + b; /** This will be removed */"), is("c=a+b;"));
  }

  @Test public void emptyLine() {
    azzert.that(system.essence("        \n"), is(""));
  }

  @Test public void fiveWords() {
    azzert.that(system.wc("hello world how are you?"), is(5));
  }

  @Test public void lineCommentIsRemoved() {
    azzert.that(system.essence("int a;// I am a line comment\r\n"), is("int a;"));
  }

  @Test public void noWords() {
    azzert.that(system.wc(""), is(0));
  }

  @Test public void oneWord() {
    azzert.that(system.wc("hello"), is(1));
  }

  @Test public void spaceIsRemovedBetweenLetterAndNonLetter() {
    azzert.that(system.essence("a 3"), is("a3"));
    azzert.that(system.essence("a ="), is("a="));
    azzert.that(system.essence("a -"), is("a-"));
  }

  @Test public void spaceIsRemovedBetweenNonLetterAndLetter() {
    azzert.that(system.essence("2 b"), is("2b"));
    azzert.that(system.essence("! some_boolean"), is("!some_boolean"));
    azzert.that(system.essence("- some_int"), is("-some_int"));
  }

  @Test public void spaceIsRemovedBetweenNonLetters() {
    azzert.that(system.essence("2 3"), is("23"));
    azzert.that(system.essence("321 3333"), is("3213333"));
    azzert.that(system.essence("- ="), is("-="));
  }

  @Test public void tabIsRemoved() {
    azzert.that(system.essence("a \t  b \t"), is("a b"));
  }

  @Test public void tabIsRemoved2() {
    azzert.that(system.essence("a  b \t\n"), is("a b"));
  }

  @Test public void twoSpaces() {
    azzert.that(system.essence("a  a"), is("a a"));
  }

  @Test public void whitespacesAreRemovedFromEOL() {
    azzert.that(system.essence("a b c d    \n"), is("a b c d"));
  }

  @Test public void zeroIsReturnedForNull() {
    azzert.that(system.wc(null), is(0));
  }
}
