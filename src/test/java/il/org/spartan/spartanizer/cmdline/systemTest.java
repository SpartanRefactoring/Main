package il.org.spartan.spartanizer.cmdline;

import org.junit.*;

/** @author Sharon Kuninin
 * @author Yarden Lev
 * @since Nov 12, 2016 */
@SuppressWarnings({ "static-method", "deprecation" })
public class systemTest {
  @Test public void blockCommentIsRemoved() {
    Assert.assertEquals("int a=0;", system.essence("int a = 0; /* I am a block comment */"));
  }

  @Test public void blockCommentIsRemoved2() {
    Assert.assertEquals("++a;", system.essence("++a; /** Another block comment **/"));
  }

  @Test public void blockCommentIsRemoved3() {
    Assert.assertEquals("double a=0.0;", system.essence("double a = 0.0; /* block comment **/"));
  }

  @Test public void blockCommentIsRemoved4() {
    Assert.assertEquals("c=a+b;", system.essence("c = a + b; /** This will be removed */"));
  }

  @Test public void emptyLine() {
    Assert.assertEquals("", system.essence("        \n"));
  }

  @Test public void fiveWords() {
    Assert.assertEquals(5, system.wc("hello world how are you?"));
  }

  @Test public void lineCommentIsRemoved() {
    Assert.assertEquals("int a;", system.essence("int a;// I am a line comment\r\n"));
  }

  @Test public void noWords() {
    Assert.assertEquals(0, system.wc(""));
  }

  @Test public void oneWord() {
    Assert.assertEquals(1, system.wc("hello"));
  }

  @Test public void spaceIsRemovedBetweenLetterAndNonLetter() {
    Assert.assertEquals("a3", system.essence("a 3"));
    Assert.assertEquals("a=", system.essence("a ="));
    Assert.assertEquals("a-", system.essence("a -"));
  }

  @Test public void spaceIsRemovedBetweenNonLetterAndLetter() {
    Assert.assertEquals("2b", system.essence("2 b"));
    Assert.assertEquals("!some_boolean", system.essence("! some_boolean"));
    Assert.assertEquals("-some_int", system.essence("- some_int"));
  }

  @Test public void spaceIsRemovedBetweenNonLetters() {
    Assert.assertEquals("23", system.essence("2 3"));
    Assert.assertEquals("3213333", system.essence("321 3333"));
    Assert.assertEquals("-=", system.essence("- ="));
  }

  @Test public void tabIsRemoved() {
    Assert.assertEquals("a b", system.essence("a \t  b \t"));
  }

  @Test public void tabIsRemoved2() {
    Assert.assertEquals("a b", system.essence("a  b \t\n"));
  }

  @Test public void twoSpaces() {
    Assert.assertEquals("a a", system.essence("a  a"));
  }

  @Test public void whitespacesAreRemovedFromEOL() {
    Assert.assertEquals("a b c d", system.essence("a b c d    \n"));
  }

  @Test public void zeroIsReturnedForNull() {
    Assert.assertEquals(0, system.wc(null));
  }
}
