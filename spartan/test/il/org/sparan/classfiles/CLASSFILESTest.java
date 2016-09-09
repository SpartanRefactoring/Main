package il.org.sparan.classfiles;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") public class CLASSFILESTest {
  @Test public void testFindAnonymous() {
    assertNotNull(CLASSFILES.open(new Object() {
      // Empty
    }.getClass()));
  }

  @Test public void testFindArray() {
    assertNull(CLASSFILES.open(String[].class));
  }

  @Test public void testFindInner() {
    assertNotNull(CLASSFILES.open(new InnerClass().getClass()));
  }

  @Test public void testFindInnerAnnotation() {
    assertNotNull(CLASSFILES.open(Annotation.class));
  }

  @Test public void testFindInnerEnum() {
    assertNotNull(CLASSFILES.open(InnerEnum.class));
  }

  @Test public void testFindInnerEnumValue() {
    assertNotNull(CLASSFILES.open(InnerEnumValues.A.getClass()));
    assertNotNull(CLASSFILES.open(InnerEnumValues.B.getClass()));
    assertNotNull(CLASSFILES.open(InnerEnumValues.class));
  }

  @Test public void testFindInnerInterface() {
    assertNotNull(CLASSFILES.open(InnerInterface.class));
  }

  @Test public void testFindJunitAssert() {
    assertNotNull(CLASSFILES.open(Assert.class));
  }

  @Test public void testFindJunitTest() {
    assertNotNull(CLASSFILES.open(Test.class));
  }

  @Test public void testFindLocal() {
    class Local {
      // Empty
    }
    assertNotNull(CLASSFILES.open(new Local().getClass()));
  }

  @Test public void testFindMe() {
    assertNotNull(CLASSFILES.open(this.getClass()));
  }

  @Test public void testFindObject() {
    assertNotNull(CLASSFILES.open(Object.class));
  }

  @Test public void testFindPrimitive() {
    assertNull(CLASSFILES.open(int.class));
  }

  @Test public void testFindPrimitiveArray() {
    assertNull(CLASSFILES.open(int[].class));
  }

  @Test public void testFindStaticInner() {
    assertNotNull(CLASSFILES.open(new StaticInnerClass().getClass()));
  }

  @Test public void testFindString() {
    assertNotNull(CLASSFILES.open(String.class));
  }

  @Test public void testFindStringBuilder() {
    assertNotNull(CLASSFILES.open(StringBuilder.class));
  }

  @Test public void testFindVoid() {
    assertNull(CLASSFILES.open(void.class));
  }

  @Test public void testIOException() {
    assertNotNull(CLASSFILES.open(IOException.class));
  }

  @Test public void testMeArray() {
    assertNull(CLASSFILES.open(CLASSFILESTest[].class));
  }

  @Test public void testSeparate() {
    assertNotNull(CLASSFILES.open(Separate.class));
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
