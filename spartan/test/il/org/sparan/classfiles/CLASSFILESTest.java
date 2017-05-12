package il.org.sparan.classfiles;

import java.io.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.classfiles.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") public class CLASSFILESTest {
  @Test public void testFindAnonymous() {
    assert CLASSFILES.open(new Object() {
      //
    }.getClass()) != null;
  }

  @Test public void testFindArray() {
    azzert.isNull(CLASSFILES.open(String[].class));
  }

  @Test public void testFindInner() {
    assert CLASSFILES.open(InnerClass.class) != null;
  }

  @Test public void testFindInnerAnnotation() {
    assert CLASSFILES.open(Annotation.class) != null;
  }

  @Test public void testFindInnerEnum() {
    assert CLASSFILES.open(InnerEnum.class) != null;
  }

  @Test public void testFindInnerEnumValue() {
    assert CLASSFILES.open(InnerEnumValues.A.getClass()) != null;
    assert CLASSFILES.open(InnerEnumValues.B.getClass()) != null;
    assert CLASSFILES.open(InnerEnumValues.class) != null;
  }

  @Test public void testFindInnerInterface() {
    assert CLASSFILES.open(InnerInterface.class) != null;
  }

  @Test public void testFindJunitAssert() {
    assert CLASSFILES.open(Assert.class) != null;
  }

  @Test public void testFindJunitTest() {
    assert CLASSFILES.open(Test.class) != null;
  }

  @Test public void testFindLocal() {
    class Local {
      // Empty
    }
    assert CLASSFILES.open(new Local().getClass()) != null;
  }

  @Test public void testFindMe() {
    assert CLASSFILES.open(this.getClass()) != null;
  }

  @Test public void testFindObject() {
    assert CLASSFILES.open(Object.class) != null;
  }

  @Test public void testFindPrimitive() {
    azzert.isNull(CLASSFILES.open(int.class));
  }

  @Test public void testFindPrimitiveArray() {
    azzert.isNull(CLASSFILES.open(int[].class));
  }

  @Test public void testFindStaticInner() {
    assert CLASSFILES.open(new StaticInnerClass().getClass()) != null;
  }

  @Test public void testFindString() {
    assert CLASSFILES.open(String.class) != null;
  }

  @Test public void testFindStringBuilder() {
    assert CLASSFILES.open(StringBuilder.class) != null;
  }

  @Test public void testFindVoid() {
    azzert.isNull(CLASSFILES.open(void.class));
  }

  @Test public void testIOException() {
    assert CLASSFILES.open(IOException.class) != null;
  }

  @Test public void testMeArray() {
    azzert.isNull(CLASSFILES.open(CLASSFILESTest[].class));
  }

  @Test public void testSeparate() {
    assert CLASSFILES.open(Separate.class) != null;
  }

  @interface Annotation {
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
      @Override  public String toString() {
        return "My value";
      }
    },
    B() {
      @Override  public String toString() {
        return "My value";
      }
    }
  }

  interface InnerInterface {
    // Empty
  }

  class StaticInnerClass {
    // Empty
  }
}
