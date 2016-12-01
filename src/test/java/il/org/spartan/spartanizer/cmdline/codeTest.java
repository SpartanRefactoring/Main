package il.org.spartan.spartanizer.cmdline;

import org.junit.*;

import il.org.spartan.spartanizer.utils.*;

/** @author Sharon Kuninin
 * @author Yarden Lev
 * @since Nov 12, 2016 */
@SuppressWarnings({ "static-method", "deprecation" })
public class codeTest {
  @Test public void noWords() {
    Assert.assertEquals(0, code.wc(""));
  }

  @Test public void oneWord() {
    Assert.assertEquals(1, code.wc("hello"));
  }

  @Test public void fiveWords() {
    Assert.assertEquals(5, code.wc("hello world how are you?"));
  }

  @Test public void zeroIsReturnedForNull() {
    Assert.assertEquals(0, code.wc(null));
  }

  @Test public void twoSpaces() {
    Assert.assertEquals("a a", code.essence("a  a"));
  }

  @Test public void emptyLine() {
    Assert.assertEquals("", code.essence("        \n"));
  }

  @Test public void whitespacesAreRemovedFromEOL() {
    Assert.assertEquals("a b c d", code.essence("a b c d    \n"));
  }

  @Test public void tabIsRemoved() {
    Assert.assertEquals("a b", code.essence("a \t  b \t"));
  }

  @Test public void tabIsRemoved2() {
    Assert.assertEquals("a b", code.essence("a  b \t\n"));
  }

  @Test public void spaceIsRemovedBetweenNonLetters() {
    Assert.assertEquals("23", code.essence("2 3"));
    Assert.assertEquals("3213333", code.essence("321 3333"));
    Assert.assertEquals("-=", code.essence("- ="));
  }

  @Test public void spaceIsRemovedBetweenLetterAndNonLetter() {
    Assert.assertEquals("a3", code.essence("a 3"));
    Assert.assertEquals("a=", code.essence("a ="));
    Assert.assertEquals("a-", code.essence("a -"));
  }

  @Test public void spaceIsRemovedBetweenNonLetterAndLetter() {
    Assert.assertEquals("2b", code.essence("2 b"));
    Assert.assertEquals("!some_boolean", code.essence("! some_boolean"));
    Assert.assertEquals("-some_int", code.essence("- some_int"));
  }

  @Test public void lineCommentIsRemoved() {
    Assert.assertEquals("int a;", code.essence("int a;// I am a line comment\r\n"));
  }

  @Test public void blockCommentIsRemoved() {
    Assert.assertEquals("int a=0;", code.essence("int a = 0; /* I am a block comment */"));
  }

  @Test public void blockCommentIsRemoved2() {
    Assert.assertEquals("++a;", code.essence("++a; /** Another block comment **/"));
  }

  @Test public void blockCommentIsRemoved3() {
    Assert.assertEquals("double a=0.0;", code.essence("double a = 0.0; /* block comment **/"));
  }

  @Test public void blockCommentIsRemoved4() {
    Assert.assertEquals("c=a+b;", code.essence("c = a + b; /** This will be removed */"));
  }
}
