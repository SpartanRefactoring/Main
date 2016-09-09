package il.org.spartan.reflection;

import static il.org.spartan.reflection.ClassPredicates.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") public class ClassPredicatesTest {
  @Test public void testBoxFields() {
    dump.go(Box.class.getSuperclass());
    assertEquals(2, nFields(Box.class));
  }

  @Test public void testBoxImmutable() {
    assertFalse(isImmutable(Box.class));
  }

  @Test public void testEmptyEnumFields() {
    assertEquals(3, nFields(EmptyEnum.class));
  }

  @Test public void testEmptyEnumImmutable() {
    assertFalse(isImmutable(EmptyEnum.class));
  }

  @Test public void testEmptyNonStaticClass() {
    assertFalse(isImmutable(EmptyNonStaticClass.class));
  }

  @Test public void testEmptyNonStaticClassFields() {
    assertEquals(0, nFields(EmptyNonStaticClass.class));
  }

  @Test public void testEmptyStaticClass() {
    assertFalse(isImmutable(EmptyStaticClass.class));
  }

  @Test public void testImmmutableInhertingFromMmutable() {
    assertFalse(isImmutable(ImmmutableInhertingFromMmutable.class));
  }

  @Test public void testImmutableStatic() {
    assertTrue(isImmutable(ImmutableStatic.class));
  }

  @Test public void testIsImmutableFalse() {
    class A {
      int a = 3;
      final Object o = null;

      int b() {
        a = 2 * a;
        return a + (o == null ? 1 : 2);
      }
    }
    new A().b();
    assertFalse(isImmutable(new A().getClass()));
  }

  @Test public void testIsImmutableInheritingTrue() {
    class A {
      final int a = 3;
      final Object o = null;

      int b() {
        return a + (o == null ? 1 : 2);
      }
    }
    class B extends A {
      // Empty local class
    }
    new A().b(); // Use the class so that it does not eliminated by
    // Eclipse's "Save Actions".
    assertTrue(isImmutable(new B().getClass()));
  }

  @Test public void testIsImmutableTrue() {
    class A {
      final int a = 3;
      final Object o = null;

      int b() {
        return a + (o == null ? 1 : 2);
      }
    }
    new A().b();
    assertTrue(isImmutable(new A().getClass()));
  }

  @Test public void testMutableInhertingFromImmutable() {
    assertFalse(isImmutable(MutableInhertingFromImmutable.class));
  }

  @Test public void testMutableStatic() {
    assertFalse(isImmutable(MutableStatic.class));
  }

  @Test public void testObject() {
    assertFalse(isImmutable(Object.class));
  }

  @Test public void testStaticUtilityClassFields() {
    assertEquals(0, nFields(StaticUtilityClass.class));
  }

  @Test public void testStaticUtilityClassImmutable() {
    assertFalse(isImmutable(StaticUtilityClass.class));
  }

  public static class ImmmutableInhertingFromMmutable extends MutableStatic {
    final int b = 3;
    final Object object = null;
  }

  public static class ImmutableStatic {
    final int a = 3;
    final Object o = null;
  }

  public static class MutableInhertingFromImmutable extends ImmutableStatic {
    int c = 3;
    Object object = null;
  }

  public static class MutableStatic {
    int a = 3;
    Object o = null;
  }

  static enum EmptyEnum {
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

  static enum NonEmptyEnum {
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
