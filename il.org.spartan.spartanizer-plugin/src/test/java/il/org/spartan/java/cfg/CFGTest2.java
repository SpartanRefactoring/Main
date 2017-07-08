package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFGTestUtil.*;

import org.junit.*;

import il.org.spartan.utils.*;

/** Unit test for {@link CFG}.
 * @author Ori Roth
 * @since 2017-07-06 */
@UnderConstruction("Dor -- 07/07/2017")
@SuppressWarnings("static-method")
public class CFGTest2 {
  @Test public void a() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  for (int i=0 ; i<100 ; ++i)\n" //
        + "    f(i);\n" //
        + "}") //
            .outs("0").contains("i=0") //
            .outs("i=0").contains("i<100") //
            .ins("i<100").contains("i=0") //
            .outs("f(i)").contains("++i");
  }
  @Test public void b() {
    cfg("" //
        + "void f(int x, int y) {\n" //
        + "  int a = 0, b = 1;\n" //
        + "  f(c, d);" //
        + "}") //
            .outs("0").contains("a = 0") //
            .outs("a = 0").contains("1") //
            .outs("1").contains("b = 1") //
            .outs("b = 1").contains("c") //
            .outs("c").contains("d") //
            .outs("d").contains("f(c, d)");
  }
  @Test public void c() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  int a = f(f(x));\n" //
        + "  f(b);" //
        + "}") //
            .outs("int x").contains("f(x)") //
            .outs("f(x)").contains("f(f(x))") //
            .outs("f(f(x))").contains("a = f(f(x))") //
            .outs("a = f(f(x))").contains("b") //
            .outs("b").contains("f(b)");
    ;
  }
  @Test public void d() {
    cfg("int d = a + x;") //
        .outs("a").contains("x") //
        .outs("x").contains("a + x") //
        .outs("a + x").contains("d = a + x");
  }
  @Test public void tryStatement() {
    cfg("" //
        + "try {\n" //
        + "  f1();\n" //
        + "} catch (Exception1 x1) {\n" //
        + "  x1.basa();\n" //
        + "} catch (Exception2 x2) {\n" //
        + "  x2.basa();\n" //
        + "}\n" //
        + "f2();") //
            .outs("f1()")
            .containsOnly( //
                "f2()", //
                "Exception1 x1", //
                "Exception2 x2") //
            .outs("x1.basa()").containsOnly("f2()");
  }
  @Test public void arrayAccess() {
    cfg("a()[b()][c()]") //
        .outs("a()").containsOnly("b()") //
        .outs("b()").containsOnly("a()[b()]") //
        .outs("a()[b()]").containsOnly("c()") //
        .outs("c()").containsOnly("a()[b()][c()]");
  }
  @Test public void arrayCreation() {
    cfg("new X[f()][g()]") //
        .outs("f()").containsOnly("g()") //
        .outs("g()").containsOnly("new X[f()][g()]");
  }
  @Test public void arrayInitializer() {
    cfg("new X[] {f(), g()}") //
        .outs("f()").containsOnly("g()") //
        .outs("g()").containsOnly("new X[] {f(), g()}");
  }
  @Test public void assignment() {
    cfg("x += y = z") //
        .outs("x").containsOnly("y") //
        .outs("y").containsOnly("z") //
        .outs("z").containsOnly("y = z") //
        .outs("y = z").containsOnly("x += y = z");
  }
  @Test public void classInstanceCreation() {
    cfg("f(g(), h()).new I(j(), k())") //
        .outs("g()").containsOnly("h()") //
        .outs("h()").containsOnly("f(g(), h())") //
        .outs("f(g(), h())").containsOnly("j()") //
        .outs("j()").containsOnly("k()") //
        .outs("k()").containsOnly("f(g(), h()).new I(j(), k())");
  }
  @Test public void conditionalExpression() {
    cfg("" //
        + "int a = b ? c : d;\n" //
        + "f();") //
            .outs("int a = b ? c : d;").containsOnly("b") //
            .outs("b").containsOnly("c", "d") //
            .outs("c").containsOnly("a = b ? c : d") //
            .outs("d").containsOnly("a = b ? c : d") //
            .outs("a = b ? c : d").containsOnly("f();");
  }
  @Test public void fieldAccess() {
    cfg("f().g") //
        .outs("f()").containsOnly("f().g");
  }
  @Test public void infixExpression() {
    cfg("w * x + y * z") //
        .outs("w").containsOnly("x") //
        .outs("x").containsOnly("w * x") //
        .outs("w * x").containsOnly("y") //
        .outs("y").containsOnly("z") //
        .outs("z").containsOnly("y * z") //
        .outs("y * z").containsOnly("w * x + y * z");
  }
  @Test public void instanceofExpression() {
    cfg("" //
        + "boolean a = b instanceof C;\n" //
        + "f();") //
            .outs("boolean a = b instanceof C;").containsOnly("b instanceof C") //
            .outs("b instanceof C").containsOnly("a = b instanceof C") //
            .outs("a = b instanceof C").containsOnly("f()");
  }
}
