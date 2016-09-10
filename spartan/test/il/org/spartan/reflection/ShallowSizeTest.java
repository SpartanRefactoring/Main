package il.org.spartan.reflection;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

@SuppressWarnings("static-method") public class ShallowSizeTest {
  @Test public void align_9() {
    assertEquals(16, ShallowSize.align(9));
  }

  @Test public void align1() {
    assertEquals(8, ShallowSize.align(1));
  }

  @Test public void align7() {
    assertEquals(8, ShallowSize.align(7));
  }

  @Test public void align8() {
    assertEquals(8, ShallowSize.align(8));
  }

  @Test public void intrinsic_ObjectObject() {
    assertEquals(4, ShallowSize.intrinsic(new ObjectObject()));
  }

  @Test public void intrinsicBoolean() {
    assertEquals(1, ShallowSize.intrinsic(new ObjectBoolean()));
  }

  @Test public void intrinsicSize_intrinsicBoolean() {
    assertEquals(1, ShallowSize.intrinsic(new ObjectBoolean()));
  }

  @Test public void intrinsicSize_ObjectObject_Object() {
    assertEquals(0, ShallowSize.intrinsic(Object.class));
  }

  @Test public void intrinsicSize_ObjectObject_ObjectObject() {
    assertEquals(4, ShallowSize.intrinsic(ObjectObject.class));
  }

  @Test public void objectBoolean() {
    assertEquals(16, ShallowSize.of(new ObjectBoolean()));
  }

  @Test public void objectByte() {
    assertEquals(16, ShallowSize.of(new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    }));
  }

  @Test public void objectChar() {
    assertEquals(16, ShallowSize.of(new ObjectChar()));
  }

  @Test public void of_array_0__bytes() {
    assertEquals(0, new byte[0].length);
    assertEquals(16, ShallowSize.of(new byte[0]));
  }

  @Test public void of_array_4__bytes() {
    assertEquals(16, ShallowSize.of(new byte[4]));
  }

  @Test public void of_HashMap() {
    final HashMap<Object, Object> m = new HashMap<>();
    assertEquals(40, ShallowSize.of(m));
    m.put(new Object(), new Object());
    assertEquals(40, ShallowSize.of(m));
    m.put(null, null);
    assertEquals(40, ShallowSize.of(m));
  }

  @Test public void of_Inner() {
    assertEquals(16, ShallowSize.of(new Object() {
      @Override public int hashCode() {
        return 17;
      }
    }));
  }

  @Test public void of_Interface() {
    assertEquals(16, ShallowSize.of(new Cloneable() {
      @Override public int hashCode() {
        return 33;
      }
    }));
  }

  @Test public void of_Null() {
    final Object o = null;
    assertEquals(0, ShallowSize.of(o));
  }

  @Test public void of_object() {
    assertEquals(8, ShallowSize.of(new Object()));
  }

  @Test public void of_Object() {
    assertEquals(8, ShallowSize.of(new Object()));
  }

  @Test public void of_objectBooleanO() {
    assertEquals(16, ShallowSize.of(new ObjectBoolean()));
  }

  @Test public void of_ObjectInt() {
    assertEquals(16, ShallowSize.of(new ObjectInt()));
  }

  @Test public void of_ObjectInt_Inner() {
    assertEquals(16, ShallowSize.of(new Object() {
      int __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    }));
  }

  @Test public void of_objectStaticChar() {
    assertEquals(8, ShallowSize.of(new ObjectStaticChar()));
  }

  @Test public void of_ObjectStaticInt() {
    assertEquals(8, ShallowSize.of(new ObjectStaticInt()));
  }

  @Test public void size_ObjectObject() {
    assertEquals(16, ShallowSize.of(new ObjectObject()));
  }

  @Test public void size_ObjectObject_Object() {
    assertEquals(8, ShallowSize.of(Object.class));
  }

  @Test public void size_ObjectObject_ObjectObject() {
    assertEquals(16, ShallowSize.of(ObjectObject.class));
  }

  static class ObjectBoolean {
    boolean __;
  }

  static class ObjectChar {
    char __;
  }

  static class ObjectInt {
    int __;
  }

  static class ObjectObject {
    Object self = this;
  }

  static class ObjectStaticChar {
    static char __;
  }

  static class ObjectStaticInt {
    static int __;
  }
}
