package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFGTestUtil.*;

import org.junit.*;

import il.org.spartan.utils.*;

/** Unit test for {@link CFG}.
 * @author Dor Ma'ayan
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
            .outs("0").contains("i=0") //
            .outs("i=0").contains("int i=0") //
            .ins("i<100").contains("100");
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
  // TODO Roth: verify array creation
  @Test public void arrayInitializer() {
    cfg("new X[] {{f()}, g()}") //
        .outs("f()").containsOnly("{f()}") //
        .outs("{f()}").containsOnly("g()") //
        .outs("g()").containsOnly("{{f()}, g()}") //
        .outs("{{f()}, g()}").containsOnly("new X[] {{f()}, g()}");
  }
  @Test public void assertStatement() {
    cfg("" //
        + "f();\n" //
        + "assert a : b;\n" //
        + "g();") //
            .ins("a").containsOnly("f()") //
            .outs("a").containsOnly("g()", "b") //
            .ins("g()").containsOnly("a");
  }
  @Test public void assignment() {
    cfg("x += y = z") //
        .outs("x").containsOnly("y") //
        .outs("y").containsOnly("z") //
        .outs("z").containsOnly("y = z") //
        .outs("y = z").containsOnly("x += y = z");
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
            .outs("b = 1").contains("int a = 0, b = 1;") //
            .outs("int a = 0, b = 1;").contains("c") //
            .outs("c").contains("d") //
            .outs("d").contains("f(c, d)");
  }
  @Test public void block() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  {g();}\n" //
        + "}") //
            .outs("int x").containsOnly("g()");
  }
  @Test public void blockEmpty() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  g();\n" //
        + "  ;\n" //
        + "  {}\n" //
        + "  {;}\n" //
        + "  h();\n" //
        + "}") //
            .outs("g()").containsOnly("h()");
  }
  @Test public void c() {
    cfg("" //
        + "void f(int x) {\n" //
        + "  int a = f(f(x));\n" //
        + "  f(b);" //
        + "}") //
            .outs("int x").contains("x") //
            .outs("f(x)").contains("f(f(x))") //
            .outs("f(f(x))").contains("a = f(f(x))") //
            .outs("a = f(f(x))").contains("int a = f(f(x));") //
            .outs("int a = f(f(x));").contains("b") //
            .outs("b").contains("f(b)");
    ;
  }
  @Test public void classInstanceCreation() {
    cfg("f(g(), h()).new I(j(), k())") //
        .outs("g()").containsOnly("h()") //
        .outs("h()").containsOnly("f(g(), h())") //
        .outs("f(g(), h())").containsOnly("j()") //
        .outs("j()").containsOnly("k()") //
        .outs("k()").containsOnly("f(g(), h()).new I(j(), k())");
  }
  @Test public void conditionalExpression1() {
    cfg("" //
        + "int a = b ? c : d;\n" //
        + "f();") //
            .outs("b").containsOnly("c", "d") //
            .outs("c").containsOnly("a = b ? c : d") //
            .outs("d").containsOnly("a = b ? c : d") //
            .outs("a = b ? c : d").containsOnly("int a = b ? c : d;").outs("int a = b ? c : d;").containsOnly("f()");
  }
  @Test public void conditionalExpression2() {
    cfg("" //
        + "T a = b(x,y) ? c : d(z,q);\n" //
        + "f();") //
            .outs("b(x,y)").containsOnly("c", "z") //
            .outs("c").containsOnly("a = b(x,y) ? c : d(z,q)") //
            .outs("z").containsOnly("q") //
            .outs("a = b(x,y) ? c : d(z,q)").containsOnly("T a = b(x,y) ? c : d(z,q);").outs("T a = b(x,y) ? c : d(z,q);").containsOnly("f()");
  }
  @Test public void d() {
    cfg("int d = a + x;") //
        .outs("a").containsOnly("x") //
        .outs("x").containsOnly("a + x") //
        .outs("a + x").containsOnly("d = a + x");
  }
  @Test public void doStatement() {
    cfg("" //
        + "do {\n" //
        + "  f();\n" //
        + "} while (b);\n" //
        + "g();\n" //
    ) //
        .outs("f()").containsOnly("b") //
        .outs("b").containsOnly("f()", "g()");
  }
  @Test public void doStatementEmpty() {
    cfg("" //
        + "do {\n" //
        + "} while (b);\n" //
        + "g();\n" //
    ) //
        .outs("b").containsOnly("g()", "b");
  }
  @Test public void fieldAccess() {
    cfg("f().g").outs("f()").containsOnly("g").outs("g").containsOnly("f().g");
  }
  @Test public void forStatementBasic() {
    cfg("for(int i=0;i<2;i++)" //
        + "{" //
        + "q();" //
        + "}" //
        + "r();") //
            .ins("int i=0").containsOnly("i=0") //
            .outs("i<2").containsOnly("q()", "r()") //
            .outs("q()").containsOnly("i") //
            .ins("i<2").containsOnly("2");
  }
  @Test public void forStatementBasicBreak() {
    cfg("for(int i=0;j<2;k++)" //
        + "{" //
        + "if(q())" //
        + " break;" //
        + "}" //
        + "r();") //
            .ins("int i=0").containsOnly("i=0") //
            .outs("j<2").containsOnly("q()", "r()") //
            .outs("break;").containsOnly("r()") //
            .outs("q()").containsOnly("k", "break;") //
            .ins("j<2").containsOnly("2");
  }
  @Test public void forStatementEmpty() {
    cfg("" //
        + "f();" //
        + "for(;;);" //
        + "h();") //
            .outs("f()").containsOnly("for(;;);") //
            .outs("for(;;);").containsOnly() //
            .ins("h()").containsOnly();
  }
  @Test public void forStatementEmptyBody() {
    cfg("" //
        + "f();" //
        + "for(;j<2;k++)" //
        + "  {;}" //
        + "h();") //
            .ins("j").contains("f()") //
            .outs("j<2").containsOnly("k", "h()") //
            .ins("h()").containsOnly("j<2");
  }
  @Test public void forStatementNoCondition() {
    cfg("" //
        + "f();" //
        + "for(int i=0;;k++)" //
        + "  g();" //
        + "h();") //
            .ins("0").contains("f()") //
            .outs("k++").containsOnly("g()") //
            .outs("g()").containsOnly("k") //
            .ins("h()").containsOnly();
  }
  @Test public void forStatementNoInitializers() {
    cfg("" //
        + "f();" //
        + "for(;j<2;k++)" //
        + "  g();" //
        + "h();") //
            .ins("j").contains("f()") //
            .outs("j<2").containsOnly("g()", "h()") //
            .outs("g()").containsOnly("k");
  }
  @Test public void forStatementNoUpdaters() {
    cfg("" //
        + "f();" //
        + "for(int i=0;j<2;)" //
        + "  g();" //
        + "h();") //
            .ins("j").contains("int i=0", "g()") //
            .outs("j<2").containsOnly("g()", "h()") //
            .outs("g()").containsOnly("j");
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
            .outs("boolean a = b instanceof C;").containsOnly("f()") //
            .outs("b instanceof C").containsOnly("a = b instanceof C") //
            .outs("a = b instanceof C").containsOnly("boolean a = b instanceof C;");
  }
  @Test public void lambdaExpression() {
    cfg("" //
        + "f();\n" //
        + "Function a = b -> c;\n" //
        + "g();") //
            .outs("b -> c").containsOnly("a = b -> c") //
            .ins("b -> c").containsOnly("f()") //
            .outs("a = b -> c").containsOnly("Function a = b -> c;")//
            .outs("Function a = b -> c;").containsOnly("g()");
  }
  @Test public void methodInvocation() {
    cfg("f().g(a)") //
        .outs("f()").containsOnly("a") //
        .outs("a").containsOnly("f().g(a)");
  }
  @Test public void methodReference() {
    cfg("" //
        + "f();\n" //
        + "g(h::i);\n" //
        + "j();") //
            .ins("h::i").containsOnly("f()") //
            .outs("h::i").containsOnly("g(h::i)") //
            .outs("g(h::i)").containsOnly("j()");
  }
  @Test public void parenthesizedExpression() {
    cfg("" //
        + "f();\n" //
        + "g((h::i));\n" //
        + "j();") //
            .ins("h::i").containsOnly("f()") //
            .outs("h::i").containsOnly("g((h::i))") //
            .outs("g((h::i))").containsOnly("j()");
  }
  @Test public void prepostfixExpression() {
    cfg("" //
        + "++i;" //
        + "j++;") //
            .outs("++i").containsOnly("j")//
            .outs("j").containsOnly("j++");
  }
  @Test public void superFieldAccess() {
    cfg("" //
        + "f(super.a);" //
        + "g(b.super.c);") //
            .outs("super.a").containsOnly("f(super.a)") //
            .outs("f(super.a)").containsOnly("b.super.c") //
            .outs("b.super.c").containsOnly("g(b.super.c)");
  }
  @Test public void superMethodInvocation() {
    cfg("" //
        + "f(super.a());" //
        + "g(b.super.c());") //
            .outs("super.a()").containsOnly("f(super.a())") //
            .outs("f(super.a())").containsOnly("b.super.c()") //
            .outs("b.super.c()").containsOnly("g(b.super.c())");
  }
  @Test public void thisExpression() {
    cfg("" //
        + "f();" //
        + "g(a.this.b());") //
            .ins("a.this.b()").containsOnly("a.this") //
            .outs("a.this").containsOnly("a.this.b()") //
            .outs("a.this.b()").containsOnly("g(a.this.b())");
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
            .outs("f1()").containsOnly( //
                "f2()", //
                "Exception1 x1", //
                "Exception2 x2") //
            .outs("x1.basa()").containsOnly("f2()");
  }
  @Test public void whileStatementBasic() {
    cfg("while(w(8)<s())" //
        + "{" //
        + "q();" //
        + "}" //
        + "r();") //
            .ins("w(8)").containsOnly("8") //
            .outs("w(8)<s()").containsOnly("q()", "r()") //
            .outs("q()").containsOnly("8");
  }
}
