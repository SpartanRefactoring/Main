package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFGTestUtil.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.utils.*;

/** Unit test for {@link CFG}.
 * @author Ori Roth
 * @since 2017-07-06 */
@UnderConstruction("Dor -- 07/07/2017")
@SuppressWarnings("static-method")
public class CFGTest {
  @Test public void a() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  for (int i=0 ; i<100 ; ++i)\n" //
        + "    f(i);\n" //
        + "}") //
            .outs("int i=0").contains("i<100") //
            .ins("i<100").contains("int i=0") //
            .outs("f(i);").contains("++i");
  }
  @Test public void b() {
    cfg("" //
        + "void f(int x, int y) {\n" //
        + "  int a = 0, b = 0;\n" //
        + "  f(a + 0, b + 0);" //
        + "}") //
            .outs("a = 0").contains("b = 0") //
            .outs("b = 0").contains("a + 0") //
            .outs("a + 0").contains("b + 0") //
            .outs("b + 0").contains("f(a + 0, b + 0);");
  }
  @Test public void c() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  int a = f(f(x));\n" //
        + "  f(a);" //
        + "}") //
            .outs("int x").contains("int a = f(f(x));") //
            .outs("f(x)").contains("f(f(x))") //
            .outs("f(f(x))").contains("a") //
            .outs("a").contains("f(a)");
    ;
  }
  @Test public void d() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  int a = x+0 == 0+0 ? 1+1 : x == 2+2 : 3+3 : 4+4;\n" //
        + "  f(a);" //
        + "}") //
            .outs("x+0 == 0+0 ? 1+1 : x == 2+2 : 3+3 : 4+4").contains("x+0==0+0") //
            .outs("x+0==0+0").contains("x+0")//
            .outs("x+0").contains("0+0") //
            .outs("0+0").contains("1+1", "x == 2+2 : 3+3 : 4+4") //
            .outs("1+1").contains("f(a)");
  }
  @Test public void e() {
    cfg("" //
        + "int d = a+x;") //
            .outs("a+x").contains("a");
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
            .outs(TryStatement.class).containsOnly("f1();") //
            .outs("f1();").containsOnly("f()") //
            .outs("f1()")
            .containsOnly( //
                "f2();", //
                "catch(Exception1 x1){x1.basa();}", //
                "catch(Exception2 x2){x2.basa();}") //
            .outs("x1.basa()").containsOnly("f2();");
  }
  @Test public void arrayAccess() {
    cfg("a()[b()][c()]") //
        .outs("a()[b()][c()]").containsOnly("a()") //
        .outs("a()").containsOnly("b()") //
        .outs("b()").containsOnly("c()");
  }
  @Test public void arrayCreation() {
    cfg("new X[f()][g()]") //
        .outs("new X[f()][g()]").containsOnly("f()") //
        .outs("f()").containsOnly("g()");
  }
  @Test public void arrayInitializer() {
    cfg("new X[] {f(), g()}") //
        .outs("new X[] {f(), g()}").containsOnly("f()") //
        .outs("f()").containsOnly("g()");
  }
  @Test public void assignment() {
    cfg("x = y = z") //
        .outs("x = y = z").containsOnly("x") //
        .outs("x").containsOnly("y = z") //
        .outs("y = z").containsOnly("y") //
        .outs("y").containsOnly("z");
  }
  @Test public void classInstanceCreation() {
    cfg("f(g(), h()).new I(j(), k())") //
        .outs("f(g(), h()).new I(j(), k())").containsOnly("f(g(), h())") //
        .outs("f(g(), h())").containsOnly("g()") //
        .outs("g()").containsOnly("h()") //
        .outs("h()").containsOnly("j()") //
        .outs("j()").containsOnly("k()");
  }
  @Test public void conditionalExpression() {
    cfg("" //
        + "int a = b ? c : d;\n" //
        + "f();") //
            .outs("int a = b ? c : d;").containsOnly("a = b ? c : d") //
            .outs("a = b ? c : d").containsOnly("b ? c : d") //
            .outs("b ? c : d").containsOnly("c", "d") //
            .outs("c").containsOnly("f();") //
            .outs("d").containsOnly("f();");
  }
}
