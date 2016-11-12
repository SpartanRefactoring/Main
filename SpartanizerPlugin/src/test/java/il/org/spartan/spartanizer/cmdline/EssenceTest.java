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
}
