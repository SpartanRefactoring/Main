package il.org.spartan.spartanizer.cmdline;

import static org.junit.Assert.*;

import org.junit.*;

/** @author Shay Segal
 * @author Sefi Albo
 * @author Daniel Shames
 * @since 12-11-2016 */
@SuppressWarnings("static-method") public class EssenceTest {
  @Test public void simpleTest() {
    assertEquals("", Essence.of("      "));
    assertEquals("a", Essence.of("      a"));
    assertEquals("a b", Essence.of("a      b"));
    assertEquals("", Essence.of("//comment"));
    assertEquals("!?", Essence.of("! ?"));
    assertEquals("a?", Essence.of("a ?"));
    assertEquals("!a", Essence.of("! a"));
    assertEquals("a", Essence.of("\n\na"));
    assertEquals("", Essence.of("\n\n"));
    assertEquals("a a", Essence.of("a\n\na"));
    assertEquals("", Essence.of("/*blablabla*/"));
    assertEquals("", Essence.of("\r\n"));
    assertEquals("", Essence.of("\n\r"));
  }
  @Test public void simpleTest2() {
    assertEquals("a b", Essence.of("a    b\r\n\n\r"));
    assertEquals("a b c d", Essence.of("a  b\r\nc\n\r\td\r\n\r"));
    assertEquals("", Essence.of("//comment\r\n\r\r\r\n"));
    assertEquals("", Essence.of("/*multi_line_comment\n*\n*/"));
    assertEquals("try_tabs b", Essence.of("try_tabs\t\t   b \t\t\t\r\n"));
    assertEquals("10 3", Essence.of("1\t0 3"));
    assertEquals("1078", Essence.of("1\t07\t\t\t8"));
  }
}
