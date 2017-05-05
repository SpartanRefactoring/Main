package il.org.spartan.reflection;

import static fluent.ly.azzert.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class ShallowSizeTest {
  @Test public void align_9() {
    azzert.that(ShallowSize.align(9), is(16));
  }

  @Test public void align1() {
    azzert.that(ShallowSize.align(1), is(8));
  }

  @Test public void align7() {
    azzert.that(ShallowSize.align(7), is(8));
  }

  @Test public void align8() {
    azzert.that(ShallowSize.align(8), is(8));
  }

  @Test public void intrinsic_ObjectObject() {
    azzert.that(ShallowSize.intrinsic(new ObjectObject()), is(4));
  }

  @Test public void intrinsicBoolean() {
    azzert.that(ShallowSize.intrinsic(new ObjectBoolean()), is(1));
  }

  @Test public void intrinsicSize_intrinsicBoolean() {
    azzert.that(ShallowSize.intrinsic(new ObjectBoolean()), is(1));
  }

  @Test public void intrinsicSize_ObjectObject_Object() {
    azzert.that(ShallowSize.intrinsic(Object.class), is(0));
  }

  @Test public void intrinsicSize_ObjectObject_ObjectObject() {
    azzert.that(ShallowSize.intrinsic(ObjectObject.class), is(4));
  }

  @Test public void objectBoolean() {
    azzert.that(ShallowSize.of(new ObjectBoolean()), is(16));
  }

  @Test public void objectByte() {
    azzert.that(ShallowSize.of(new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    }), is(16));
  }

  @Test public void objectChar() {
    azzert.that(ShallowSize.of(new ObjectChar()), is(16));
  }

  @Test public void of_array_0__bytes() {
    azzert.that(new byte[0].length, is(0));
    azzert.that(ShallowSize.of(new byte[0]), is(16));
  }

  @Test public void of_array_4__bytes() {
    azzert.that(ShallowSize.of(new byte[4]), is(16));
  }

  @Test public void of_HashMap() {
     final HashMap<Object, Object> m = new HashMap<>();
    azzert.that(ShallowSize.of(m), is(40));
    m.put(new Object(), new Object());
    azzert.that(ShallowSize.of(m), is(40));
    m.put(null, null);
    azzert.that(ShallowSize.of(m), is(40));
  }

  @Test public void of_Inner() {
    azzert.that(ShallowSize.of(new Object() {
      @Override public int hashCode() {
        return 17;
      }
    }), is(16));
  }

  @Test public void of_Interface() {
    azzert.that(ShallowSize.of(new Cloneable() {
      @Override public int hashCode() {
        return 33;
      }
    }), is(16));
  }

  @Test public void of_Null() {
    azzert.that(ShallowSize.of((Object) null), is(0));
  }

  @Test public void of_object() {
    azzert.that(ShallowSize.of(new Object()), is(8));
  }

  @Test public void of_Object() {
    azzert.that(ShallowSize.of(new Object()), is(8));
  }

  @Test public void of_objectBooleanO() {
    azzert.that(ShallowSize.of(new ObjectBoolean()), is(16));
  }

  @Test public void of_ObjectInt() {
    azzert.that(ShallowSize.of(new ObjectInt()), is(16));
  }

  @Test public void of_ObjectInt_Inner() {
    azzert.that(ShallowSize.of(new Object() {
      int __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    }), is(16));
  }

  @Test public void of_objectStaticChar() {
    azzert.that(ShallowSize.of(new ObjectStaticChar()), is(8));
  }

  @Test public void of_ObjectStaticInt() {
    azzert.that(ShallowSize.of(new ObjectStaticInt()), is(8));
  }

  @Test public void size_ObjectObject() {
    azzert.that(ShallowSize.of(new ObjectObject()), is(16));
  }

  @Test public void size_ObjectObject_Object() {
    azzert.that(ShallowSize.of(Object.class), is(8));
  }

  @Test public void size_ObjectObject_ObjectObject() {
    azzert.that(ShallowSize.of(ObjectObject.class), is(16));
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
