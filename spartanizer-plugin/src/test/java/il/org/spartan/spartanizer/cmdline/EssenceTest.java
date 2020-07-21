package il.org.spartan.spartanizer.cmdline;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.java.*;

/** TODO Shay Segal please add a description
 * @author Shay Segal
 * @author Sefi Albo
 * @author Daniel Shames
 * @since 12-11-2016 */
@SuppressWarnings("static-method")
public class EssenceTest {
  @Test public void simpleTest() {
    azzert.that(Essence.of("      "), is(""));
    azzert.that(Essence.of("      a"), is("a"));
    azzert.that(Essence.of("a      b"), is("a b"));
    azzert.that(Essence.of("//comment"), is(""));
    azzert.that(Essence.of("! ?"), is("!?"));
    azzert.that(Essence.of("a ?"), is("a?"));
    azzert.that(Essence.of("! a"), is("!a"));
    azzert.that(Essence.of("\n\na"), is("a"));
    azzert.that(Essence.of("\n\n"), is(""));
    azzert.that(Essence.of("a\n\na"), is("a a"));
    azzert.that(Essence.of("/*blablabla*/"), is(""));
    azzert.that(Essence.of("\r\n"), is(""));
    azzert.that(Essence.of("\n\r"), is(""));
  }
  @Test public void simpleTest2() {
    azzert.that(Essence.of("a    b\r\n\n\r"), is("a b"));
    azzert.that(Essence.of("a  b\r\nc\n\r\td\r\n\r"), is("a b c d"));
    azzert.that(Essence.of("//comment\r\n\r\r\r\n"), is(""));
    azzert.that(Essence.of("/*multi_line_comment\n*\n*/"), is(""));
    azzert.that(Essence.of("try_tabs\t\t   b \t\t\t\r\n"), is("try_tabs b"));
    azzert.that(Essence.of("1\t0 3"), is("10 3"));
    azzert.that(Essence.of("1\t07\t\t\t8"), is("1078"));
  }
  @Test public void simpleTest3() {
    azzert.that(Essence.of("basic test"), is("basic test"));
    azzert.that(Essence.of("//////comment"), is(""));
    azzert.that(Essence.of("string\r_b4_cmnt//comment"), is("string_b4_cmnt"));
  }
}
