package il.org.spartan.reflection;

import static il.org.spartan.azzert.*;
import static il.org.spartan.reflection.ClassPredicates.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import nano.ly.*;

@SuppressWarnings("static-method") public class ClassPredicatesTest {
  @Test public void testBoxFields() {
    dump.go(Box.class.getSuperclass());
    azzert.that(nFields(Box.class), is(2));
  }

  @Test public void testBoxImmutable() {
    assert !isImmutable(Box.class);
  }

  @Test public void testEmptyEnumFields() {
    azzert.that(nFields(EmptyEnum.class), is(3));
  }

  @Test public void testEmptyEnumImmutable() {
    assert !isImmutable(EmptyEnum.class);
  }

  @Test public void testEmptyNonStaticClass() {
    assert !isImmutable(EmptyNonStaticClass.class);
  }

  @Test public void testEmptyNonStaticClassFields() {
    azzert.that(nFields(EmptyNonStaticClass.class), is(0));
  }

  @Test public void testEmptyStaticClass() {
    assert !isImmutable(EmptyStaticClass.class);
  }

  @Test public void testImmmutableInhertingFromMmutable() {
    assert !isImmutable(ImmmutableInhertingFromMmutable.class);
  }

  @Test public void testImmutableStatic() {
    assert isImmutable(ImmutableStatic.class);
  }

  @Test public void testIsImmutableFalse() {
    class A {
      int a = 3;
      @Nullable final Object o = null;

      int b() {
        a *= 2;
        return a + (o == null ? 1 : 2);
      }
    }
    new A().b();
    assert !isImmutable(new A().getClass());
  }

  @Test public void testIsImmutableInheritingTrue() {
    class A {
      final int a = 3;
      @Nullable final Object o = null;

      int b() {
        return a + (o == null ? 1 : 2);
      }
    }
    class B extends A {
      // Empty local class
    }
    new A().b(); // Use the class so that it does not eliminated by
    // Eclipse's "Save Actions".
    assert isImmutable(new B().getClass());
  }

  @Test public void testIsImmutableTrue() {
    class A {
      final int a = 3;
      @Nullable final Object o = null;

      int b() {
        return a + (o == null ? 1 : 2);
      }
    }
    new A().b();
    assert isImmutable(new A().getClass());
  }

  @Test public void testMutableInhertingFromImmutable() {
    assert !isImmutable(MutableInhertingFromImmutable.class);
  }

  @Test public void testMutableStatic() {
    assert !isImmutable(MutableStatic.class);
  }

  @Test public void testObject() {
    assert !isImmutable(Object.class);
  }

  @Test public void testStaticUtilityClassFields() {
    azzert.that(nFields(StaticUtilityClass.class), is(0));
  }

  @Test public void testStaticUtilityClassImmutable() {
    assert !isImmutable(StaticUtilityClass.class);
  }

  public static class ImmmutableInhertingFromMmutable extends MutableStatic {
    final int b = 3;
    @Nullable final Object object = null;
  }

  public static class ImmutableStatic {
    final int a = 3;
    @Nullable final Object o = null;
  }

  public static class MutableInhertingFromImmutable extends ImmutableStatic {
    int c = 3;
    Object object;
  }

  public static class MutableStatic {
    int a = 3;
    Object o;
  }

  enum EmptyEnum {
    ;
    public int x;

    int f() {
      return 2;
    }
  }

  class EmptyNonStaticClass {
    // Empty
  }

  static class EmptyStaticClass {
    // Empty
  }

  enum NonEmptyEnum {
    A, B, C;
    int a;

    int f() {
      return ordinal();
    }
  }

  static class StaticUtilityClass {
    static final int a = 19;
  }
}
