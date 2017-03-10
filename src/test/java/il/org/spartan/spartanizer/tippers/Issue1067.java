package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO: YuvalSimon <tt>yuvaltechnion@gmail.com</tt> please add a description
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-17 */
@SuppressWarnings("static-method")
public class Issue1067 {
  @Test public void t1() {
    trimmingOf("int zero = 0, result = 8 / zero;++result;").stays();
    // int zero = 1, result = 8;
    // ++result;
    // int a =0, res = 8;
    // f(res);
  }

  @Test public void t2() {
    trimmingOf("int div = 2, result = 8/div;++result;")// //
        .stays();
  }

  static class Issue0364 {
    @Test public void notTerminating() {
      trimmingOf("void f() {\n  String[] x = {\"\"};  g(x);  h();}").gives("void f() {\n  g(new String[] {\"\"});  h();}");
    }
  }

  static class Issue0856 {
    @Test public void e() {
      trimmingOf("  final InflaterListener il = (InflaterListener) ((TypedListener) l).getEventListener();" + //
          "il.finalize(); " + //
          "return 0;")//
              .gives(//
                  "((InflaterListener) ((TypedListener) l).getEventListener()).finalize();" + //
                      "return 0;" //
              )//
              .stays();
    }
  }

  static class Version230 {
    @Test public void inlineArrayInitialization1() {
      trimmingOf("public void multiDimensionalIntArraysAreEqual() {\n" //
          + "  int[][] int1 = {{1, 2, 3}, {4, 5, 6}};\n" //
          + "  int[][] int2 = {{1, 2, 3}, {4, 5, 6}};\n" //
          + "  assertArrayEquals(int1, int2);\n" //
          + "}")
              .gives("public void multiDimensionalIntArraysAreEqual() {\n" //
                  + "  int[][] int1 = {{1, 2, 3}, {4, 5, 6}}" //
                  + "  , int2 = {{1, 2, 3}, {4, 5, 6}};\n" //
                  + "  assertArrayEquals(int1, int2);\n" //
                  + "}")
              .gives("public void multiDimensionalIntArraysAreEqual() {\n" //
                  + "  assertArrayEquals(new int[][]{{1,2,3},{4,5,6}},new int[][]{{1,2,3},{4,5,6}});\n" //
                  + "}");
    }
  }
}
