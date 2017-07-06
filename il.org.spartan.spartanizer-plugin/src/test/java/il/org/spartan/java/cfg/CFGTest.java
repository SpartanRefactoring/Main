package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFGTestUtil.*;

import org.junit.*;

/** Unit test for {@link CFG}.
 * @author Ori Roth
 * @since 2017-07-06 */
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
}
