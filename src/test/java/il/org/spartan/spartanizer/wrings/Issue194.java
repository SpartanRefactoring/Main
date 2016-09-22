package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method") public final class Issue194 {
  @Ignore public void test01() {
    trimmingOf("if (a != null) { \n" //
        + "a = f(); \n" //
        + "if (g()) \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
        + "} \n" //
        + "return null;" //
    ).gives("if (a == null) \n" + "return null;" //
        + "a = f(); \n" //
        + "if (g()); \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
    );
  }

  @Ignore public void test02() {
    trimmingOf("if (a == null) { \n" //
        + "a = f(); \n" //
        + "if (g()) \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
        + "} \n" //
        + "return null;" //
    ).gives("if (a != null) \n" + "return null;" //
        + "a = f(); \n" //
        + "if (g()); \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
    );
  }

  // Don't do anything if there is more than return sideEffects after the
  // ifstatement.
  @Test public void test03() {
    trimmingOf("if (a == null) { \n" //
        + "a = f(); \n" //
        + "if (g()) \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
        + "} \n" //
        + "x=3; \n" //
        + "return null;" //
    ).stays();
  }

  // Works with the current wring.
  @Test public void test04() {
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
            ).gives("if(!b1) \n" //
                + "return i(); \n" //
                + "if(!b2) \n" //
                + "return h(); \n" //
                + "x = f(); \n" //
                + "return g(); \n" //
    );
  }

  // Works with the original wring.
  @Test public void test05() {
    trimmingOf("if(x > y){ \n" //
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

  // Works with the orignal wring.
  @Test public void test06() {
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

  // Passes with original wring too...
  @Test public void test07() {
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

  @Test public void test08() {
    trimmingOf("if( x != null){" + "x = f();" + "return g();" + "}" + "return null;")
        .gives("if( x == null)" + "return null;" + "x = f();" + "return g();");
  }

  @Ignore public void test09() {
    trimmingOf("if(b1){ \n" //
        + "for(;;) \n" //
        + "return x; \n" //
        + "if(x!=x) \n" //
        + "return y; \n" //
        + "} \n" //
        + "return z;")
            .gives("if(!b1) \n" //
                + "return z; \n" //
                + "for(;;) \n" //
                + "return x; \n" //
                + "if(x!=x) \n" //
                + "return y;");
  }

  @Ignore public void test10() {
    trimmingOf("if(b1){ \n" //
        + "x=5; \n" //
        + "while(b2) \n" //
        + "return x; \n" //
        + "} \n" //
        + "return z;")
            .gives("if(!b1) \n" //
                + "return z; \n" //
                + "x=5; \n" //
                + "while(b2){ \n" //
                + "return x; \n" //
                + "}");
  }

  @Ignore public void test11() {
    trimmingOf("if(onoes() && omigod()){" //
        + "if(panic())" //
        + "return weGonnaDie();" //
        + "if(!panic())" //
        + "return meh();" //
        + "}" //
        + "return noExcitement();" //
    ).gives("if(!onoes() || !omigod())" //
        + "return noExcitement();" //
        + "if(panic())" //
        + "return weGonnaDie();" //
        + "if(!panic())" //
        + "return meh();" //
    );
  }

  @Ignore public void test12() {
    trimmingOf("if(b1){" + "if(b2){" + "while(b3)" + "return x;" + "while(!b3)" + "return y;" + "}" + "return z;" + "}" + "return w;")
        .gives("if(!b1)" + "return w;" + "if(b2){" + "while(b3)" + "return x;" + "while(!b3)" + "return y;" + "}" + "return z;")
        .gives("if(!b1)" + "return w;" + "if(!b2)" + "return z;" + "while(b3)" + "return x;" + "while(!b3)" + "return y;");
  }

  @Ignore public void test13() {
    trimmingOf("if(b1){" + "x=3;" + "if(b2)" + "return f();" + "y=7;" + "return g();" + "foo();" + "}" + "return h();")
        .gives("if(!b1)" + "return h();" + "x=3;" + "if(b2)" + "return f();" + "y=7;" + "return g();" + "foo();");
  }
}
