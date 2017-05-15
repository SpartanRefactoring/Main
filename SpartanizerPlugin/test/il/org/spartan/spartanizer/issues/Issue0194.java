package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test for {@link IfCommandsSequencerNoElseSingletonSequencer}. Examine cases
 * where the last statement in the if statement then section is not an immediate
 * sequencer, or not a sequencer at all.
 * @author Yossi Gil
 * @author Ori Roth
 * @since Jan 6, 2017 */
@SuppressWarnings("static-method")
public final class Issue0194 {
  // TODO Ori convert to java doc --yg
  // Couple of tests to check that the tipper is safe with empty values, etc.
  // Empty "then".
  // There is some tipper that eliminates the empty "then", so the result is not
  // "stays();",
  // but what we care about is that early return does not apply.
  // It would have, if it could be, because thats the outer node.
  @Test public void test01() {
    trimmingOf("if(b1){"//
        + "x=13.5;"//
        + "if(b2){}\n"//
        + "else\n"//
        + "return g();" //
        + "}" //
        + "return h();")
            .gives("if(!b1)"//
                + "return h();" //
                + "x=13.5;"//
                + "if(b2){}\n"//
                + "else\n"//
                + "return g();" //
                + "return h();")
            .gives("if(!b1)"//
                + "return h();" //
                + "x=13.5;"//
                + "if(!b2)\n"//
                + "return g();" //
                + "return h();");
  }
  // TODO Ori convert to java doc --yg
  // Empty "else".
  // Similar to test01().
  @Test public void test02() {
    trimmingOf("if(b1){"//
        + "x=13.5;"//
        + "if(b2)\n"//
        + "return g();"//
        + "else{}"//
        + "}" //
        + "return h();")
            .gives("if(!b1)"//
                + "return h();" //
                + "x=13.5;"//
                + "if(b2)\n"//
                + "return g();"//
                + "else{}"//
                + "return h();")
            .givesEither(
                "if(!b1)"//
                    + "return h();" //
                    + "x=13.5;"//
                    + "if(b2)\n"//
                    + "return g();"//
                    + "return h();", //
                "if(!b1)"//
                    + "return h();" //
                    + "x=13.5;"//
                    + "return b2 ? g() : h();");
  }
  // TODO Ori convert to java doc --yg
  // Empty Block.
  // Similar to test01() and test02().
  @Test public void test03() {
    trimmingOf("/**/" //
        + "if(b1){"//
        + "x=13.5;"//
        + "{}"//
        + "}" //
        + "return h();")
            .gives("if(b1)"//
                + "x=13.5;"//
                + "return h();");
  }
  // TODO Ori convert to java doc --yg
  // Don'tipper do anything if there is more than return sideEffects after the
  // ifstatement.
  @Test public void test04() {
    trimmingOf("if (a == null) { \n" //
        + "a = f(); \n" //
        + "if (g()) \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
        + "} \n" //
        + "x=3; \n" //
        + "return null;" //
    )//
        .stays();
  }
  @Test public void test05() {
    trimmingOf("if(b1){ \n" //
        + "if(b2){ \n" //
        + "x = f(); \n" //
        + "return g(); \n" //
        + "} \n" //
        + "return h(); \n" //
        + "} \n" //
        + "return i();")
            .gives("if(!b1) \n" //
                + "return i(); \n" //
                + "if(b2){ \n" //
                + "x = f(); \n" //
                + "return g(); \n"//
                + "}" //
                + "return h(); \n" //
            )//
            .gives("if(!b1) \n" //
                + "return i(); \n" //
                + "if(!b2) \n" //
                + "return h(); \n" //
                + "x = f(); \n" //
                + "return g(); \n" //
    );
  }
  @Test public void test06() {
    trimmingOf("if(x> y){ \n" //
        + "x*=y; \n" //
        + "y*=z; \n" //
        + "z*=x*=y*=z; \n" //
        + "return z; \n" //
        + "} \n" //
        + "return x;")
            .gives("if(x <= y) \n" //
                + "return x; \n" //
                + "x*=y; \n" //
                + "y*=z; \n" //
                + "z*=x*=y*=z; \n" //
                + "return z; \n" //
    );
  }
  @Test public void test07() {
    trimmingOf("if(x == y){ \n" //
        + "x*=y; \n" //
        + "y*=z; \n" //
        + "z*=x*=y*=z; \n" //
        + "return z; \n" //
        + "} \n" //
        + "return x;")
            .gives("if(x != y) \n" //
                + "return x; \n" //
                + "x*=y; \n" //
                + "y*=z; \n" //
                + "z*=x*=y*=z; \n" //
                + "return z; \n" //
    );
  }
  @Test public void test08() {
    trimmingOf("if(x != null){ \n" //
        + "x*=y; \n" //
        + "y*=z; \n" //
        + "z*=x*=y*=z; \n" //
        + "return z; \n" //
        + "} \n" //
        + "return x;")
            .gives("if(x == null) \n" //
                + "return x; \n" //
                + "x*=y; \n" //
                + "y*=z; \n" //
                + "z*=x*=y*=z; \n" //
                + "return z; \n" //
    );
  }
  @Test public void test09() {
    trimmingOf("if( x != null){"//
        + "x = f();"//
        + "return g();"//
        + "}"//
        + "return null;")
            .gives("if( x == null)"//
                + "return null;"//
                + "x = f();"//
                + "return g();");
  }
  @Test public void test10() {
    trimmingOf("if(b1){ \n" //
        + "x=8;\n"//
        + "myFirstLabel:\n"//
        + "do{\n"//
        + "mySecondLabel:\n"//
        + "{\n" //
        + "if(x!=x) \n" //
        + "return y; \n"//
        + "else \n"//
        + "return w;\n"//
        + "}\n" //
        + "}while(5==3);\n"//
        + "} \n" //
        + "return z;")
            .gives("if(!b1) \n" //
                + "return z; \n" //
                + "x=8;\n"//
                + "myFirstLabel:"//
                + "do{"//
                + "mySecondLabel:"//
                + "{" //
                + "if(x!=x) \n" //
                + "return y; \n"//
                + "else \n"//
                + "return w;"//
                + "}\n"//
                + "}while(5==3);"); //
  }
  @Test public void test11() {
    trimmingOf("if(b1){ \n" //
        + "x=5; \n" //
        + "{\n" //
        + "return x; \n"//
        + "}" //
        + "} \n" //
        + "return z;")
            .gives("if(!b1) \n" //
                + "return z; \n" //
                + "x=5; \n" //
                + "return x; \n");
  }
  @Test public void test12() {
    trimmingOf("if(onoes() && omigod()){" //
        + "if(panic())" //
        + "return weGonnaDie();" //
        + "else{" //
        + "return meh();"//
        + "}" //
        + "}" //
        + "return noExcitement();" //
    )//
        .gives("if(!onoes() || !omigod())" //
            + "return noExcitement();" //
            + "if(panic())" //
            + "return weGonnaDie();" //
            + "else{" //
            + "return meh();"//
            + "}" //
    );
  }
  @Test public void test13() {
    trimmingOf("if(b1){"//
        + "if(b2){"//
        + "while(b3)"//
        + "return x;"//
        + "do{"//
        + "return y;"//
        + "}"//
        + "while(!b3);"//
        + "}"//
        + "return z;"//
        + "}"//
        + "return w;")
            .gives("if(!b1)"//
                + "return w;"//
                + "if(b2){"//
                + "while(b3)"//
                + "return x;"//
                + "do{"//
                + "return y;"//
                + "}"//
                + "while(!b3);"//
                + "}"//
                + "return z;")
            .gives("if(!b1)"//
                + "return w;"//
                + "if(!b2)"//
                + "return z;while(b3)"//
                + "return x;"//
                + "do{"//
                + "return y;"//
                + "}"//
                + "while(!b3);");
  }
  @Test public void test14() {
    trimmingOf("if(b1){" //
        + "x=3;" //
        + "if(b2)" //
        + "return f();"//
        + "meLabel:" //
        + "{" //
        + "y=7;" //
        + "return g();" //
        + "}" //
        + "}" //
        + "return h();")
            .gives("if(!b1)"//
                + "return h();"//
                + "x=3;"//
                + "if(b2)"//
                + "return f();" //
                + "meLabel:" //
                + "{"//
                + "y=7;"//
                + "return g();" //
                + "}");
  }
  @Test public void test15() {
    trimmingOf("/**/" //
        + "if (a != null) {\n" //
        + "  final X a = f();\n" //
        + "  if (g())\n" //
        + "    return f();\n" //
        + "  if (f() && g())  {\n" //
        + "    return a;\n" //
        + "  }\n" //
        + "}\n" //
        + "return null;")
            .gives("/**/" //
                + "if (a == null)\n" //
                + "  return null;\n" //
                + "final X a = f();\n" //
                + "if (g())\n" //
                + "  return f();\n" //
                + "if (f() && g())  {\n" //
                + "  return a;\n" //
                + "}\n" //
                + "return null;");
  }
}
