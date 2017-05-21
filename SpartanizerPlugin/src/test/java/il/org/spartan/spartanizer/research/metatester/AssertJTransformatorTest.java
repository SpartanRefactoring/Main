package il.org.spartan.spartanizer.research.metatester;

import org.junit.Test;

import static il.org.spartan.spartanizer.research.metatester.AssertJTransformator.transformIfPossible;
import static org.junit.Assert.assertEquals;

/**
 * @author Oren Afek
 * @since 5/19/2017.
 */

public class AssertJTransformatorTest {

    @Test
    public void testTransformations() {
        assertEquals("assertThat(listy).isEmpty();", transformIfPossible("assertEquals(0,listy.size());"));
        assertEquals("assertThat(listy).hasSize(9);", transformIfPossible("assertEquals(9,listy.size());"));
        assertEquals("assertThat(1 + 1).isEqualTo(2);", transformIfPossible("assertEquals(2,1 + 1);"));
        assertEquals("assertThat(listy).isNull();", transformIfPossible("assertNull(listy);"));
        assertEquals("assertThat(listy).isNotNull();", transformIfPossible("assertNotNull(listy);"));
        assertEquals("assertThat(1 >= 1).isTrue();", transformIfPossible("assertTrue(1 >= 1);"));
        assertEquals("assertThat(0 < 0).isFalse();", transformIfPossible("assertFalse(0 < 0);"));

    }
}