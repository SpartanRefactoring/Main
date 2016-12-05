package il.org.spartan.reflection;

import static il.org.spartan.azzert.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.reflection.DeepSize.*;
import il.org.spartan.sequence.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
public class DeepSizeTest {
  @NotNull private static MyHashMap<String, String> createHashTable(final int i) {
    @NotNull final MyHashMap<String, String> $ = new MyHashMap<>();
    for (int ¢ = 0; ¢ < i; ++¢)
      $.put(String.valueOf(¢ * ¢ + 1), String.valueOf((¢ + 5) * (¢ - 9) + 1));
    return $;
  }

  @Test public void DeepSize_of_Array_non_null() {
    @NotNull final Object[] os = makeRecursiveArray(83);
    azzert.that(DeepSize.of(os), is(ShallowSize.align(4 * os.length + 12)));
  }

  @Test public void getAllFields_objectByte() {
    azzert.that(DeepSize.Visitor.getAllFields(new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    }.getClass()).size(), is(2));
  }

  @Test public void getFields_Object() {
    azzert.that(Visitor.getAllFields(Object.class).size(), is(0));
  }

  @Test public void getFields_ObjectObject() {
    azzert.that(Visitor.getAllFields(ObjectObject.class).size(), is(1));
  }

  @Test public void new_Visitor_ClassWithArray() {
    @NotNull final ClassWithArray o = new ClassWithArray();
    @NotNull final Object[] os = makeRecursiveArray(3);
    o.os = os;
    azzert.that(new DeepSize.Visitor().size(o.os), is(DeepSize.of(o.os)));
  }

  @Test public void new_Visitor_size_ObjectInt() {
    azzert.that(new Visitor().size(new ObjectInt()), is(16));
  }

  @Test public void new_Visitor_size_ObjectObject() {
    @NotNull final Object o = new ObjectObject();
    azzert.that(new Visitor().size(o, Object.class), is(ShallowSize.of(o)));
  }

  @Test(expected = ClassCastException.class) public void new_Visitor_size_ObjectObjectArray() {
    azzert.that(new Visitor().size(new ObjectObject(), Object[].class), is(16));
  }

  @Test public void objectChar() {
    azzert.that(DeepSize.of(new ObjectChar()), is(16));
  }

  @Test public void objectInt() {
    @NotNull final Object o = new Object() {
      int __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    };
    azzert.that(DeepSize.of(o), is(ShallowSize.of(this) + ShallowSize.of(o)));
  }

  @Test public void objectSize_ObjectObject() {
    azzert.that(new Visitor().size(new ObjectObject()), is(16));
  }

  @Test public void objectSize_ObjectObject_ObjectObject() {
    @NotNull final Object o = new ObjectObject();
    azzert.that(DeepSize.of(o), is(ShallowSize.of(o)));
  }

  @Test public void of_array_0__bytes() {
    azzert.that(new byte[0].length, is(0));
    azzert.that(DeepSize.of(new byte[0]), is(16));
  }

  @Test public void of_array_4__bytes() {
    azzert.that(DeepSize.of(new byte[4]), is(16));
  }

  @Test public void of_array_of_nulls() {
    azzert.that(DeepSize.of(new Object[8]), is(48));
  }

  @Test public void of_array_of_objects() {
    @NotNull final Object[] os = new Object[8];
    for (int ¢ = 0; ¢ < os.length; ++¢)
      os[¢] = os;
    azzert.that(DeepSize.of(os), is(48));
  }

  @Test public void of_ClassWithArray() {
    @NotNull final ClassWithArray o = new ClassWithArray();
    @NotNull final Object[] os = makeRecursiveArray(79);
    o.os = os;
    for (int ¢ = 0; ¢ < os.length; ++¢)
      if (¢ % 2 == 1)
        os[¢] = o;
    azzert.that(DeepSize.of(o), is(ShallowSize.of(os) + ShallowSize.of(o)));
  }

  @Test public void of_ClassWithArray_non_null() {
    @NotNull final ClassWithArray o = new ClassWithArray();
    @NotNull final Object[] os = makeRecursiveArray(13);
    o.os = os;
    azzert.that(DeepSize.of(o), is(DeepSize.of(os) + ShallowSize.of(o)));
  }

  @Test public void of_ClassWithArray_null() {
    azzert.that(DeepSize.of(new ClassWithArray()), is(16));
  }

  @Test public void of_ClassWithArray_recursive() {
    @NotNull final ClassWithArray o = new ClassWithArray();
    @NotNull final Object[] os = makeRecursiveArray(3);
    o.os = os;
    azzert.that(DeepSize.of(o), is(ShallowSize.of(o) + ShallowSize.of(os)));
  }

  @Test public void of_ClassWithArrayReursiveArray() {
    final int arraySize = 6;
    @NotNull final Object o = new Object() {
      final Object os[] = makeRecursiveArray(arraySize);

      @Override public int hashCode() {
        return os.hashCode();
      }
    };
    DeepSize.of(o);
    azzert.that(DeepSize.of(o), is(ShallowSize.of(this) + ShallowSize.of(o) + DeepSize.of(makeRecursiveArray(arraySize))));
  }

  @Test public void of_ClassWithObjecReursiveArray() {
    final int arraySize = 23;
    @NotNull final Object o = new Object() {
      final Object o__ = makeRecursiveArray(arraySize);

      @Override public int hashCode() {
        return o__.hashCode();
      }
    };
    azzert.that(DeepSize.of(o), is(ShallowSize.of(this) + ShallowSize.of(o) + DeepSize.of(makeRecursiveArray(arraySize))));
  }

  @Test public void of_MyHashMap() {
    @NotNull final MyHashMap<Object, Object> m = new MyHashMap<>();
    azzert.that(ShallowSize.of(m), is(40));
    azzert.that(DeepSize.of(m), is(120));
    m.put(null, null);
    azzert.that(DeepSize.of(m), is(136 + ShallowSize.of(new Object())));
  }

  @Test public void of_MyHashMap_DEFAULT_INITIAL_CAPACITY() {
    azzert.that(new MyHashMap<>().table.length, is(MyHashMap.DEFAULT_INITIAL_CAPACITY));
  }

  @Test public void of_MyHashMap_table() {
    @NotNull final MyHashMap<Object, Object> m = new MyHashMap<>();
    azzert.that(DeepSize.of(m) - ShallowSize.of(m), is(ShallowSize.of(m.table) + DeepSize.of(m.keySet)));
  }

  @Test public void of_MyHashMap_table_size() {
    @NotNull final MyHashMap<Object, Object> m = new MyHashMap<>();
    azzert.that(ShallowSize.of(m.table), is(ShallowSize.arraySize(m.table.length)));
  }

  @Test public void of_MyHashMap_table_size_80() {
    azzert.that(ShallowSize.of(new MyHashMap<>().table), is(80));
  }

  @Test public void of_object() {
    azzert.that(DeepSize.of(new Object()), is(8));
  }

  @Test public void of_Object() {
    azzert.that(DeepSize.of(new Object()), is(8));
  }

  @Test public void of_ObjectBoolean() {
    azzert.that(DeepSize.of(new ObjectBoolean()), is(16));
  }

  @Test public void of_objectByte() {
    @NotNull final Object o = new Object() {
      byte __;

      @Override public int hashCode() {
        return super.hashCode() ^ __;
      }
    };
    azzert.that(DeepSize.of(o), is(ShallowSize.of(this) + ShallowSize.of(o)));
  }

  @Test public void of_ObjectInt_extends_ObjectInt() {
    azzert.that(DeepSize.of(new ObjectInt_extends_ObjectInt()), is(24));
  }

  @Test public void of_ObjectObject() {
    azzert.that(DeepSize.of(new ObjectObject()), is(16));
  }

  @Test public void of_objectStaticChar() {
    azzert.that(DeepSize.of(new ObjectStaticChar()), is(8));
  }

  @Test public void shallow_of_MyHashMap() {
    for (@NotNull final Sequence f = new Fibonacci(1000); f.more(); f.advance())
      azzert.that(ShallowSize.of(createHashTable(f.current())), is(ShallowSize.of(new MyHashMap<>())));
  }

  @Test public void ShallowSize_of_Array_non_null() {
    @NotNull final Object[] os = makeRecursiveArray(17);
    azzert.that(ShallowSize.of(os), is(ShallowSize.align(4 * os.length + 12)));
  }

  @Test public void Visitor_size_ObjectBoolean() {
    azzert.that(new DeepSize.Visitor().size(new ObjectBoolean()), is(16));
  }

  @NotNull Object[] makeRecursiveArray(final int i) {
    @NotNull final Object[] $ = new Object[i];
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
