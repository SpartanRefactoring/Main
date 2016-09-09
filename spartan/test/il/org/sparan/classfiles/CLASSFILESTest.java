package il.org.sparan.classfiles;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") public class CLASSFILESTest {
  @Test public void testFindAnonymous() {
    assert null != CLASSFILES.open(new Object() {
      // Empty
    }.getClass());
  }

  @Test public void testFindArray() {
    assertNull(CLASSFILES.open(String[].class));
  }

  @Test public void testFindInner() {
    assert null != CLASSFILES.open(new InnerClass().getClass());
  }

  @Test public void testFindInnerAnnotation() {
    assert null != CLASSFILES.open(Annotation.class);
  }

  @Test public void testFindInnerEnum() {
    assert null != CLASSFILES.open(InnerEnum.class);
  }

  @Test public void testFindInnerEnumValue() {
    assert null != CLASSFILES.open(InnerEnumValues.A.getClass());
    assert null != CLASSFILES.open(InnerEnumValues.B.getClass());
    assert null != CLASSFILES.open(InnerEnumValues.class);
  }

  @Test public void testFindInnerInterface() {
    assert null != CLASSFILES.open(InnerInterface.class);
  }

  @Test public void testFindJunitAssert() {
    assert null != CLASSFILES.open(Assert.class);
  }

  @Test public void testFindJunitTest() {
    assert null != CLASSFILES.open(Test.class);
  }

  @Test public void testFindLocal() {
    class Local {
      // Empty
    }
    assert null != CLASSFILES.open(new Local().getClass());
  }

  @Test public void testFindMe() {
    assert null != CLASSFILES.open(this.getClass());
  }

  @Test public void testFindObject() {
    assert null != CLASSFILES.open(Object.class);
  }

  @Test public void testFindPrimitive() {
    assertNull(CLASSFILES.open(int.class));
  }

  @Test public void testFindPrimitiveArray() {
    assertNull(CLASSFILES.open(int[].class));
  }

  @Test public void testFindStaticInner() {
    assert null != CLASSFILES.open(new StaticInnerClass().getClass());
  }

  @Test public void testFindString() {
    assert null != CLASSFILES.open(String.class);
  }

  @Test public void testFindStringBuilder() {
    assert null != CLASSFILES.open(StringBuilder.class);
  }

  @Test public void testFindVoid() {
    assertNull(CLASSFILES.open(void.class));
  }

  @Test public void testIOException() {
    assert null != CLASSFILES.open(IOException.class);
  }

  @Test public void testMeArray() {
    assertNull(CLASSFILES.open(CLASSFILESTest[].class));
  }

  @Test public void testSeparate() {
    assert null != CLASSFILES.open(Separate.class);
  }

  static @interface Annotation {
    // Empty
  }

  class InnerClass {
    // Empty
  }

  enum InnerEnum {
    // Empty
  }

  enum InnerEnumValues {
    A() {
      @Override public String toString() {
        return "My value";
      }
    },
    B() {
      @Override public String toString() {
        return "My value";
      }
    };
  }

  interface InnerInterface {
    // Empty
  }

  class StaticInnerClass {
    // Empty
  }
}
