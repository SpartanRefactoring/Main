package il.org.spartan.reflection;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.reflection.DeepSize.*;
import il.org.spartan.sequence.*;

@SuppressWarnings("static-method") public class DeepSizeTest {
  private static MyHashMap<String, String> createHashTable(final int i) {
    final MyHashMap<String, String> $ = new MyHashMap<>();
    for (int ¢ = 0; ¢ < i; ++¢)
      $.put(String.valueOf(¢ * ¢ + 1), String.valueOf((¢ + 5) * (¢ - 9) + 1));
    return $;
  }

  @Test public void DeepSize_of_Array_non_null() {
    final Object[] os = makeRecursiveArray(83);
    assertEquals(ShallowSize.align(4 * os.length + 4 + 8), DeepSize.of(os));
  }

  @Test public void getAllFields_objectByte() {
    assertEquals(2, DeepSize.Visitor.getAllFields(new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    }.getClass()).size());
  }

  @Test public void getFields_Object() {
    assertEquals(0, Visitor.getAllFields(Object.class).size());
  }

  @Test public void getFields_ObjectObject() {
    assertEquals(1, Visitor.getAllFields(ObjectObject.class).size());
  }

  @Test public void new_Visitor_ClassWithArray() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(3);
    o.os = os;
    assertEquals(DeepSize.of(o.os), new DeepSize.Visitor().size(o.os));
  }

  @Test public void new_Visitor_size_ObjectInt() {
    assertEquals(16, new Visitor().size(new ObjectInt()));
  }

  @Test public void new_Visitor_size_ObjectObject() {
    final Object o = new ObjectObject();
    assertEquals(ShallowSize.of(o), new Visitor().size(o, Object.class));
  }

  @Test(expected = ClassCastException.class) public void new_Visitor_size_ObjectObjectArray() {
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

  @Test public void objectSize_ObjectObject() {
    assertEquals(16, new Visitor().size(new ObjectObject()));
  }

  @Test public void objectSize_ObjectObject_ObjectObject() {
    final Object o = new ObjectObject();
    assertEquals(ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of_array_0__bytes() {
    assertEquals(0, new byte[0].length);
    assertEquals(16, DeepSize.of(new byte[0]));
  }

  @Test public void of_array_4__bytes() {
    assertEquals(16, DeepSize.of(new byte[4]));
  }

  @Test public void of_array_of_nulls() {
    assertEquals(48, DeepSize.of(new Object[8]));
  }

  @Test public void of_array_of_objects() {
    final Object[] os = new Object[8];
    for (int ¢ = 0; ¢ < os.length; ++¢)
      os[¢] = os;
    assertEquals(48, DeepSize.of(os));
  }

  @Test public void of_ClassWithArray() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(79);
    o.os = os;
    for (int ¢ = 0; ¢ < os.length; ++¢)
      if (¢ % 2 == 1)
        os[¢] = o;
    assertEquals(ShallowSize.of(os) + ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of_ClassWithArray_non_null() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(13);
    o.os = os;
    assertEquals(DeepSize.of(os) + ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of_ClassWithArray_null() {
    assertEquals(16, DeepSize.of(new ClassWithArray()));
  }

  @Test public void of_ClassWithArray_recursive() {
    final ClassWithArray o = new ClassWithArray();
    final Object[] os = makeRecursiveArray(3);
    o.os = os;
    assertEquals(ShallowSize.of(o) + ShallowSize.of(os), DeepSize.of(o));
  }

  @Test public void of_ClassWithArrayReursiveArray() {
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

  @Test public void of_ClassWithObjecReursiveArray() {
    final int arraySize = 23;
    final Object o = new Object() {
      final Object o__ = makeRecursiveArray(arraySize);

      @Override public int hashCode() {
        return o__.hashCode();
      }
    };
    assertEquals(ShallowSize.of(this) + ShallowSize.of(o) + DeepSize.of(makeRecursiveArray(arraySize)), DeepSize.of(o));
  }

  @Test public void of_MyHashMap() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    assertEquals(40, ShallowSize.of(m));
    assertEquals(120, DeepSize.of(m));
    m.put(null, null);
    assertEquals(120 + 16 + ShallowSize.of(new Object()), DeepSize.of(m));
  }

  @Test public void of_MyHashMap_DEFAULT_INITIAL_CAPACITY() {
    assertEquals(MyHashMap.DEFAULT_INITIAL_CAPACITY, new MyHashMap<>().table.length);
  }

  @Test public void of_MyHashMap_table() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    assertEquals(ShallowSize.of(m.table) + DeepSize.of(m.keySet), DeepSize.of(m) - ShallowSize.of(m));
  }

  @Test public void of_MyHashMap_table_size() {
    final MyHashMap<Object, Object> m = new MyHashMap<>();
    assertEquals(ShallowSize.arraySize(m.table.length), ShallowSize.of(m.table));
  }

  @Test public void of_MyHashMap_table_size_80() {
    assertEquals(80, ShallowSize.of(new MyHashMap<>().table));
  }

  @Test public void of_object() {
    assertEquals(8, DeepSize.of(new Object()));
  }

  @Test public void of_Object() {
    assertEquals(8, DeepSize.of(new Object()));
  }

  @Test public void of_ObjectBoolean() {
    assertEquals(16, DeepSize.of(new ObjectBoolean()));
  }

  @Test public void of_objectByte() {
    final Object o = new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    };
    assertEquals(ShallowSize.of(this) + ShallowSize.of(o), DeepSize.of(o));
  }

  @Test public void of_ObjectInt_extends_ObjectInt() {
    assertEquals(24, DeepSize.of(new ObjectInt_extends_ObjectInt()));
  }

  @Test public void of_ObjectObject() {
    assertEquals(16, DeepSize.of(new ObjectObject()));
  }

  @Test public void of_objectStaticChar() {
    assertEquals(8, DeepSize.of(new ObjectStaticChar()));
  }

  @Test public void shallow_of_MyHashMap() {
    for (final Sequence f = new Fibonacci(1000); f.more(); f.advance())
      assertEquals(ShallowSize.of(new MyHashMap<>()), ShallowSize.of(createHashTable(f.current())));
  }

  @Test public void ShallowSize_of_Array_non_null() {
    final Object[] os = makeRecursiveArray(17);
    assertEquals(ShallowSize.align(4 * os.length + 4 + 8), ShallowSize.of(os));
  }

  @Test public void Visitor_size_ObjectBoolean() {
    assertEquals(16, new DeepSize.Visitor().size(new ObjectBoolean()));
  }

  Object[] makeRecursiveArray(final int i) {
    final Object[] $ = new Object[i];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = $;
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

  static class ObjectInt_extends_ObjectInt extends ObjectInt {
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
