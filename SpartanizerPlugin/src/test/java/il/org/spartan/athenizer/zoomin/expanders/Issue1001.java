package il.org.spartan.athenizer.zoomin.expanders;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;
import il.org.spartan.spartanizer.meta.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit test for {@link AssignmentOperatorBloater}.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2016-12-25 [[SuppressWarningsSpartan]] */
public class Issue1001 extends BloaterTest<Assignment> {
  @Override public Tipper<Assignment> bloater() {
    return new AssignmentOperatorBloater();
  }
  @Override public Class<Assignment> tipsOn() {
    return Assignment.class;
  }
  @Test public void basic() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f1() {\n" //
        + "  int a = 0;\n" //
        + "  a = a + 1;\n" //
        + "  x(a);" //
        + "}", "f1");
  }
  @Test public void inclusion() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f2() {\n" //
        + "  int a;\n" //
        + "  int b;\n" //
        + "  a = 0;\n" //
        + "  b = 0;\n" //
        + "  x(a = a + (b += 1));\n" //
        + "}", "f2");
  }
  @Test public void inclusion2() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f22() {\n" //
        + "  int a;\n" //
        + "  int b;\n" //
        + "  a = 0;\n" //
        + "  b = 0;\n" //
        + "  x(a = a + (b = b + 1));\n" //
        + "}", "f22");
  }
  @Test public void inclusion3() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f3() {\n" //
        + "  int a;\n" //
        + "  int b;\n" //
        + "  a = 0;\n" //
        + "  b = 0;\n" //
        + "  x(a = a + (b = 1));\n" //
        + "  x(b);" //
        + "}", "f3");
  }
  @Test public void nonMatchingPrimitives() {
    bloatingOf(Issue1001Aux.instance()).staysWithBinding();
  }
  @Test public void operators() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f4() {\n" //
        + "  int a;\n" //
        + "  int b;\n" //
        + "  a = 0;\n" //
        + "  b = 0;\n" //
        + "  x(a = a % (b |= 1));\n" //
        + "}", "f4");
  }
  @Test public void operators2() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f44() {\n" //
        + "  int a;\n" //
        + "  int b;\n" //
        + "  a = 0;\n" //
        + "  b = 0;\n" //
        + "  x(a = a % (b = b | 1));\n" //
        + "}", "f44");
  }
  @Test public void byteTypeBug() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "static byte f5() {\n" //
        + "byte a = 1;" + "byte b = 2;" + "a = (byte) (a + b);" + "return a;" + "}", "f5");
  }

  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings({ "unused", "TooBroadScope" })
  public static class Issue1001Aux extends MetaFixture {
    public static Issue1001Aux instance() {
      return new Issue1001Aux();
    }
    void f1() {
      int a = 0;
      a += 1;
      x(a);
    }
    void f2() {
      int a;
      int b;
      a = 0;
      b = 0;
      x(a += b += 1);
    }
    void f22() {
      int a;
      int b;
      a = 0;
      b = 0;
      x(a = a + (b += 1));
    }
    void f3() {
      int a;
      int b;
      a = 0;
      b = 0;
      x(a += b = 1);
      x(b);
    }
    void f4() {
      int a;
      int b;
      a = 0;
      b = 0;
      x(a %= b |= 1);
    }
    void f44() {
      int a;
      int b;
      a = 0;
      b = 0;
      x(a = a % (b |= 1));
    }
    void x(final int y) {
      //
    }
    static byte f5() {
      byte a = 1;
      final byte b = 2;
      a += b;
      return a;
    }
  }
}
