package tests;

import metatester.MetaTester;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Oren Afek
 * @since 02-Jul-17.
 */
@RunWith(MetaTester.class)
public class Test1 {

    @Test
    public void testOnlySimpleAsserts() {
        assert 1 == 1;
        assert 0 == 1;
        assert 2 == 2;
    }

    @Test
    public void testOnlyJunitAsserts() {
        assertEquals(1, 1);
        assertEquals(0, 1);
        assertEquals(2, 2);
    }

    @Test
    public void testGreaterThanSmallerThan() {
        assert 1 < 2;
        assert 3 > 5;
    }

    int x = 0;

    private int f(int x) {
        return x;
    }

    @Test
    public void testWithMethodCallsNoSideEffects() {
        assertEquals(x, f(x));
        assertEquals(x, f(f(f(f(f(f(x)))))));
    }

    @Test
    public void testWithBetweens() {
        int a = 1, b = 2, c = 0;
        assert a < b && b < c;
        assert a <= b && b <= c;
    }

    @Test
    public void testThreeAsserts() {
        int x = 0;
        assertEquals(0, x++);
        assertEquals(1, x++);
        assertEquals(2, x);
    }

    @Test
    public void testInvocations() {
        List<String> l = new ArrayList<>();
        assertEquals(0, l.size());
        l.add("aba");
        assertTrue(l.contains("aba"));
    }

}
