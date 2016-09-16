package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method") public class Issue194Test {
  @Ignore public void test01() {
    trimming("if (a != null) { \n" //
        + "a = f(); \n" //
        + "if (g()) \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
        + "} \n" //
        + "return null;" //
    ).to("if (a == null) \n" + "return null;" //
        + "a = f(); \n" //
        + "if (g()); \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
    );
  }

  @Ignore public void test02() {
    trimming("if (a == null) { \n" //
        + "a = f(); \n" //
        + "if (g()) \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
        + "} \n" //
        + "return null;" //
    ).to("if (a != null) \n" + "return null;" //
        + "a = f(); \n" //
        + "if (g()); \n" //
        + "return f(); \n" //
        + "if (f() && g()) \n" //
        + "return a; \n" //
    );
  }

  // Don't do anything if there is more than return statements after the
  // ifstatement.
  @Test public void test03() {
    trimming("if (a == null) { \n" //
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
    trimming("if(b1){ \n" //
        + "if(b2){ \n" //
        + "x = f(); \n" //
        + "return g(); \n" //
        + "} \n" //
        + "return h(); \n" //
        + "} \n" //
        + "return i();")
            .to("if(!b1) \n" //
                + "return i(); \n" //
                + "if(b2){ \n" //
                + "x = f(); \n" //
                + "return g(); \n"//
                + "}" //
                + "return h(); \n" //
            ).to("if(!b1) \n" //
                + "return i(); \n" //
                + "if(!b2) \n" //
                + "return h(); \n" //
                + "x = f(); \n" //
                + "return g(); \n" //
    );
  }

  // Works with the original wring.
  @Test public void test05() {
    trimming("if(x > y){ \n" //
        + "x*=y; \n" //
        + "y*=z; \n" //
        + "z*=x*=y*=z; \n" //
        + "return z; \n" //
        + "} \n" //
        + "return x;")
            .to("if(x <= y) \n" //
                + "return x; \n" //
                + "x*=y; \n" //
                + "y*=z; \n" //
                + "z*=x*=y*=z; \n" //
                + "return z; \n" //
    );
  }

  // Works with the orignal wring.
  @Test public void test06() {
    trimming("if(x == y){ \n" //
        + "x*=y; \n" //
        + "y*=z; \n" //
        + "z*=x*=y*=z; \n" //
        + "return z; \n" //
        + "} \n" //
        + "return x;")
            .to("if(x != y) \n" //
                + "return x; \n" //
                + "x*=y; \n" //
                + "y*=z; \n" //
                + "z*=x*=y*=z; \n" //
                + "return z; \n" //
    );
  }

  // Passes with original wring too...
  @Test public void test07() {
    trimming("if(x != null){ \n" //
        + "x*=y; \n" //
        + "y*=z; \n" //
        + "z*=x*=y*=z; \n" //
        + "return z; \n" //
        + "} \n" //
        + "return x;")
            .to("if(x == null) \n" //
                + "return x; \n" //
                + "x*=y; \n" //
                + "y*=z; \n" //
                + "z*=x*=y*=z; \n" //
                + "return z; \n" //
    );
  }

  @Test public void test08() {
    trimming("if( x != null){" + "x = f();" + "return g();" + "}" + "return null;")
        .to("if( x == null)" + "return null;" + "x = f();" + "return g();");
  }

  @Ignore public void test09() {
    trimming("if(b1){ \n" //
        + "for(;;) \n" //
        + "return x; \n" //
        + "if(x!=x) \n" //
        + "return y; \n" //
        + "} \n" //
        + "return z;")
            .to("if(!b1) \n" //
                + "return z; \n" //
                + "for(;;) \n" //
                + "return x; \n" //
                + "if(x!=x) \n" //
                + "return y;");
  }

  @Ignore public void test10() {
    trimming("if(b1){ \n" //
        + "x=5; \n" //
        + "while(b2) \n" //
        + "return x; \n" //
        + "} \n" //
        + "return z;")
            .to("if(!b1) \n" //
                + "return z; \n" //
                + "x=5; \n" //
                + "while(b2){ \n" //
                + "return x; \n" //
                + "}");
  }

  @Ignore public void test11() {
    trimming("if(onoes() && omigod()){" //
        + "if(panic())" //
        + "return weGonnaDie();" //
        + "if(!panic())" //
        + "return meh();" //
        + "}" //
        + "return noExcitement();" //
    ).to("if(!onoes() || !omigod())" //
        + "return noExcitement();" //
        + "if(panic())" //
        + "return weGonnaDie();" //
        + "if(!panic())" //
        + "return meh();" //
    );
  }

  @Ignore public void test12() {
    trimming("if(b1){" + "if(b2){" + "while(b3)" + "return x;" + "while(!b3)" + "return y;" + "}" + "return z;" + "}" + "return w;")
        .to("if(!b1)" + "return w;" + "if(b2){" + "while(b3)" + "return x;" + "while(!b3)" + "return y;" + "}" + "return z;")
        .to("if(!b1)" + "return w;" + "if(!b2)" + "return z;" + "while(b3)" + "return x;" + "while(!b3)" + "return y;");
  }

  @Ignore public void test13() {
    trimming("if(b1){" + "x=3;" + "if(b2)" + "return f();" + "y=7;" + "return g();" + "foo();" + "}" + "return h();")
        .to("if(!b1)" + "return h();" + "x=3;" + "if(b2)" + "return f();" + "y=7;" + "return g();" + "foo();");
  }
}
