package il.org.spartan.reflection;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.reflection.DeepSize.*;
import il.org.spartan.sequence.*;

@SuppressWarnings("static-method") public class DeepSizeTest {
  private static MyHashMap<String, String> createHashTable(final int n) {
    final MyHashMap<String, String> $ = new MyHashMap<>();
    for (int i = 0; i < n; i++)
      $.put(String.valueOf(i * i + 1), String.valueOf((i + 5) * (i - 9) + 1));
    return $;
  }

  @Test public void DeepSize__of__Array__non__null() {
    final Object[] os = makeRecursiveArray(83);
    assertEquals(ShallowSize.align(4 * os.length + 4 + 8), DeepSize.of(os));
  }

  @Test public void getAllFields__objectByte() {
    assertEquals(2, DeepSize.Visitor.getAllFields(new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    }.getClass()).size());
  }

  @Test public void getFields__Object() {
    assertEquals(0, Visitor.getAllFields(Object.class).size());
  }

  @Test public void getFields__ObjectObject() {
    assertEquals(1, Visitor.getAllFields(ObjectObject.class).size());
  }

  @Test public void new__Visitor__ClassWithArray() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(3);
    o.os = os;
    assertEquals(DeepSize.of(o.os), new DeepSize.Visitor().size(o.os));
  }

  @Test public void new__Visitor__size__ObjectInt() {
    assertEquals(16, new Visitor().size(new ObjectInt()));
  }

  @Test public void new__Visitor__size__ObjectObject() {
    final Object o = new ObjectObject();
    assertEquals(ShallowSize.of(o), new Visitor().size(o, Object.class));
  }

  @Test(expected = ClassCastException.class) public void new__Visitor__size__ObjectObjectArray() {
    assertEquals(16, new Visitor().size(new ObjectObject(), Object[].class));
  }

  @Test public void objectChar() {
    assertEquals(16, DeepSize.of(new ObjectChar()));
  }

  @Test public void objectInt() {
    final Object o = new Object() {
      int __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    };
    assertEquals(ShallowSize.of(this) + ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void objectSize__ObjectObject() {
    assertEquals(16, new Visitor().size(new ObjectObject()));
  }

  @Test public void objectSize__ObjectObject__ObjectObject() {
    final Object o = new ObjectObject();
    assertEquals(ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of__array__0__bytes() {
    assertEquals(0, new byte[0].length);
    assertEquals(16, DeepSize.of(new byte[0]));
  }

  @Test public void of__array__4__bytes() {
    assertEquals(16, DeepSize.of(new byte[4]));
  }

  @Test public void of__array__of__nulls() {
    assertEquals(48, DeepSize.of(new Object[8]));
  }

  @Test public void of__array__of__objects() {
    final Object[] os = new Object[8];
    for (int i = 0; i < os.length; i++)
      os[i] = os;
    assertEquals(48, DeepSize.of(os));
  }

  @Test public void of__ClassWithArray() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(79);
    o.os = os;
    for (int i = 0; i < os.length; i++)
      if (i % 2 == 1)
        os[i] = o;
    assertEquals(ShallowSize.of(os) + ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of__ClassWithArray__non__null() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(13);
    o.os = os;
    assertEquals(DeepSize.of(os) + ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of__ClassWithArray__null() {
    assertEquals(16, DeepSize.of(new ClassWithArray()));
  }

  @Test public void of__ClassWithArray__recursive() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(3);
    o.os = os;
    assertEquals(ShallowSize.of(o) + ShallowSize.of(os), DeepSize.of(o));
  }

  @Test public void of__ClassWithArrayReursiveArray() {
    final int arraySize = 6;
    final Object o = new Object() {
      final Object os[] = makeRecursiveArray(arraySize);

      @Override public int hashCode() {
        return os.hashCode();
      }
    };
    DeepSize.of(o);
    assertEquals(ShallowSize.of(this) + ShallowSize.of(o) + DeepSize.of(makeRecursiveArray(arraySize)), DeepSize.of(o));
  }

  @Test public void of__ClassWithObjecReursiveArray() {
    final int arraySize = 23;
    final Object o = new Object() {
      final Object o__ = makeRecursiveArray(arraySize);

      @Override public int hashCode() {
        return o__.hashCode();
      }
    };
    assertEquals(ShallowSize.of(this) + ShallowSize.of(o) + DeepSize.of(makeRecursiveArray(arraySize)), DeepSize.of(o));
  }

  @Test public void of__MyHashMap() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    assertEquals(40, ShallowSize.of(m));
    assertEquals(120, DeepSize.of(m));
    m.put(null, null);
    assertEquals(120 + 16 + ShallowSize.of(new Object()), DeepSize.of(m));
  }

  @Test public void of__MyHashMap__DEFAULT__INITIAL__CAPACITY() {
    assertEquals(MyHashMap.DEFAULT__INITIAL__CAPACITY, new MyHashMap<>().table.length);
  }

  @Test public void of__MyHashMap__table() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    final int shallow = ShallowSize.of(m);
    final int deep = DeepSize.of(m);
    assertEquals(ShallowSize.of(m.table) + DeepSize.of(m.keySet), deep - shallow);
  }

  @Test public void of__MyHashMap__table__size() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    assertEquals(ShallowSize.arraySize(m.table.length), ShallowSize.of(m.table));
  }

  @Test public void of__MyHashMap__table__size__80() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    assertEquals(80, ShallowSize.of(m.table));
  }

  @Test public void of__object() {
    assertEquals(8, DeepSize.of(new Object()));
  }

  @Test public void of__Object() {
    assertEquals(8, DeepSize.of(new Object()));
  }

  @Test public void of__ObjectBoolean() {
    assertEquals(16, DeepSize.of(new ObjectBoolean()));
  }

  @Test public void of__objectByte() {
    final Object o = new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    };
    assertEquals(ShallowSize.of(this) + ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of__ObjectInt__extends__ObjectInt() {
    assertEquals(24, DeepSize.of(new ObjectInt__extends__ObjectInt()));
  }

  @Test public void of__ObjectObject() {
    assertEquals(16, DeepSize.of(new ObjectObject()));
  }

  @Test public void of__objectStaticChar() {
    assertEquals(8, DeepSize.of(new ObjectStaticChar()));
  }

  @Test public void shallow__of__MyHashMap() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    final int baseSize = ShallowSize.of(m);
    for (final Sequence f = new Fibonacci(1000); f.more(); f.advance())
      assertEquals(baseSize, ShallowSize.of(createHashTable(f.current())));
  }

  @Test public void ShallowSize__of__Array__non__null() {
    final Object[] os = makeRecursiveArray(17);
    assertEquals(ShallowSize.align(4 * os.length + 4 + 8), ShallowSize.of(os));
  }

  @Test public void Visitor__size__ObjectBoolean() {
    assertEquals(16, new DeepSize.Visitor().size(new ObjectBoolean()));
  }

  Object[] makeRecursiveArray(final int n) {
    final Object[] $ = new Object[n];
    for (int i = 0; i < $.length; i++)
      $[i] = $;
    return $;
  }

  static class ClassWithArray {
    Object[] os;
  }

  static class ObjectBoolean {
    boolean __;
  }

  static class ObjectChar {
    char __;
  }

  static class ObjectInt {
    private int __;

    public int __() {
      return __;
    }
  }

  static class ObjectInt__extends__ObjectInt extends ObjectInt {
    private int __;

    @Override public int __() {
      return __;
    }
  }

  static class ObjectObject {
    final Object o;

    ObjectObject() {
      o = this;
    }

    ObjectObject(final Object o) {
      this.o = o;
    }
  }

  static class ObjectStaticChar {
    static char __;
  }
}
