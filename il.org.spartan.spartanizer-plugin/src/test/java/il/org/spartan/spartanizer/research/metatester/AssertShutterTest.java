package il.org.spartan.spartanizer.research.metatester;

import org.junit.Test;

import static il.org.spartan.spartanizer.research.metatester.AssertShutter.shutDown;
import static org.junit.Assert.assertEquals;

/**
 * @author Oren Afek
 * @since 28/5/2017.
 */
public class AssertShutterTest {

    @Test
    public void shutDownAlreadyAssertJ() {
        assertEquals("q.remove();", shutDown("assertThat(q.remove()).is(0);"));
        assertEquals("q.remove();", shutDown("assertThat(q.remove());"));
    }

    @Test
    public void cantShutDownNotAssertJ() {
        String $1 = "assertEquals(0,q.remove());";
        assertEquals($1, shutDown($1));
    }
}
