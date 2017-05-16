package il.org.spartan.Leonidas.auxilary_layer;

import il.org.spartan.Leonidas.PsiTypeHelper;

public class WrapperTest extends PsiTypeHelper {

    public void testGetAndSet() {
        Wrapper<Integer> wrapper = new Wrapper<>();
        assertNull(wrapper.get());
        Integer x = 2;
        wrapper.set(x);
        assertEquals(x, wrapper.get());
    }

    public void testCtorWithInit() {
        Integer x = 2, y = 5;
        Wrapper<Integer> wrapper = new Wrapper<>(x);
        assertEquals(x, wrapper.get());
        wrapper.set(y);
        assertNotSame(x, wrapper.get());
        assertEquals(y, wrapper.get());
    }

    public void testToString() {
        Wrapper<Integer> wrapper = new Wrapper<>();
        assertEquals("null", wrapper.toString());
        Integer x = 2;
        wrapper.set(x);
        assertEquals(x.toString(), wrapper.toString());
        assertEquals("string", (new Wrapper<>("string")).toString());
    }

    public void testHashCode() {
        Wrapper<Integer> wrapper = new Wrapper<>();
        assertEquals(0, wrapper.hashCode());
        Integer x = 2;
        wrapper.set(2);
        assertEquals(x.hashCode(), wrapper.hashCode());
        wrapper.set(x);
        assertEquals(x.hashCode(), wrapper.hashCode());
        assertEquals(Integer.valueOf(2).hashCode(), wrapper.hashCode());
    }

    public void testEquals() {
        assert (new Wrapper<>()).equals(new Wrapper<Integer>());
    }
}