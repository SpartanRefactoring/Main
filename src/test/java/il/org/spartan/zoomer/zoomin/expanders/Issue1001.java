package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Unit test for {@link AssignmentOperatorExpansion}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 [[SuppressWarningsSpartan]] */
@Ignore
@SuppressWarnings("static-method")
public class Issue1001 {
  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unused")
  public static class Issue1001Aux extends ReflectiveTester {
    void x(final int y) {
      //
    }

    void f1() {}

    void f2() {
      int a;
      int b;
      a = 0;
      b = 0;
      x(a += b += 1);
    }

    void f3() {
      int a;
      a = 0;
      x(a += 1);
    }

    void f4() {
      int a;
      int b;
      a = 0;
      b = 0;
      x(a %= b |= 1);
    }

    void f5() {
      int a;
      double b;
      a = 0;
      b = 0;
      x(a += b += 1);
    }

    public static Issue1001Aux instance() {
      return new Issue1001Aux();
    }
  }

  @Test public void basic() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f1() {\n" //
        + "  int a;\n" //
        + "  a = 0;\n" //
        + "  a = a + 1;\n" //
        + "}", "f1");
  }

  @Test public void inclusion() {
    bloatingOf(Issue1001Aux.instance())
        .givesWithBinding("" //
            + "void f2() {\n" //
            + "  int a;\n" //
            + "  int b;\n" //
            + "  a = 0;\n" //
            + "  b = 0;\n" //
            + "  x(a = a + (b += 1));\n" //
            + "}", "f2")
        .givesWithBinding("" //
            + "void f2() {\n" //
            + "  int a;\n" //
            + "  int b;\n" //
            + "  a = 0;\n" //
            + "  b = 0;\n" //
            + "  x(a = a + (b = b + 1));\n" //
            + "}", "f2");
  }

  @Test public void inclusion2() {
    bloatingOf(Issue1001Aux.instance()).givesWithBinding("" //
        + "void f3() {\n" //
        + "  int a;\n" //
        + "  int b;\n" //
        + "  a = 0;\n" //
        + "  b = 0;\n" //
        + "  x(a = a + (b = 1));\n" //
        + "}", "f3");
  }

  @Test public void operators() {
    bloatingOf(Issue1001Aux.instance())
        .givesWithBinding("" //
            + "void f4() {\n" //
            + "  int a;\n" //
            + "  int b;\n" //
            + "  a = 0;\n" //
            + "  b = 0;\n" //
            + "  x(a = a % (b |= 1));\n" //
            + "}", "f4")
        .givesWithBinding("" //
            + "void f4() {\n" //
            + "  int a;\n" //
            + "  int b;\n" //
            + "  a = 0;\n" //
            + "  b = 0;\n" //
            + "  x(a = a % (b = b | 1));\n" //
            + "}", "f4");
  }

  @Test public void nonMatchingPrimitives() {
    bloatingOf(Issue1001Aux.instance()).staysWithBinding();
  }
}
