package il.org.spartan.spartanizer.research.metatester;

import org.junit.Test;

import static il.org.spartan.spartanizer.research.metatester.AssertSilencer.shutDown;
import static org.junit.Assert.assertEquals;

/** @author Oren Afek
 * @since 28/5/2017. */
@SuppressWarnings("static-method")
public class AssertShutterTest {
  @Test public void shutDownAlreadyAssertJ() {
    assertEquals("q.remove();", shutDown("assertThat(q.remove()).is(0);"));
    assertEquals("q.remove();", shutDown("assertThat(q.remove());"));
  }
  @Test public void cantShutDownNotAssertJ() {
    assertEquals("assertEquals(0,q.remove());", shutDown("assertEquals(0,q.remove());"));
  }
}
